package de.st_ddt.crazyutil.databases;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

public class ConfigurationDatabase<S extends ConfigurationDatabaseEntry> extends BasicDatabase<S>
{

	protected final ConfigurationSection config;
	protected String table;

	public ConfigurationDatabase(final Class<S> clazz, final ConfigurationSection config, final String table, final String[] columnNames)
	{
		super(DatabaseType.CONFIG, clazz, columnNames, getConstructor(clazz));
		this.config = config;
		this.table = table;
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
	public void checkTable()
	{
		return;
	}

	@Override
	public String getTableName()
	{
		return table;
	}

	@Override
	public S getEntry(final String key)
	{
		final ConfigurationSection section = config.getConfigurationSection(table + "." + key);
		if (section == null)
			return null;
		try
		{
			return constructor.newInstance(section, columnNames);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<S> getEntries(final String key)
	{
		final List<S> list = new ArrayList<S>();
		list.add(getEntry(key));
		return list;
	}

	@Override
	public List<S> getAllEntries()
	{
		final List<S> list = new ArrayList<S>();
		if (config.getConfigurationSection(table) == null)
			return list;
		for (final String key : config.getConfigurationSection(table).getKeys(false))
			list.add(getEntry(key));
		return list;
	}

	@Override
	public void delete(final String key)
	{
		config.set(table + "." + key, null);
	}

	@Override
	public void save(final S entry)
	{
		entry.saveToConfigDatabase(config, table + "." + entry.getName() + ".", columnNames);
	}

	@Override
	protected void saveDatabase()
	{
	}
}
