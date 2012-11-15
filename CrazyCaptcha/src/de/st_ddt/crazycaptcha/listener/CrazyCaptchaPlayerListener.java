package de.st_ddt.crazycaptcha.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

import de.st_ddt.crazycaptcha.CrazyCaptcha;
import de.st_ddt.crazycaptcha.tasks.ScheduledKickTask;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazyCaptchaPlayerListener implements Listener
{

	protected final CrazyCaptcha plugin;

	public CrazyCaptchaPlayerListener(final CrazyCaptcha plugin)
	{
		super();
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	@Localized("CRAZYCAPTCHA.KICKED.BANNED.UNTIL $BannedUntil$")
	public void PlayerLoginBanCheck(final PlayerLoginEvent event)
	{
		final Player player = event.getPlayer();
		if (plugin.isTempBanned(event.getAddress().getHostAddress()))
		{
			event.setResult(Result.KICK_OTHER);
			event.setKickMessage(plugin.getLocale().getLocaleMessage(player, "KICKED.BANNED.UNTIL", plugin.getTempBannedString(event.getAddress().getHostAddress())));
			plugin.getCrazyLogger().log("AccessDenied", "Denied access for player " + player.getName() + " @ " + event.getAddress().getHostAddress() + " because of a temporary ban!");
			return;
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void PlayerJoin(final PlayerJoinEvent event)
	{
		PlayerJoin(event.getPlayer());
	}

	@Localized("CRAZYCAPTCHA.KICKED.NOTVERIFIED")
	public void PlayerJoin(final Player player)
	{
		plugin.getCrazyLogger().log("Join", player.getName() + " @ " + player.getAddress().getAddress().getHostAddress() + " joined the server");
		if (plugin.playerJoin(player))
			return;
		final int autoKick = plugin.getAutoKick();
		if (autoKick >= 10)
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new ScheduledKickTask(player, plugin.getLocale().getLanguageEntry("KICKED.NOTVERIFIED"), plugin.getAutoTempBan()), autoKick * 20);
		plugin.requestVerification(player);
	}

	@EventHandler
	public void PlayerQuit(final PlayerQuitEvent event)
	{
		PlayerQuit(event.getPlayer());
	}

	public void PlayerQuit(final Player player)
	{
		plugin.getCrazyLogger().log("Quit", player.getName() + " @ " + player.getAddress().getAddress().getHostAddress() + " left the server");
		plugin.playerQuit(player);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	@Localized({ "CRAZYCAPTCHA.KICKED.COMMANDUSAGE" })
	public void PlayerPreCommand(final PlayerCommandPreprocessEvent event)
	{
		final Player player = event.getPlayer();
		if (plugin.isVerified(player))
			return;
		final String message = event.getMessage().toLowerCase();
		if (message.startsWith("/"))
		{
			for (final String command : plugin.getCommandWhiteList())
				if (message.matches(command))
					return;
			event.setCancelled(true);
			final String IP = player.getAddress().getAddress().getHostAddress();
			if (plugin.isAutoKickCommandUsers())
			{
				player.kickPlayer(plugin.getLocale().getLocaleMessage(player, "KICKED.COMMANDUSAGE"));
				plugin.getCrazyLogger().log("CommandBlocked", player.getName() + " @ " + IP + " has been kicked for trying to execute", event.getMessage());
			}
			else
			{
				plugin.requestVerification(player);
				plugin.getCrazyLogger().log("CommandBlocked", player.getName() + " @ " + IP + " tried to execute", event.getMessage());
			}
		}
	}
}
