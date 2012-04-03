package de.st_ddt.crazyutil.trigger;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyutil.action.NamedRunnable;

public class ScheduledTrigger extends Trigger
{

	protected Date date;
	protected int taskID = -1;

	public ScheduledTrigger(ConfigurationSection config, List<NamedRunnable> actionlist, JavaPlugin plugin)
	{
		super(config, actionlist, plugin);
		String datesString = config.getString("date");
		try
		{
			this.date = CrazyCore.DateFormat.parse(datesString);
		}
		catch (ParseException e)
		{
			this.date = new Date();
		}
		register();
	}

	public ScheduledTrigger(String name, List<NamedRunnable> actionlist, JavaPlugin plugin, Date date)
	{
		super(name, actionlist, plugin);
		this.date = date;
		register();
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		if (enabled != this.enabled)
			if (enabled)
				super.setEnabled(enabled);
	}

	@Override
	public boolean needToBeSaved()
	{
		return new Date().before(date);
	}

	@Override
	public void register()
	{
		if (taskID != -1)
			if (new Date().before(date))
				taskID = plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, this, (date.getTime() - new Date().getTime()) / 50);
	}

	@Override
	public void unregister()
	{
		if (taskID != -1)
			plugin.getServer().getScheduler().cancelTask(taskID);
		taskID = -1;
	}

	@Override
	public void run()
	{
		super.run();
		taskID = -1;
	}
}
