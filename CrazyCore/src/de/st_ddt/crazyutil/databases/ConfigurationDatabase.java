package de.st_ddt.crazyutil.databases;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

public class ConfigurationDatabase<S extends Saveable, T extends DatabaseEntry<S, ? extends ConfigurationSection>> extends Database<S, T>
{

	protected final ConfigurationSection config;
	protected final Class<S> clazz;
	protected String path;

	public ConfigurationDatabase(ConfigurationSection config, String path, Class<S> clazz)
	{
		super(DatabaseTypes.FLAT);
		this.config = config;
		this.clazz = clazz;
		this.path = path;
	}

	@Override
	public void checkTable()
	{
		return;
	}

	@Override
	public S getEntry(String key)
	{
		ConfigurationSection section = config.getConfigurationSection(path + "." + key);
		if (section == null)
			return null;
		try
		{
			return clazz.getConstructor(ConfigurationSection.class).newInstance(section);
		}
		catch (Exception e)
		{
			// e.printStackTrace();
		}
		return null;
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
		System.out.println("Get");
		if (config.getConfigurationSection(path) == null)
			return list;
		System.out.println("notnull");
		for (String key : config.getConfigurationSection(path).getKeys(false))
		{
			System.out.println(path + "." + key);
			list.add(getEntry(key));
		}
		System.out.println("return");
		return list;
	}

	@Override
	public void delete(String key)
	{
		config.set(path + "." + key, null);
	}

	@Override
	public void save(S entry)
	{
		entry.save(config, path + "." + entry.getName() + ".");
	}
}
