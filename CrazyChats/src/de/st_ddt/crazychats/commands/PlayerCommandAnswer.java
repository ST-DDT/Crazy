package de.st_ddt.crazychats.commands;

import org.bukkit.entity.Player;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.channels.PrivateChannel;
import de.st_ddt.crazychats.data.ChatPlayerData;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.source.Localized;

public class PlayerCommandAnswer extends PlayerCommandExecutor
{

	public PlayerCommandAnswer(final CrazyChats plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYCHATS.CHANNEL.CHANGED $Channel$")
	public void command(final Player player, final String[] args) throws CrazyException
	{
		final ChatPlayerData data = plugin.getPlayerData(player);
		final PrivateChannel channel = data.getPrivateChannel();
		if (data.getCurrentChannel() != channel)
		{
			data.setCurrentChannel(channel);
			plugin.sendLocaleMessage("CHANNEL.CHANGED", player, channel.getName());
		}
		final Player target = plugin.getLastPrivateChatSender(player);
		if (target == null)
			throw new CrazyCommandCircumstanceException("if you have recieved at least one private message.");
		channel.getTargets(null).clear();
		channel.getTargets(null).add(target);
		player.chat(ChatHelper.listingString(" ", ChatHelperExtended.shiftArray(args, 1)));
	}
}
