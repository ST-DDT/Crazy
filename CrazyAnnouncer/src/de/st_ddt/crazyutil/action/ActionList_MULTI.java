package de.st_ddt.crazyutil.action;

import org.bukkit.configuration.ConfigurationSection;

public class ActionList_MULTI extends ActionList
{

	public ActionList_MULTI(ConfigurationSection config)
	{
		super(config);
	}

	@Override
	public void run()
	{
		for (Action action : actions)
			action.run();
	}
}
