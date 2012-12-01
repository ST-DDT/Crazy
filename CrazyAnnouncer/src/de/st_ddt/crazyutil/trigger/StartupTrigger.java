package de.st_ddt.crazyutil.trigger;

import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazyutil.NamedRunnable;

public class StartupTrigger extends Trigger
{

	public StartupTrigger(final ConfigurationSection config, final Set<NamedRunnable> actionlist, final JavaPlugin plugin)
	{
		super(config, actionlist, plugin);
	}

	public StartupTrigger(final String name, final Set<NamedRunnable> actionlist, final JavaPlugin plugin)
	{
		super(name, actionlist, plugin);
	}

	@Override
	public boolean needToBeSaved()
	{
		return true;
	}

	@Override
	public void register()
	{
		run();
	}

	@Override
	public void unregister()
	{
	}
}
