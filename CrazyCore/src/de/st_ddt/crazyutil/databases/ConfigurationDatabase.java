package de.st_ddt.crazyutil.databases;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

public class ConfigurationDatabase<S extends ConfigurationDatabaseEntry> extends Database<S>
{

	protected final ConfigurationSection config;
	protected String table;

	public ConfigurationDatabase(Class<S> clazz, ConfigurationSection config, String table)
	{
		super(DatabaseTypes.FLAT, clazz);
		this.config = config;
		this.table = table;
	}

	@Override
	public void checkTable()
	{
		return;
	}

	@Override
	public S getEntry(String key)
	{
		ConfigurationSection section = config.getConfigurationSection(table + "." + key);
		if (section == null)
			return null;
		try
		{
			return clazz.getConstructor(ConfigurationSection.class).newInstance(section);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<S> getEntries(String key)
	{
		List<S> list = new ArrayList<S>();
		list.add(getEntry(key));
		return list;
	}

	@Override
	public List<S> getAllEntries()
	{
		List<S> list = new ArrayList<S>();
		if (config.getConfigurationSection(table) == null)
			return list;
		for (String key : config.getConfigurationSection(table).getKeys(false))
			list.add(getEntry(key));
		return list;
	}

	@Override
	public void delete(String key)
	{
		config.set(table + "." + key, null);
	}

	@Override
	public void save(S entry)
	{
		entry.save(config, table + "." + entry.getName() + ".");
	}
}
