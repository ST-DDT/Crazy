package de.st_ddt.crazypromoter.tasks;

import de.st_ddt.crazypromoter.CrazyPromoter;

public class CheckTask implements Runnable
{

	private final CrazyPromoter plugin;

	public CheckTask(final CrazyPromoter plugin)
	{
		super();
		this.plugin = plugin;
	}

	@Override
	public void run()
	{
		plugin.checkStatus();
	}
}
