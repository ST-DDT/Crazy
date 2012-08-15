package de.st_ddt.crazyutil.action;

import java.util.Collection;

import org.bukkit.configuration.ConfigurationSection;

public class ActionList_MULTI extends ActionList
{

	public ActionList_MULTI(final ConfigurationSection config)
	{
		super(config);
	}

	public ActionList_MULTI(final String name, final Collection<? extends Action> actions)
	{
		super(name, actions);
	}

	public ActionList_MULTI(final String name)
	{
		super(name);
	}

	@Override
	public void run()
	{
		for (final Action action : actions)
			action.run();
	}
}
