package de.st_ddt.crazyutil.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

public class ActionList_RANDOM extends ActionList
{

	protected int amount;

	public ActionList_RANDOM(ConfigurationSection config)
	{
		super(config);
		amount = config.getInt("amount", 1);
		amount = Math.max(amount, actions.size());
	}

	@Override
	public void run()
	{
		List<Action> list = new ArrayList<Action>();
		for (Action action : actions)
			list.add(action);
		Collections.shuffle(list);
		for (int i = 0; i < amount; i++)
			list.get(i).run();
	}
}
