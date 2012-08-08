package de.st_ddt.crazyutil.databases;

import java.lang.reflect.Constructor;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigurationDatabase<S extends ConfigurationDatabaseEntry> extends BasicDatabase<S>
{

	protected final ConfigurationSection config;
	protected final JavaPlugin plugin;

	public ConfigurationDatabase(final Class<S> clazz, final String tableName, final ConfigurationSection config, final String[] columnNames, JavaPlugin plugin)
	{
		super(DatabaseType.CONFIG, clazz, tableName, config, columnNames, getConstructor(clazz));
		this.config = config;
		this.plugin = plugin;
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
	public S loadEntry(final String key)
	{
		ConfigurationSection section = config.getConfigurationSection(tableName + "." + key.toLowerCase());
		boolean nameCase = false;
		if (section == null)
		{
			section = config.getConfigurationSection(tableName + "." + key);
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
				config.set(tableName + "." + key, null);
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
		if (config.getConfigurationSection(tableName) == null)
			return;
		for (final String key : config.getConfigurationSection(tableName).getKeys(false))
			loadEntry(key);
	}

	@Override
	public boolean deleteEntry(final String key)
	{
		config.set(tableName + "." + key.toLowerCase(), null);
		return super.deleteEntry(key);
	}

	@Override
	public synchronized void save(final S entry)
	{
		super.save(entry);
		entry.saveToConfigDatabase(config, tableName + "." + entry.getName().toLowerCase() + ".", columnNames);
	}

	@Override
	public void saveDatabase()
	{
		plugin.saveConfig();
	}
}
