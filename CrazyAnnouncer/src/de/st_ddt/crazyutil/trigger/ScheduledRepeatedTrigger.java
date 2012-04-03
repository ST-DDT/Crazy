package de.st_ddt.crazyutil.trigger;

import java.util.Date;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazyutil.action.NamedRunnable;

public class ScheduledRepeatedTrigger extends ScheduledTrigger
{

	long interval;
	int repeat;

	public ScheduledRepeatedTrigger(ConfigurationSection config, List<NamedRunnable> actionlist, JavaPlugin plugin)
	{
		super(config, actionlist, plugin);
		config.getInt("interval", 1000);
		config.getInt("repeat", 0);
	}

	public ScheduledRepeatedTrigger(String name, List<NamedRunnable> actionlist, JavaPlugin plugin, Date date, long interval, int repeat)
	{
		super(name, actionlist, plugin, date);
		this.interval = interval;
		this.repeat = repeat;
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		if (enabled != this.enabled)
			if (enabled)
				super.setEnabled(enabled);
	}

	@Override
	public void run()
	{
		super.run();
		if (repeat == 0)
			return;
		date = new Date(date.getTime() + interval);
		if (repeat != -1)
			repeat--;
		register();
	}
}
