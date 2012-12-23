package de.st_ddt.crazyutil.databases;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazyutil.ChatHelper;

public class FlatDatabase<S extends FlatDatabaseEntry> extends BasicDatabase<S>
{

	private final static String lineSeparator = System.getProperty("line.separator");
	protected final static Pattern PATTERN_DATASEPARATOR = Pattern.compile("\\|");
	private final JavaPlugin plugin;
	private final Map<String, String> entries = new TreeMap<String, String>();
	private final String filePath;
	private final File file;
	private final File backupFile;
	private final Lock lock = new ReentrantLock();
	private final Runnable delayedSave = new Runnable()
	{

		@Override
		public void run()
		{
			if (requireSave)
				saveDatabase();
		}
	};
	private boolean requireSave = false;

	public FlatDatabase(final Class<S> clazz, final String[] defaultColumnNames, final String defaultPath, final JavaPlugin plugin, final ConfigurationSection config)
	{
		super(DatabaseType.FLAT, clazz, getConstructor(clazz), defaultColumnNames);
		this.plugin = plugin;
		this.filePath = config == null ? defaultPath : config.getString("FLAT.filePath", defaultPath);
		this.file = new File(plugin.getDataFolder().getPath(), filePath);
		this.backupFile = new File(plugin.getDataFolder().getPath(), filePath + "_old");
	}

	public FlatDatabase(final Class<S> clazz, final String[] defaultColumnNames, final JavaPlugin plugin, final String path)
	{
		super(DatabaseType.FLAT, clazz, getConstructor(clazz), defaultColumnNames);
		this.plugin = plugin;
		this.filePath = path;
		this.file = new File(plugin.getDataFolder(), filePath);
		this.backupFile = new File(plugin.getDataFolder().getPath(), filePath + "_old");
	}

	private static <S> Constructor<S> getConstructor(final Class<S> clazz)
	{
		try
		{
			return clazz.getConstructor(String[].class);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void initialize()
	{
		checkTable();
		loadFile();
		loadAllEntries();
	}

	@Override
	public void checkTable()
	{
		file.getAbsoluteFile().getParentFile().mkdirs();
	}

	@Override
	public final boolean isStaticDatabase()
	{
		return true;
	}

	@Override
	public final boolean isCachedDatabase()
	{
		return true;
	}

	@Override
	public final S updateEntry(final String key)
	{
		return getEntry(key);
	}

	@Override
	public S loadEntry(final String key)
	{
		final String rawData = entries.get(key.toLowerCase());
		if (rawData == null)
			return null;
		try
		{
			final S data = constructor.newInstance(new Object[] { PATTERN_DATASEPARATOR.split(rawData.trim()) });
			datas.put(key.toLowerCase(), data);
			return data;
		}
		catch (final InvocationTargetException e)
		{
			System.err.println("Error occured while trying to load entry: " + key);
			if (e.getCause() instanceof ArrayIndexOutOfBoundsException)
			{
				final int count = defaultColumnNames.length - StringUtils.countMatches(rawData, "|") - 1;
				if (count > 0)
				{
					System.out.println("Trying to fix entry");
					final StringBuilder builder = new StringBuilder(rawData.trim());
					for (int i = 0; i < count; i++)
						builder.append("|");
					builder.append(".|");
					builder.append(lineSeparator);
					entries.put(key, builder.toString());
					final S data = loadEntry(key);
					if (data != null)
					{
						save(data);
						System.out.println("Entry FIXED!");
					}
					else
						System.err.println("Repair FAILED!");
					return data;
				}
			}
			shortPrintStackTrace(e, e.getCause());
			return null;
		}
		catch (final Exception e)
		{
			System.err.println("Error occured while trying to load entry: " + key);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void loadAllEntries()
	{
		for (final String key : entries.keySet())
			loadEntry(key);
	}

	@Override
	public boolean deleteEntry(final String key)
	{
		entries.remove(key.toLowerCase());
		final boolean res = super.deleteEntry(key);
		asyncSaveDatabase();
		return res;
	}

	@Override
	public void save(final S entry)
	{
		super.save(entry);
		entries.put(entry.getName().toLowerCase(), ChatHelper.listingString("|", entry.saveToFlatDatabase()) + lineSeparator);
		asyncSaveDatabase();
	}

	@Override
	public void saveAll(final Collection<S> entries)
	{
		for (final S entry : entries)
		{
			super.save(entry);
			this.entries.put(entry.getName().toLowerCase(), ChatHelper.listingString("|", entry.saveToFlatDatabase()) + lineSeparator);
		}
		asyncSaveDatabase();
	}

	@Override
	public void purgeDatabase()
	{
		entries.clear();
		super.purgeDatabase();
	}

	@Override
	public void saveDatabase()
	{
		saveFile();
	}

	@SuppressWarnings("deprecation")
	public void asyncSaveDatabase()
	{
		if (!requireSave)
		{
			requireSave = true;
			Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, delayedSave, 1);
		}
	}

	private void loadFile()
	{
		try
		{
			lock.lock();
			loadFile(backupFile);
			loadFile(file);
		}
		finally
		{
			lock.unlock();
		}
	}

	private void loadFile(final File file)
	{
		BufferedReader reader = null;
		try
		{
			if (!file.exists())
				return;
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			// ErsteZeile Skippen
			String zeile = reader.readLine();
			while ((zeile = reader.readLine()) != null)
			{
				if (zeile.length() == 0)
					continue;
				try
				{
					entries.put(PATTERN_DATASEPARATOR.split(zeile, 2)[0].toLowerCase(), zeile + lineSeparator);
				}
				catch (final ArrayIndexOutOfBoundsException e)
				{
					System.err.println("Invalid line " + zeile);
				}
			}
			reader.close();
		}
		catch (final IOException e)
		{
			if (reader != null)
				try
				{
					reader.close();
				}
				catch (final IOException e1)
				{}
			e.printStackTrace();
		}
	}

	private void saveFile()
	{
		lock.lock();
		try
		{
			backupFile.delete();
			file.renameTo(backupFile);
		}
		catch (final SecurityException e)
		{
			e.printStackTrace();
		}
		Writer writer = null;
		try
		{
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			writer.write(ChatHelper.listingString("|", defaultColumnNames));
			writer.write(lineSeparator);
			for (final String string : entries.values())
				writer.write(string);
			writer.close();
			backupFile.delete();
			requireSave = false;
		}
		catch (final IOException e)
		{
			if (writer != null)
				try
				{
					writer.close();
				}
				catch (final IOException e1)
				{}
			e.printStackTrace();
		}
		finally
		{
			lock.unlock();
		}
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		config.set(path + "FLAT.filePath", filePath);
	}
}
