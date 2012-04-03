package de.st_ddt.crazyutil.action;

import org.bukkit.configuration.ConfigurationSection;

public class Action_ORDERED extends ActionList
{

	protected int currentIndex;

	public Action_ORDERED(ConfigurationSection config)
	{
		super(config);
		currentIndex = config.getInt("currentIndex", 0);
		if (currentIndex >= actions.size())
			currentIndex = 0;
	}

	@Override
	public void run()
	{
		int size = actions.size();
		if (size == 0)
			return;
		if (currentIndex >= size)
			currentIndex = 0;
		Action action = actions.get(currentIndex);
		if (action != null)
			action.run();
		currentIndex++;
	}

	@Override
	public void save(ConfigurationSection config, String path)
	{
		super.save(config, path);
		config.set(path + "currentIndex", currentIndex);
	}
}
