package de.st_ddt.crazychats.commands;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.channels.ChannelInterface;
import de.st_ddt.crazychats.channels.MuteableChannelInterface;
import de.st_ddt.crazychats.data.ChatPlayerData;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.source.Localized;

public class PlayerCommandMuteChannel extends PlayerCommandExecutor
{

	public PlayerCommandMuteChannel(final CrazyChats plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYCHATS.COMMAND.MUTEDCHANNEL $Name$")
	public void command(final Player player, final String[] args) throws CrazyException
	{
		if (args.length == 0)
			throw new CrazyCommandUsageException("<Channel...>");
		final ChatPlayerData data = plugin.getPlayerData(player);
		if (args.length == 1)
			if (args[0].equals("*"))
			{
				final Set<ChannelInterface> channels = data.getAccessibleChannels();
				synchronized (channels)
				{
					for (final ChannelInterface channel : channels)
						if (channel instanceof MuteableChannelInterface)
							((MuteableChannelInterface) channel).muteChannel(player);
				}
				plugin.sendLocaleMessage("COMMAND.MUTEDCHANNEL", player, "*ALL*");
				return;
			}
		final Map<String, ChannelInterface> channels = data.getChannelMap();
		for (final String arg : args)
		{
			final ChannelInterface channel = channels.get(arg.toLowerCase());
			if (channel instanceof MuteableChannelInterface)
			{
				((MuteableChannelInterface) channel).muteChannel(player);
				plugin.sendLocaleMessage("COMMAND.MUTEDCHANNEL", player, channel.getName());
			}
			else
				throw new CrazyCommandNoSuchException("Channel", arg, channels.keySet());
		}
	}

	@Override
	public List<String> tab(final Player player, final String[] args)
	{
		final String last = args[args.length - 1].toLowerCase();
		final List<String> res = new LinkedList<String>();
		for (final String channel : plugin.getPlayerData(player).getChannelMap().keySet())
			if (channel.toLowerCase().startsWith(last))
				res.add(channel);
		return res;
	}
}
