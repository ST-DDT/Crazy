package de.st_ddt.crazyutil.databases;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigurationDatabase<S extends ConfigurationDatabaseEntry> extends BasicDatabase<S>
{

	private final JavaPlugin plugin;
	final ConfigurationSection config;
	final String path;
	final String[] columnNames;
	private final Runnable delayedSave = new Runnable()
	{

		@Override
		public void run()
		{
			if (requireSave)
				saveDatabaseDelayed();
		}
	};
	private boolean requireSave = false;

	public ConfigurationDatabase(final Class<S> clazz, final String[] defaultColumnNames, final String defaultPath, final JavaPlugin plugin, final ConfigurationSection config)
	{
		super(DatabaseType.CONFIG, clazz, defaultColumnNames);
		this.plugin = plugin;
		this.config = plugin.getConfig();
		if (config == null)
		{
			this.path = defaultPath;
			this.columnNames = defaultColumnNames;
		}
		else
		{
			this.path = config.getString("CONFIG.path", defaultPath);
			this.columnNames = new String[defaultColumnNames.length];
			for (int i = 0; i < defaultColumnNames.length; i++)
				columnNames[i] = config.getString("CONFIG.columns." + defaultColumnNames[i], defaultColumnNames[i]);
		}
	}

	public ConfigurationDatabase(final Class<S> clazz, final String[] defaultColumnNames, final JavaPlugin plugin, final String path, final String[] columnNames)
	{
		super(DatabaseType.CONFIG, clazz, defaultColumnNames);
		this.plugin = plugin;
		this.config = plugin.getConfig();
		this.path = path;
		this.columnNames = columnNames;
	}

	@Override
	Constructor<S> getConstructor(final Class<S> clazz)
	{
		try
		{
			return clazz.getConstructor(ConfigurationSection.class, String[].class);
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
		loadAllEntries();
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
		ConfigurationSection section = config.getConfigurationSection(path + "." + key.toLowerCase());
		final boolean nameCase;
		if (section == null)
		{
			section = config.getConfigurationSection(path + "." + key);
			nameCase = true;
		}
		else
			nameCase = false;
		if (section == null)
			return null;
		try
		{
			final S data = constructor.newInstance(section, columnNames);
			if (data.getName() == null)
			{
				System.err.println("Entry " + key + " was corrupted and could be fixed.");
				if (nameCase)
					config.set(path + "." + key + "." + columnNames[0], key);
				else
					config.set(path + "." + key.toLowerCase() + "." + columnNames[0], key);
				return loadEntry(key);
			}
			datas.put(data.getName().toLowerCase(), data);
			if (nameCase || !key.equals(data.getName()))
			{
				config.set(path + "." + key, null);
				save(data);
			}
			return data;
		}
		catch (final InvocationTargetException e)
		{
			System.err.println("Error occured while trying to load entry: " + key);
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
		if (config.getConfigurationSection(path) == null)
			return;
		for (final String key : config.getConfigurationSection(path).getKeys(false))
			loadEntry(key);
	}

	@Override
	public boolean deleteEntry(final String key)
	{
		config.set(path + "." + key.toLowerCase(), null);
		final boolean res = super.deleteEntry(key);
		asyncSaveDatabase();
		return res;
	}

	@Override
	public void save(final S entry)
	{
		if (entry == null)
			return;
		super.save(entry);
		entry.saveToConfigDatabase(config, path + "." + entry.getName().toLowerCase() + ".", columnNames);
		asyncSaveDatabase();
	}

	@Override
	public void saveAll(final Collection<S> entries)
	{
		for (final S entry : entries)
		{
			super.save(entry);
			entry.saveToConfigDatabase(config, path + "." + entry.getName().toLowerCase() + ".", columnNames);
		}
		asyncSaveDatabase();
	}

	@Override
	public void purgeDatabase()
	{
		config.set(path, null);
		super.purgeDatabase();
	}

	@Override
	public void saveDatabase()
	{
		synchronized (getDatabaseLock())
		{
			for (final S entry : datas.values())
				if (entry != null)
					entry.saveToConfigDatabase(config, path + "." + entry.getName().toLowerCase() + ".", columnNames);
		}
		plugin.saveConfig();
	}

	@SuppressWarnings("deprecation")
	final void asyncSaveDatabase()
	{
		if (!requireSave)
		{
			requireSave = true;
			Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, delayedSave);
		}
	}

	private void saveDatabaseDelayed()
	{
		plugin.saveConfig();
		requireSave = false;
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		config.set(path + "CONFIG.path", this.path);
		for (int i = 0; i < defaultColumnNames.length; i++)
			config.set(path + "CONFIG.columns." + defaultColumnNames[i], columnNames[i]);
	}
}
