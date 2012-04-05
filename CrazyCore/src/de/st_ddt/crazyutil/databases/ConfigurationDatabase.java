package de.st_ddt.crazyutil.databases;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

public class ConfigurationDatabase<S extends DatabaseSaveable, T extends ConfigurationDatabaseEntry<S>> extends Database<S, T>
{

	protected final ConfigurationSection config;
	protected String table;

	public ConfigurationDatabase(T entrymaker, ConfigurationSection config, String table)
	{
		super(DatabaseTypes.FLAT, entrymaker);
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
		return entrymaker.load(section);
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
		entrymaker.save(entry, config);
	}
}
