package de.st_ddt.crazyutil.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

public class ActionList_RANDOM extends ActionList
{

	protected int amount;

	public ActionList_RANDOM(final ConfigurationSection config)
	{
		super(config);
		amount = config.getInt("amount", 1);
		amount = Math.max(amount, actions.size());
	}

	public ActionList_RANDOM(final String name, final Collection<? extends Action> actions)
	{
		super(name, actions);
		this.amount = 1;
	}

	public ActionList_RANDOM(final String name)
	{
		super(name);
		this.amount = 1;
	}

	@Override
	public void run()
	{
		final List<Action> list = new ArrayList<Action>();
		for (final Action action : actions)
			list.add(action);
		Collections.shuffle(list);
		for (int i = 0; i < amount; i++)
			list.get(i).run();
	}
}
