package de.st_ddt.crazyutil.trigger;

import java.text.ParseException;
import java.util.Date;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazyplugin.CrazyLightPluginInterface;
import de.st_ddt.crazyutil.NamedRunnable;

public class ScheduledTrigger extends Trigger
{

	protected Date date;
	protected int taskID = -1;

	public ScheduledTrigger(final ConfigurationSection config, final Set<NamedRunnable> actionlist, final JavaPlugin plugin)
	{
		super(config, actionlist, plugin);
		final String datesString = config.getString("date");
		try
		{
			this.date = CrazyLightPluginInterface.DATETIMEFORMAT.parse(datesString);
		}
		catch (final ParseException e)
		{
			this.date = new Date();
		}
	}

	public ScheduledTrigger(final String name, final Set<NamedRunnable> actionlist, final JavaPlugin plugin, final Date date)
	{
		super(name, actionlist, plugin);
		this.date = date;
	}

	@Override
	public void setEnabled(final boolean enabled)
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
		if (taskID == -1)
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

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		config.set(path + "date", CrazyLightPluginInterface.DATETIMEFORMAT.format(date));
	}
}
