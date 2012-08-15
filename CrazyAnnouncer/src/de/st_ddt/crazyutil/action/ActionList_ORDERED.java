package de.st_ddt.crazyutil.action;

import java.util.Collection;

import org.bukkit.configuration.ConfigurationSection;

public class ActionList_ORDERED extends ActionList
{

	protected int currentIndex;

	public ActionList_ORDERED(final ConfigurationSection config)
	{
		super(config);
		currentIndex = config.getInt("currentIndex", 0);
		if (currentIndex >= actions.size())
			currentIndex = 0;
	}

	public ActionList_ORDERED(final String name, final Collection<? extends Action> actions)
	{
		super(name, actions);
		currentIndex = 0;
	}

	public ActionList_ORDERED(final String name)
	{
		super(name);
		currentIndex = 0;
	}

	@Override
	public void run()
	{
		final int size = actions.size();
		if (size == 0)
			return;
		if (currentIndex >= size)
			currentIndex = 0;
		final Action action = actions.get(currentIndex);
		if (action != null)
			action.run();
		currentIndex++;
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		config.set(path + "currentIndex", currentIndex);
	}
}
