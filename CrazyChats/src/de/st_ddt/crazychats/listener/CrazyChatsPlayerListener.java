package de.st_ddt.crazychats.listener;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.channels.ChannelInterface;
import de.st_ddt.crazychats.channels.ControlledChannelInterface;
import de.st_ddt.crazychats.channels.PrivateChannel;
import de.st_ddt.crazychats.channels.WorldChannel;
import de.st_ddt.crazychats.data.ChatPlayerData;
import de.st_ddt.crazyplugin.CrazyLightPluginInterface;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.CrazyChatsChatHelper;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class CrazyChatsPlayerListener implements Listener
{

	protected final CrazyChats plugin;
	protected final Pattern PATTERN_SPACE = Pattern.compile(" ");
	protected final Pattern PATTERN_COMMA = Pattern.compile(",");

	public CrazyChatsPlayerListener(final CrazyChats plugin)
	{
		super();
		this.plugin = plugin;
	}

	public void PlayerJoinEnabled(final Player player)
	{
		PlayerLogin(player);
		PlayerJoinComplete(player);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void PlayerLogin(final PlayerLoginEvent event)
	{
		PlayerLogin(event.getPlayer());
	}

	public void PlayerLogin(final Player player)
	{
		ChatPlayerData data = plugin.getCrazyDatabase().updateEntry(player);
		if (data == null)
		{
			data = new ChatPlayerData(player.getName());
			plugin.getCrazyDatabase().save(data);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void PlayerJoinComplete(final PlayerJoinEvent event)
	{
		PlayerJoinComplete(event.getPlayer());
	}

	public void PlayerJoinComplete(final Player player)
	{
		final ChatPlayerData data = plugin.getPlayerData(player);
		final Set<ChannelInterface> channels = data.getAccessibleChannels();
		channels.add(plugin.getBroadcastChannel());
		channels.add(plugin.getGlobalChannel());
		channels.addAll(plugin.getWorldChannels().values());
		channels.add(plugin.getLocalChannel());
		final PrivateChannel privateChannel = new PrivateChannel(player);
		data.setPrivateChannel(privateChannel);
		channels.add(plugin.getAdminChannel());
		plugin.getControlledChannels().add(privateChannel);
		if (data.getDisplayName() != null)
			player.setDisplayName(data.getDisplayName());
		if (data.getListName() != null)
			player.setPlayerListName(data.getListName());
		else
		{
			final String name = plugin.getGroupListnamePrefix(player) + player.getName();
			if (name.length() > 16)
				player.setPlayerListName(name.substring(0, 16));
			else
				player.setPlayerListName(name);
		}
		final ChannelInterface channel = data.getChannelMap().get(plugin.getDefaultChannelKey());
		if (channel != null)
			data.setCurrentChannelForced(channel);
		else
			data.setCurrentChannel(privateChannel);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void PlayerQuit(final PlayerQuitEvent event)
	{
		PlayerQuit(event.getPlayer());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void PlayerTeleport(final PlayerTeleportEvent event)
	{
		final Player player = event.getPlayer();
		final ChatPlayerData data = plugin.getPlayerData(player);
		if (data.getCurrentChannel() != null)
			if (data.getCurrentChannel() instanceof WorldChannel)
				data.setCurrentChannelForced(plugin.getWorldChannels().get(event.getTo().getWorld().getName()));
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void PlayerKick(final PlayerKickEvent event)
	{
		PlayerQuit(event.getPlayer());
	}

	protected void PlayerQuit(final Player player)
	{
		final ChatPlayerData data = plugin.getPlayerData(player);
		final Set<ControlledChannelInterface> controlledChannels = plugin.getControlledChannels();
		if (data != null)
		{
			data.quit();
			controlledChannels.remove(data.getPrivateChannel());
		}
		synchronized (controlledChannels)
		{
			for (final ControlledChannelInterface channel : controlledChannels)
				channel.kick(player);
		}
		plugin.getGlobalChannel().unmuteChannel(player);
		final Collection<WorldChannel> worldChannels = plugin.getWorldChannels().values();
		synchronized (worldChannels)
		{
			for (final WorldChannel channel : worldChannels)
				channel.unmuteChannel(player);
		}
		plugin.getLocalChannel().unmuteChannel(player);
		plugin.getCrazyDatabase().save(data);
	}

	@Localized({ "CRAZYCHATS.CHAT.BLOCKED.NOPERMISSION", "CRAZYCHATS.CHAT.BLOCKED.SILENCED $UntilDateTime$", "CRAZYCHATS.CHAT.BLOCKED.NOSUCHPLAYER $Player$", "CRAZYCHATS.CHANNEL.CHANGED $Channel$", "CRAZYCHATS.CHAT.BLOCKED.NOCHANNEL" })
	protected ChatResult PlayerChat(final Player player, String message)
	{
		if (!PermissionModule.hasPermission(player, "crazychats.talk"))
		{
			plugin.sendLocaleMessage("CHAT.BLOCKED.NOPERMISSION", player);
			return new ChatResult();
		}
		final ChatPlayerData data = plugin.getPlayerData(player);
		if (data.isSilenced())
		{
			plugin.sendLocaleMessage("CHAT.BLOCKED.SILENCED", player, CrazyLightPluginInterface.DATETIMEFORMAT.format(data.getSilenced()));
			return new ChatResult();
		}
		ChannelInterface channel = data.getCurrentChannel();
		if (message.startsWith("@"))
		{
			if (message.length() == 1)
				plugin.sendLocaleMessage("CHAT.BLOCKED.NOCHANNEL", player, ChatHelper.listingString(data.getChannelMap().keySet()) + ", <Player,...>");
			final String[] split = PATTERN_SPACE.split(message, 2);
			final String channelName = split[0].substring(1).toLowerCase();
			if (channelName.equals("w") || channelName.equals("world"))
				channel = plugin.getWorldChannels().get(player.getWorld().getName());
			else
				channel = data.getChannelMap().get(channelName);
			if (channel == null)
			{
				final HashSet<Player> targets = new HashSet<Player>();
				final String[] playerSplit = PATTERN_COMMA.split(channelName);
				for (final String targetName : playerSplit)
				{
					final Player target = Bukkit.getPlayerExact(targetName);
					if (target == null)
					{
						if (playerSplit.length == 1)
							plugin.sendLocaleMessage("CHAT.BLOCKED.NOCHANNEL", player, ChatHelper.listingString(data.getChannelMap().keySet()) + ", <Player,...>");
						else
							plugin.sendLocaleMessage("CHAT.BLOCKED.NOSUCHPLAYER", player, targetName);
						return new ChatResult();
					}
					targets.add(target);
				}
				channel = data.getPrivateChannel();
				channel.getTargets(null).clear();
				channel.getTargets(null).addAll(targets);
				targets.clear();
			}
			if (split.length == 1)
			{
				data.setCurrentChannel(channel);
				plugin.sendLocaleMessage("CHANNEL.CHANGED", player, channel.getName());
				return new ChatResult();
			}
			else
				message = split[1];
		}
		if (channel == null)
		{
			plugin.sendLocaleMessage("CHAT.BLOCKED.NOCHANNEL", player, ChatHelper.listingString(data.getChannelMap().keySet()) + ", <Player,...>");
			return new ChatResult();
		}
		data.setCurrentChannel(channel);
		final Set<Player> targets = new HashSet<Player>();
		final Set<Player> channelTargets = channel.getTargets(player);
		synchronized (channelTargets)
		{
			for (final Player target : channelTargets)
				if (!plugin.getAvailablePlayerData(target).isMuted(player) || PermissionModule.hasPermission(player, "crazychats.unmutable"))
					targets.add(target);
		}
		targets.add(player);
		for (final Player online : Bukkit.getOnlinePlayers())
			if (PermissionModule.hasPermission(online, "crazychats.chatspy"))
				if (!plugin.getAvailablePlayerData(online).isMuted(player) || PermissionModule.hasPermission(player, "crazychats.unmutable"))
					targets.add(online);
		if (plugin.isCleaningRepetitionsEnabled() && !PermissionModule.hasPermission(player, "crazychats.nocleaning"))
			message = CrazyChatsChatHelper.cleanRepetitions(message);
		if (plugin.isCleaningCapsEnabled() && !PermissionModule.hasPermission(player, "crazychats.nocleaning"))
			message = CrazyChatsChatHelper.cleanCaps(message);
		if (PermissionModule.hasPermission(player, "crazychats.coloredchat"))
			message = ChatHelper.colorise(message);
		return new ChatResult(channel.getFormat(player), targets, message);
	}

	protected void PlayerChatOwnerMessage(final String format, final Player player, final String message)
	{
		player.sendMessage(String.format(format, plugin.getOwnChatNamePrefix() + player.getDisplayName(), message));
	}

	protected void PlayerChatLog(final Player player, final String message)
	{
		plugin.getCrazyLogger().log("Chat", "[" + plugin.getPlayerData(player).getCurrentChannel().getName() + "] " + player.getName() + " >>> " + message);
	}

	protected class ChatResult
	{

		private final boolean cancelled;
		private final String format;
		private final Set<Player> targets;
		private final String message;

		/**
		 * Canceled Chat
		 */
		public ChatResult()
		{
			super();
			this.cancelled = true;
			format = null;
			targets = null;
			message = null;
		}

		/**
		 * Accepted Chat
		 */
		public ChatResult(final String format, final Set<Player> targets, final String message)
		{
			super();
			this.cancelled = false;
			this.format = format;
			this.targets = targets;
			this.message = message;
		}

		public boolean isCancelled()
		{
			return cancelled;
		}

		public String getFormat()
		{
			return format;
		}

		public String getAdvancedFormat(final Player player)
		{
			return ChatHelper.putArgs(format, "", "", plugin.getGroupPrefix(player), plugin.getGroupSuffix(player), player.getWorld().getName());
		}

		public Set<Player> getTargets()
		{
			return targets;
		}

		public String getMessage()
		{
			return message;
		}
	}
}
