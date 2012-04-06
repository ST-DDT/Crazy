package de.st_ddt.crazyutil.action;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.NamedRunnable;

public abstract class Action implements NamedRunnable
{

	protected final String name;

	public static Action load(ConfigurationSection config)
	{
		if (config == null)
			return null;
		String type = config.getString("type", "-1");
		if (type == "-1")
		{
			System.out.println("Invalid Action Type!");
			return null;
		}
		Class<?> clazz = null;
		try
		{
			clazz = Class.forName(type);
		}
		catch (ClassNotFoundException e)
		{
			try
			{
				clazz = Class.forName("de.st_ddt.crazyutil.action." + type);
			}
			catch (ClassNotFoundException e2)
			{
				e.printStackTrace();
				return null;
			}
		}
		if (clazz.getClass().isAssignableFrom(Action.class))
		{
			System.out.println("Invalid ActionType " + clazz.toString().substring(6));
			return null;
		}
		Action action = null;
		try
		{
			action = (Action) clazz.getConstructor(ConfigurationSection.class).newInstance(config);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return action;
	}

	public Action(ConfigurationSection config)
	{
		name = config.getName();
	}

	public Action(String name)
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

	public void save(ConfigurationSection config, String path)
	{
		config.set(path + "type", getClass().getName());
	}
}
