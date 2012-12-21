package de.st_ddt.crazycaptcha.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.st_ddt.crazycaptcha.CrazyCaptcha;

public class CrazyCaptchaPlayerListener_132 extends CrazyCaptchaPlayerListener
{

	public CrazyCaptchaPlayerListener_132(final CrazyCaptcha plugin)
	{
		super(plugin);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerChat(final AsyncPlayerChatEvent event)
	{
		if (!PlayerChat(event.getPlayer(), event.getMessage()))
			event.setCancelled(true);
	}
}
