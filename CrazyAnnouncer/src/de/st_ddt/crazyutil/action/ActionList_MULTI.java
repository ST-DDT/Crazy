package de.st_ddt.crazyutil.action;

import java.util.Collection;

import org.bukkit.configuration.ConfigurationSection;

public class ActionList_MULTI extends ActionList
{

	public ActionList_MULTI(ConfigurationSection config)
	{
		super(config);
	}

	public ActionList_MULTI(String name, Collection<? extends Action> actions)
	{
		super(name, actions);
	}

	public ActionList_MULTI(String name)
	{
		super(name);
	}

	@Override
	public void run()
	{
		for (Action action : actions)
			action.run();
	}
}
