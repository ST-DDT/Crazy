package de.st_ddt.crazychats.listener;

import java.util.Collection;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.data.ChatPlayerData;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.ChatHelperExtended;

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
		final Player player = event.getPlayer();
		final String message = event.getMessage();
		PlayerChatLog(player, message);
		try
		{
			if (event.getRecipients().remove(player))
				PlayerChatOwnerMessage(event.getFormat(), player, message);
		}
		catch (final UnsupportedOperationException e)
		{}
	}

	@EventHandler
	public void PlayerChatTab(final PlayerChatTabCompleteEvent event)
	{
		final String message = event.getChatMessage();
		if (!message.startsWith("@"))
			return;
		if (message.contains(" "))
			return;
		final ChatPlayerData data = plugin.getPlayerData(event.getPlayer());
		if (data == null)
			return;
		final Collection<String> completions = event.getTabCompletions();
		if (message.equals("@"))
		{
			for (final Player player : Bukkit.getOnlinePlayers())
				completions.add("@" + player.getName());
			for (final String channel : data.getChannelMap().keySet())
				completions.add("@" + channel);
			return;
		}
		if (message.endsWith(","))
		{
			for (final Player player : Bukkit.getOnlinePlayers())
				completions.add(message + player.getName());
			return;
		}
		final String[] split = PATTERN_COMMA.split(message);
		if (split.length == 1)
		{
			final String part = message.substring(1).toLowerCase();
			for (final Player player : Bukkit.getOnlinePlayers())
				if (player.getName().toLowerCase().startsWith(part))
					completions.add("@" + player.getName());
			for (final String channel : data.getChannelMap().keySet())
				if (channel.toLowerCase().startsWith(part))
					completions.add("@" + channel);
		}
		else
		{
			final String prefix = ChatHelper.listingString(",", ChatHelperExtended.cutArray(split, split.length - 1)) + ",";
			final String part = split[split.length - 1].toLowerCase();
			for (final Player player : Bukkit.getOnlinePlayers())
				if (player.getName().toLowerCase().startsWith(part))
					completions.add(prefix + player.getName());
		}
	}
}
