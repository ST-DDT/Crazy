package de.st_ddt.crazyutil.databases;

import java.lang.reflect.Constructor;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigurationDatabase<S extends ConfigurationDatabaseEntry> extends BasicDatabase<S>
{

	private final JavaPlugin plugin;
	private final ConfigurationSection config;
	private final String path;
	private final String[] columnNames;

	public ConfigurationDatabase(final Class<S> clazz, final String[] defaultColumnNames, final String defaultPath, final JavaPlugin plugin, final ConfigurationSection config)
	{
		super(DatabaseType.CONFIG, clazz, getConstructor(clazz), defaultColumnNames);
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
		super(DatabaseType.CONFIG, clazz, getConstructor(clazz), defaultColumnNames);
		this.plugin = plugin;
		this.config = plugin.getConfig();
		this.path = path;
		this.columnNames = columnNames;
	}

	private static <S> Constructor<S> getConstructor(final Class<S> clazz)
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
	public boolean isCachedDatabase()
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
		boolean nameCase = false;
		if (section == null)
		{
			section = config.getConfigurationSection(path + "." + key);
			nameCase = true;
		}
		if (section == null)
			return null;
		try
		{
			final S data = constructor.newInstance(section, columnNames);
			datas.put(key.toLowerCase(), data);
			if (nameCase)
			{
				config.set(path + "." + key, null);
				save(data);
			}
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
		if (config.getConfigurationSection(path) == null)
			return;
		for (final String key : config.getConfigurationSection(path).getKeys(false))
			loadEntry(key);
	}

	@Override
	public boolean deleteEntry(final String key)
	{
		config.set(path + "." + key.toLowerCase(), null);
		return super.deleteEntry(key);
	}

	@Override
	public void save(final S entry)
	{
		super.save(entry);
		entry.saveToConfigDatabase(config, path + "." + entry.getName().toLowerCase() + ".", columnNames);
		if (!bulkOperation)
			saveDatabase();
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
		plugin.saveConfig();
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
