package de.st_ddt.crazyutil.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.bukkit.configuration.ConfigurationSection;

public class ActionList_RANDOM extends ActionList
{

	protected int amount;
	protected final ArrayList<Action> random = new ArrayList<Action>();

	public ActionList_RANDOM(final ConfigurationSection config)
	{
		super(config);
		amount = config.getInt("amount", 1);
		amount = Math.min(amount, actions.size());
		random.addAll(actions);
	}

	public ActionList_RANDOM(final String name, final Collection<? extends Action> actions)
	{
		super(name, actions);
		this.amount = 1;
		random.addAll(actions);
	}

	public ActionList_RANDOM(final String name)
	{
		super(name);
		this.amount = 1;
	}

	@Override
	public void run()
	{
		Collections.shuffle(random);
		for (int i = 0; i < amount; i++)
			random.get(i).run();
	}

	@Override
	public void save(ConfigurationSection config, String path)
	{
		super.save(config, path);
		config.set(path + "amount", amount);
	}
}
