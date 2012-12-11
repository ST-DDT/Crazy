package de.st_ddt.crazyonline.tasks;

import de.st_ddt.crazyonline.CrazyOnline;

public class StatsTask implements Runnable
{

	private final CrazyOnline plugin;

	public StatsTask(final CrazyOnline plugin)
	{
		super();
		this.plugin = plugin;
	}

	@Override
	public void run()
	{
		plugin.dayChange();
	}
}
