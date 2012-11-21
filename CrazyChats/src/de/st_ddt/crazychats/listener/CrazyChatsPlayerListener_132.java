package de.st_ddt.crazychats.listener;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.st_ddt.crazychats.CrazyChats;

public class CrazyChatsPlayerListener_132 extends CrazyChatsPlayerListener
{

	public CrazyChatsPlayerListener_132(final CrazyChats plugin)
	{
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void PlayerChat(final AsyncPlayerChatEvent event)
	{
		final ChatResult result = PlayerChat(event.getPlayer(), event.getMessage());
		if (result.isCancelled())
			event.setCancelled(true);
		else
		{
			event.setFormat(result.getFormat());
			event.setMessage(result.getMessage());
			final Set<Player> targets = event.getRecipients();
			try
			{
				targets.clear();
				targets.addAll(result.getTargets());
			}
			catch (final UnsupportedOperationException e)
			{}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void PlayerChatLog(final AsyncPlayerChatEvent event)
	{
		PlayerChatLog(event.getPlayer(), event.getMessage());
	}
}
