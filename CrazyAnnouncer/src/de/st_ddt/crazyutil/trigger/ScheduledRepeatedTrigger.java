package de.st_ddt.crazyutil.trigger;

import java.util.Date;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazyutil.NamedRunnable;

public class ScheduledRepeatedTrigger extends ScheduledTrigger
{

	long interval;
	int repeat;

	public ScheduledRepeatedTrigger(final ConfigurationSection config, final List<NamedRunnable> actionlist, final JavaPlugin plugin)
	{
		super(config, actionlist, plugin);
		config.getInt("interval", 1000);
		config.getInt("repeat", 0);
	}

	public ScheduledRepeatedTrigger(final String name, final List<NamedRunnable> actionlist, final JavaPlugin plugin, final Date date, final long interval, final int repeat)
	{
		super(name, actionlist, plugin, date);
		this.interval = interval;
		this.repeat = repeat;
	}

	@Override
	public void setEnabled(final boolean enabled)
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

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		config.set(path + "repeat", repeat);
		config.set(path + "interval", interval);
	}
}
