package de.st_ddt.crazycaptcha.tasks;

import org.bukkit.entity.Player;

import de.st_ddt.crazycaptcha.CrazyCaptcha;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public class ScheduledKickTask implements Runnable
{

	protected final Player player;
	protected final CrazyLocale locale;
	protected final long tempBan;

	public ScheduledKickTask(final Player player, final CrazyLocale message)
	{
		super();
		this.player = player;
		this.locale = message;
		this.tempBan = 0;
	}

	public ScheduledKickTask(final Player player, final CrazyLocale message, final long tempBan)
	{
		super();
		this.player = player;
		this.locale = message;
		this.tempBan = tempBan;
	}

	@Override
	public void run()
	{
		final CrazyCaptcha plugin = CrazyCaptcha.getPlugin();
		if (!player.isOnline())
			return;
		if (!plugin.isVerified(player))
			player.kickPlayer(locale.getLanguageText(player));
		if (tempBan > 0)
			plugin.setTempBanned(player, tempBan);
	}
}
