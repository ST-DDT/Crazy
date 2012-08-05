package de.st_ddt.crazyutil.databases;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.HashMap;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.ChatHelper;

public class FlatDatabase<S extends FlatDatabaseEntry> extends BasicDatabase<S>
{

	protected final File file;
	protected HashMap<String, String[]> entries = new HashMap<String, String[]>();

	public FlatDatabase(final Class<S> clazz, final String tableName, final ConfigurationSection config, final String[] columnNames, final File file)
	{
		super(DatabaseType.FLAT, clazz, tableName, config, columnNames, getConstructor(clazz));
		this.file = file;
		checkTable();
		loadFile();
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
	public void checkTable()
	{
		file.getAbsoluteFile().getParentFile().mkdirs();
	}

	private synchronized void loadFile()
	{
		FileInputStream fileInputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufreader = null;
		try
		{
			fileInputStream = new FileInputStream(file);
			inputStreamReader = new InputStreamReader(fileInputStream);
			bufreader = new BufferedReader(inputStreamReader);
			// ErsteZeile Skippen
			String zeile = bufreader.readLine();
			while ((zeile = bufreader.readLine()) != null)
			{
				if (zeile.equals(""))
					continue;
				final String[] split = zeile.split("\\|");
				if (split == null)
					continue;
				if (split.length == 0)
					continue;
				try
				{
					entries.put(split[0].toLowerCase(), split);
				}
				catch (final ArrayIndexOutOfBoundsException e)
				{
					System.err.println("Invalid line " + zeile);
				}
			}
		}
		catch (final FileNotFoundException e)
		{}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (bufreader != null)
				try
				{
					bufreader.close();
				}
				catch (final IOException e)
				{}
			if (inputStreamReader != null)
				try
				{
					inputStreamReader.close();
				}
				catch (final IOException e)
				{}
			if (fileInputStream != null)
				try
				{
					fileInputStream.close();
				}
				catch (final IOException e)
				{}
		}
	}

	private synchronized void saveFile()
	{
		FileWriter writer = null;
		try
		{
			writer = new FileWriter(file);
			writer.write(ChatHelper.listingString("|", columnNames) + System.getProperty("line.separator"));
			for (final String[] strings : entries.values())
				if (strings != null)
					writer.write(ChatHelper.listingString("|", strings) + System.getProperty("line.separator"));
			writer.flush();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (writer != null)
				try
				{
					writer.close();
				}
				catch (final IOException e)
				{
					e.printStackTrace();
				}
		}
	}

	@Override
	public void saveDatabase()
	{
		saveFile();
	}

	@Override
	public S loadEntry(final String key)
	{
		final String[] rawData = entries.get(key.toLowerCase());
		if (rawData == null)
			return null;
		try
		{
			final S data = constructor.newInstance(new Object[] { rawData });
			datas.put(key.toLowerCase(), data);
			return data;
		}
		catch (final Exception e)
		{
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
		entries.put(key.toLowerCase(), null);
		if (!bulkOperation)
			saveDatabase();
		return super.deleteEntry(key);
	}

	@Override
	public void save(final S entry)
	{
		super.save(entry);
		entries.put(entry.getName().toLowerCase(), entry.saveToFlatDatabase());
		if (!bulkOperation)
			saveFile();
	}
}
