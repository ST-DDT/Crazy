package de.st_ddt.crazycaptcha.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.st_ddt.crazycaptcha.CrazyCaptcha;
import de.st_ddt.crazycaptcha.captcha.Captcha;

public class CaptchaReminderTask implements Runnable
{

	private final Player player;
	private final CrazyCaptcha plugin;
	private int taskID = -1;

	public CaptchaReminderTask(final Player player, final CrazyCaptcha plugin)
	{
		super();
		this.player = player;
		this.plugin = plugin;
	}

	@Override
	public void run()
	{
		if (!player.isOnline())
		{
			cancelTask();
			return;
		}
		if (plugin.isVerified(player))
		{
			cancelTask();
			return;
		}
		final Captcha captcha = plugin.getCaptchas().get(player.getName().toLowerCase());
		if (captcha == null)
		{
			cancelTask();
			return;
		}
		captcha.sendRequest(player);
	}

	public void startTask(long delay)
	{
		delay *= 20;
		if (taskID == -1)
			taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, delay, delay);
	}

	public void cancelTask()
	{
		if (taskID > -1)
		{
			Bukkit.getScheduler().cancelTask(taskID);
			taskID = -1;
		}
	}
}
