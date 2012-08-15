package de.st_ddt.crazyutil.action;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyannouncer.CrazyAnnouncer;

public class Action_DELAYEDACTION extends Action_ACTION
{

	protected final long delay;

	public Action_DELAYEDACTION(final ConfigurationSection config)
	{
		super(config);
		delay = config.getLong("delay", 0);
	}

	public Action_DELAYEDACTION(final String name, final String actionname, final long delay)
	{
		super(name, actionname);
		this.delay = delay;
	}

	@Override
	public void run()
	{
		if (action == null)
			action = CrazyAnnouncer.getPlugin().getAction(actionname);
		if (action != null)
			Bukkit.getScheduler().scheduleAsyncDelayedTask(CrazyAnnouncer.getPlugin(), action, delay / 50);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		config.set(path + "delay", delay);
	}
}
