package de.st_ddt.crazydetectorsign.actions;

import org.bukkit.Bukkit;

import de.st_ddt.crazydetectorsign.CrazyDetectorSign;

public class TimedAction implements Action
{

	protected final long interval;
	protected final int taskID;
	protected final Runnable runnable;

	@SuppressWarnings("deprecation")
	public TimedAction(final long interval, final Runnable runnable)
	{
		super();
		this.interval = interval;
		this.runnable = runnable;
		this.taskID = Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(CrazyDetectorSign.getPlugin(), runnable, 20, interval * 20);
	}

	@Override
	public void disable()
	{
		Bukkit.getServer().getScheduler().cancelTask(taskID);
	}
}
