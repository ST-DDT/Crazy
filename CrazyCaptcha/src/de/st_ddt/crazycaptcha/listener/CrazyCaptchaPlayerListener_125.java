package de.st_ddt.crazycaptcha.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChatEvent;

import de.st_ddt.crazycaptcha.CrazyCaptcha;

@SuppressWarnings("deprecation")
public class CrazyCaptchaPlayerListener_125 extends CrazyCaptchaPlayerListener
{

	public CrazyCaptchaPlayerListener_125(final CrazyCaptcha plugin)
	{
		super(plugin);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerChat(final PlayerChatEvent event)
	{
		if (!PlayerChat(event.getPlayer(), event.getMessage()))
			event.setCancelled(true);
	}
}
