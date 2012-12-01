package de.st_ddt.crazyutil.trigger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazyutil.NamedRunnable;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;

public abstract class Trigger implements NamedRunnable
{

	public String name;
	public Set<NamedRunnable> actions = new HashSet<NamedRunnable>();
	public boolean enabled;
	protected final JavaPlugin plugin;

	public static Trigger load(final ConfigurationSection config, final Set<? extends NamedRunnable> actionlist, final JavaPlugin plugin)
	{
		if (config == null)
			return null;
		return ObjectSaveLoadHelper.load(config, Trigger.class, new Class[] { ConfigurationSection.class, Set.class, JavaPlugin.class }, new Object[] { config, actionlist, plugin });
	}

	public Trigger(final String name, final Set<NamedRunnable> actionlist, final JavaPlugin plugin)
	{
		super();
		this.name = name;
		this.actions = actionlist;
		this.enabled = true;
		this.plugin = plugin;
	}

	public Trigger(final ConfigurationSection config, final Set<NamedRunnable> actionlist, final JavaPlugin plugin)
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
	public final String getName()
	{
		return name;
	}

	public final boolean isEnabled()
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

	public final void addAction(final NamedRunnable action)
	{
		if (!actions.contains(action))
			actions.add(action);
	}

	public final void removeAction(final NamedRunnable action)
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
