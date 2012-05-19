package de.st_ddt.crazyutil.action;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.NamedRunnable;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;

public abstract class Action implements NamedRunnable
{

	protected final String name;

	public static Action load(final ConfigurationSection config)
	{
		if (config == null)
			return null;
		return ObjectSaveLoadHelper.load(config, Action.class, new Class[] { ConfigurationSection.class }, new Object[] { config });
	}

	public Action(final ConfigurationSection config)
	{
		name = config.getName();
	}

	public Action(final String name)
	{
		this.name = name;
	}

	@Override
	public abstract void run();

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "type", getClass().getName());
	}
}
