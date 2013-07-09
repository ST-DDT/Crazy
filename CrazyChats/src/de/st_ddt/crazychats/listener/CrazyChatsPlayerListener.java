package de.st_ddt.crazychats.listener;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.channels.ChannelInterface;
import de.st_ddt.crazychats.channels.ControlledChannelInterface;
import de.st_ddt.crazychats.channels.CustomChannel;
import de.st_ddt.crazychats.channels.PrivateChannel;
import de.st_ddt.crazychats.channels.WorldChannel;
import de.st_ddt.crazychats.data.ChatPlayerData;
import de.st_ddt.crazyplugin.CrazyLightPluginInterface;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.CrazyChatsChatHelper;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.Permission;

public class CrazyChatsPlayerListener implements Listener
{

	protected final static Pattern PATTERN_SPACE = Pattern.compile(" ");
	protected final static Pattern PATTERN_COMMA = Pattern.compile(",");
	protected final static String CHANNELSIGNHEADER = ChatColor.RED + "[" + ChatColor.GREEN + "Channel" + ChatColor.RED + "]";
	protected final ChatResult CANCELLED = new ChatResult();
	protected final CrazyChats plugin;
	protected final Map<Player, String> lastPrivateChatSenders;

	public CrazyChatsPlayerListener(final CrazyChats plugin)
	{
		super();
		this.plugin = plugin;
		this.lastPrivateChatSenders = plugin.getLastPrivateChatSenders();
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

	private void PlayerLogin(final Player player)
	{
		ChatPlayerData data = plugin.getCrazyDatabase().updateEntry(player);
		if (data == null)
		{
			data = new ChatPlayerData(player.getName());
			plugin.getCrazyDatabase().save(data);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void PlayerJoinComplete(final PlayerJoinEvent event)
	{
		PlayerJoinComplete(event.getPlayer());
	}

	private void PlayerJoinComplete(final Player player)
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
		final Map<Integer, CustomChannel> customChannels = plugin.getCustomChannels();
		synchronized (customChannels)
		{
			for (final CustomChannel channel : customChannels.values())
				if (channel.join(player))
					channels.add(channel);
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
		if (event.getTo().getWorld() == null)
			return;
		final Player player = event.getPlayer();
		final ChatPlayerData data = plugin.getPlayerData(player);
		if (data == null)
			return;
		final ChannelInterface channel = data.getCurrentChannel();
		if (channel != null)
			if (channel instanceof WorldChannel)
				data.setCurrentChannelForced(plugin.getWorldChannels().get(event.getTo().getWorld().getName()));
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void PlayerKick(final PlayerKickEvent event)
	{
		PlayerQuit(event.getPlayer());
	}

	private void PlayerQuit(final Player player)
	{
		final ChatPlayerData data = plugin.getPlayerData(player);
		lastPrivateChatSenders.remove(player);
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
		if (data != null)
			plugin.getCrazyDatabase().save(data);
	}

	@EventHandler(ignoreCancelled = true)
	@Permission("crazychats.customchannel.admin")
	@Localized("CRAZYCHATS.SIGN.CREATED $ChannelID$ $ChannelName$")
	public void SignChangeEvent(final SignChangeEvent event)
	{
		final String[] lines = event.getLines();
		if (lines[0] == null || lines[1] == null)
			return;
		if (!lines[0].equals("[Channel]"))
			return;
		lines[0] = CHANNELSIGNHEADER;
		try
		{
			final int id = Integer.parseInt(lines[1]);
			final CustomChannel channel = plugin.getCustomChannels().get(id);
			if (channel == null)
			{
				lines[1] = ChatColor.RED + "INVALID";
				return;
			}
			final Player player = event.getPlayer();
			if (player.getName().equals(channel.getOwner()) || PermissionModule.hasPermission(player, "crazychats.customchannel.admin"))
				plugin.sendLocaleMessage("SIGN.CREATED", player, id, channel.getName());
			else
				lines[1] = ChatColor.RED + "INVALID";
		}
		catch (final NumberFormatException e)
		{
			lines[1] = ChatColor.RED + "INVALID";
		}
	}

	@EventHandler(ignoreCancelled = true)
	@Localized({ "CRAZYCHATS.SIGN.INVALID", "CRAZYCHATS.SIGN.JOINED $ChannelID$ $ChannelName$", "CRAZYCHATS.SIGN.JOINFAILED" })
	public void PlayerInteractEvent(final PlayerInteractEvent event)
	{
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		final BlockState block = event.getClickedBlock().getState();
		if (!(block instanceof Sign))
			return;
		final Sign sign = (Sign) block;
		if (!sign.getLine(0).equals(CHANNELSIGNHEADER))
			return;
		event.setCancelled(true);
		final Player player = event.getPlayer();
		try
		{
			final CustomChannel channel = plugin.getCustomChannels().get(Integer.parseInt(sign.getLine(1)));
			if (channel == null)
				plugin.sendLocaleMessage("SIGN.INVALID", player);
			else if (channel.participate(player, true))
				plugin.sendLocaleMessage("SIGN.JOINED", player, channel.getId(), channel.getName());
			else
				plugin.sendLocaleMessage("SIGN.JOINFAILED", player);
		}
		catch (final NumberFormatException e)
		{
			plugin.sendLocaleMessage("SIGN.INVALID", player);
		}
	}

	@Permission({ "crazychats.talk", "crazychats.serversilence.bypass", "crazychats.unmutable", "crazychats.chatspy", "crazychats.nocleaning", "crazychats.coloredchat" })
	@Localized({ "CRAZYCHATS.CHAT.BLOCKED.NOPERMISSION", "CRAZYCHATS.CHAT.BLOCKED.SILENCED $UntilDateTime$", "CRAZYCHATS.CHAT.BLOCKED.NOSUCHPLAYER $Player$", "CRAZYCHATS.CHANNEL.CHANGED $Channel$", "CRAZYCHATS.CHAT.BLOCKED.NOCHANNEL", "CRAZYCHATS.CHAT.BLOCKED.SERVERSILENCED" })
	protected ChatResult PlayerChat(final Player player, String message)
	{
		if (!PermissionModule.hasPermission(player, "crazychats.talk"))
		{
			plugin.sendLocaleMessage("CHAT.BLOCKED.NOPERMISSION", player);
			return CANCELLED;
		}
		final ChatPlayerData data = plugin.getPlayerData(player);
		if (data.isSilenced())
		{
			plugin.sendLocaleMessage("CHAT.BLOCKED.SILENCED", player, CrazyLightPluginInterface.DATETIMEFORMAT.format(data.getSilenced()));
			return CANCELLED;
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
					if (target == null || !player.canSee(target))
					{
						if (playerSplit.length == 1)
							plugin.sendLocaleMessage("CHAT.BLOCKED.NOCHANNEL", player, ChatHelper.listingString(data.getChannelMap().keySet()) + ", <Player,...>");
						else
							plugin.sendLocaleMessage("CHAT.BLOCKED.NOSUCHPLAYER", player, targetName);
						return CANCELLED;
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
				return CANCELLED;
			}
			else
				message = split[1];
		}
		if (channel == null)
		{
			plugin.sendLocaleMessage("CHAT.BLOCKED.NOCHANNEL", player, ChatHelper.listingString(data.getChannelMap().keySet()) + ", <Player,...>");
			return CANCELLED;
		}
		if (plugin.isServerSilenced())
			if (!PermissionModule.hasPermission(player, "crazychats.serversilence.bypass"))
				if (channel.isAffectedByServerSilence())
				{
					plugin.sendLocaleMessage("CHAT.BLOCKED.SERVERSILENCED", player);
					return CANCELLED;
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
		if (channel instanceof PrivateChannel)
		{
			targets.remove(player);
			final String senderName = player.getName();
			for (final Player target : targets)
				lastPrivateChatSenders.put(target, senderName);
		}
		targets.add(player);
		if (!(channel instanceof PrivateChannel) || plugin.isPrivateChatSpyingEnabled())
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
		if (player == null)
			return;
		final ChatPlayerData data = plugin.getPlayerData(player);
		if (data == null)
			return;
		final ChannelInterface channel = data.getCurrentChannel();
		if (channel == null)
			return;
		plugin.getCrazyLogger().log("Chat", "[" + channel.getName() + "] " + player.getName() + " >>> " + message);
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
		protected ChatResult()
		{
			super();
			this.cancelled = true;
			this.format = null;
			this.targets = null;
			this.message = null;
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
			return CrazyChatsChatHelper.applyFormat(plugin, player, format);
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
