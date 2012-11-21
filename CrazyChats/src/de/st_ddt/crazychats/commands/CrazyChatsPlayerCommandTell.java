package de.st_ddt.crazychats.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.channels.PrivateChannel;
import de.st_ddt.crazychats.data.ChatPlayerData;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.paramitrisable.PlayerParamitrisable;

public class CrazyChatsPlayerCommandTell extends CrazyChatsPlayerCommandExecutor
{

	public CrazyChatsPlayerCommandTell(final CrazyChats plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYCHATS.CHANNEL.CHANGED $Channel$")
	public void command(final Player player, final String[] args) throws CrazyException
	{
		if (args.length < 2)
			throw new CrazyCommandUsageException("<Player> <Message>");
		final ChatPlayerData data = plugin.getPlayerData(player);
		final PrivateChannel channel = data.getPrivateChannel();
		if (data.getCurrentChannel() != channel)
		{
			data.setCurrentChannel(channel);
			plugin.sendLocaleMessage("CHANNEL.CHANGED", player, channel.getName());
		}
		final Player target = Bukkit.getPlayer(args[0]);
		if (target == null)
			throw new CrazyCommandNoSuchException("Player", args[0]);
		channel.getTargets(null).clear();
		channel.getTargets(null).add(target);
		player.chat(ChatHelper.listingString(" ", ChatHelperExtended.shiftArray(args, 1)));
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		return PlayerParamitrisable.tabHelp(args[args.length - 1]);
	}
}
