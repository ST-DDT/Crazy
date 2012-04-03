package de.st_ddt.crazyutil.trigger;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazyutil.action.NamedRunnable;
import de.st_ddt.crazyutil.databases.Saveable;

public abstract class Trigger implements Saveable, Runnable
{

	public String name;
	public List<NamedRunnable> actions = new ArrayList<NamedRunnable>();
	public boolean enabled;
	protected final JavaPlugin plugin;

	public Trigger(String name, List<NamedRunnable> actionlist, JavaPlugin plugin)
	{
		super();
		this.name = name;
		this.actions = actionlist;
		this.enabled = true;
		this.plugin = plugin;
	}

	public Trigger(ConfigurationSection config, List<NamedRunnable> actionlist, JavaPlugin plugin)
	{
		super();
		this.name = config.getName();
		List<String> actionnames = config.getStringList("actions");
		for (NamedRunnable action : actionlist)
			if (actionnames.contains(action.getName()))
				this.actions.add(action);
		this.enabled = config.getBoolean("enabled", true);
		this.plugin = plugin;
	}

	@Override
	public void save(ConfigurationSection config, String path)
	{
		List<String> actionnames = new ArrayList<String>();
		config.set(path + "typ", this.getClass().getName().substring(6));
		for (NamedRunnable action : actions)
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

	public void setEnabled(boolean enabled)
	{
		if (this.enabled != enabled)
			if (enabled)
				register();
			else
				unregister();
		this.enabled = enabled;
	}

	public void addAction(NamedRunnable action)
	{
		if (!actions.contains(action))
			actions.add(action);
	}

	public void removeAction(NamedRunnable action)
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
		for (Runnable action : actions)
			action.run();
	}
}
