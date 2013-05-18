package de.st_ddt.crazychats.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.channels.ChannelInterface;
import de.st_ddt.crazychats.data.ChatPlayerData;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.paramitrisable.MapParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class PlayerCommandChatChannel extends PlayerCommandExecutor
{

	public PlayerCommandChatChannel(final CrazyChats plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYCHATS.CHANNEL.CURRENT $Channel$", "CRAZYCHATS.COMMAND.CHANNELS.LIST.HEADER $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$", "CRAZYCHATS.COMMAND.CHANNELS.LIST.LISTFORMAT $Index$ $Entry$ $ChatHeader$", "CRAZYCHATS.CHANNEL.CHANGED $Channel$" })
	public void command(final Player player, final String[] args) throws CrazyException
	{
		if (args.length > 1)
			throw new CrazyCommandUsageException("[Channel]");
		final ChatPlayerData data = plugin.getPlayerData(player);
		final Map<String, ChannelInterface> channels = data.getChannelMap();
		if (args.length == 0)
		{
			if (data.getCurrentChannel() == null)
				plugin.sendLocaleMessage("CHANNEL.CURRENT", player, "NONE");
			else
				plugin.sendLocaleMessage("CHANNEL.CURRENT", player, data.getCurrentChannel().getName());
			plugin.sendLocaleList(player, "COMMAND.CHANNELS.LIST.HEADER", "COMMAND.CHANNELS.LIST.LISTFORMAT", null, -1, -1, new ArrayList<String>(channels.keySet()));
		}
		else
		{
			final ChannelInterface channel = channels.get(args[0].toLowerCase());
			if (channel == null)
				throw new CrazyCommandNoSuchException("Channel", args[0], channels.keySet());
			data.setCurrentChannel(channel);
			plugin.sendLocaleMessage("CHANNEL.CHANGED", player, channel.getName());
		}
	}

	@Override
	public List<String> tab(final Player player, final String[] args)
	{
		if (args.length != 1)
			return null;
		return MapParamitrisable.tabHelp(plugin.getPlayerData(player).getChannelMap(), args[0].toLowerCase());
	}
}
