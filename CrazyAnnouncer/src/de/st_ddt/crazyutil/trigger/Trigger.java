package de.st_ddt.crazyutil.trigger;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazyutil.NamedRunnable;

public abstract class Trigger implements NamedRunnable
{

	public String name;
	public List<NamedRunnable> actions = new ArrayList<NamedRunnable>();
	public boolean enabled;
	protected final JavaPlugin plugin;

	public static Trigger load(final ConfigurationSection config, final List<? extends NamedRunnable> actionlist, final JavaPlugin plugin)
	{
		if (config == null)
			return null;
		final String type = config.getString("type", "-1");
		if (type == "-1")
		{
			System.out.println("Invalid Trigger Type!");
			return null;
		}
		Class<?> clazz = null;
		try
		{
			clazz = Class.forName(type);
		}
		catch (final ClassNotFoundException e)
		{
			try
			{
				clazz = Class.forName("de.st_ddt.crazyutil.trigger." + type);
			}
			catch (final ClassNotFoundException e2)
			{
				e.printStackTrace();
				return null;
			}
		}
		if (clazz.getClass().isAssignableFrom(Trigger.class))
		{
			System.out.println("Invalid TriggerType " + clazz.toString().substring(6));
			return null;
		}
		Trigger trigger = null;
		try
		{
			trigger = (Trigger) clazz.getConstructor(ConfigurationSection.class, List.class, JavaPlugin.class).newInstance(config, actionlist, plugin);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return trigger;
	}

	public Trigger(final String name, final List<NamedRunnable> actionlist, final JavaPlugin plugin)
	{
		super();
		this.name = name;
		this.actions = actionlist;
		this.enabled = true;
		this.plugin = plugin;
	}

	public Trigger(final ConfigurationSection config, final List<NamedRunnable> actionlist, final JavaPlugin plugin)
	{
		super();
		this.name = config.getName();
		final List<String> actionnames = config.getStringList("actions");
		for (final NamedRunnable action : actionlist)
			if (actionnames.contains(action.getName()))
				this.actions.add(action);
		this.enabled = config.getBoolean("enabled", true);
		this.plugin = plugin;
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		final List<String> actionnames = new ArrayList<String>();
		config.set(path + "type", this.getClass().getName());
		for (final NamedRunnable action : actions)
			actionnames.add(action.getName());
		config.set(path + "actions", actionnames);
		config.set(path + "enabled", enabled);
	}

	@Override
	public String getName()
	{
		return name;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(final boolean enabled)
	{
		if (this.enabled != enabled)
			if (enabled)
				register();
			else
				unregister();
		this.enabled = enabled;
	}

	public void addAction(final NamedRunnable action)
	{
		if (!actions.contains(action))
			actions.add(action);
	}

	public void removeAction(final NamedRunnable action)
	{
		actions.remove(action);
	}

	public abstract boolean needToBeSaved();

	public abstract void register();

	public abstract void unregister();

	@Override
	public void run()
	{
		if (!enabled)
			return;
		for (final Runnable action : actions)
			action.run();
	}
}
