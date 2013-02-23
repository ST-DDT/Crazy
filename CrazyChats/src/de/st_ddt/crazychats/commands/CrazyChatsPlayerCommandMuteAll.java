package de.st_ddt.crazychats.commands;

import java.util.Set;

import org.bukkit.entity.Player;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.channels.ChannelInterface;
import de.st_ddt.crazychats.channels.MuteableChannelInterface;
import de.st_ddt.crazychats.data.ChatPlayerData;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.source.Localized;

public class CrazyChatsPlayerCommandMuteAll extends CrazyChatsPlayerCommandExecutor
{

	public CrazyChatsPlayerCommandMuteAll(final CrazyChats plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYCHATS.COMMAND.MUTEDCHANNEL $Name$")
	public void command(final Player player, final String[] args) throws CrazyException
	{
		final ChatPlayerData data = plugin.getPlayerData(player);
		final Set<ChannelInterface> channels = data.getAccessibleChannels();
		synchronized (channels)
		{
			for (final ChannelInterface channel : channels)
				if (channel instanceof MuteableChannelInterface)
					((MuteableChannelInterface) channel).muteChannel(player);
		}
		plugin.sendLocaleMessage("COMMAND.MUTEDCHANNEL", player, "*ALL*");
	}
}
