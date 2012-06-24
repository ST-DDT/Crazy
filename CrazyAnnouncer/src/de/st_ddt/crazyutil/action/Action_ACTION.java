package de.st_ddt.crazyutil.action;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyannouncer.CrazyAnnouncer;
import de.st_ddt.crazyutil.NamedRunnable;

public class Action_ACTION extends Action
{

	protected final String actionname;
	protected NamedRunnable action;

	public Action_ACTION(final ConfigurationSection config)
	{
		super(config);
		actionname = config.getString("action");
	}

	public Action_ACTION(final String name, final String actionname)
	{
		super(name);
		this.actionname = actionname;
		action = null;
	}

	@Override
	public void run()
	{
		if (action == null)
			action = CrazyAnnouncer.getPlugin().getAction(actionname);
		if (action != null)
			action.run();
	}

	@Override
	public void save(ConfigurationSection config, String path)
	{
		super.save(config, path);
		if (action == null)
			action = CrazyAnnouncer.getPlugin().getAction(actionname);
		if (action != null)
			config.set(path + "action", action.getName());
	}
}
