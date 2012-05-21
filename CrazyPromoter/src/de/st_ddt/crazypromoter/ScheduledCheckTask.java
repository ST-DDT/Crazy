package de.st_ddt.crazypromoter;

public class ScheduledCheckTask implements Runnable
{

	protected final CrazyPromoter plugin;

	public ScheduledCheckTask(CrazyPromoter plugin)
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
