package de.st_ddt.crazychats.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.channels.BroadcastChannel;
import de.st_ddt.crazychats.data.ChatPlayerData;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.paramitrisable.PlayerParamitrisable;

public class CrazyChatsCommandSay extends CrazyChatsCommandExecutor
{

	public CrazyChatsCommandSay(final CrazyChats plugin)
	{
		super(plugin);
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length < 1)
			throw new CrazyCommandUsageException("<Message>");
		if (sender instanceof Player)
			command((Player) sender, args);
		else
		{
			final String message = ChatHelper.colorise(String.format(plugin.getBroadcastChatFormat(), sender.getName(), ChatHelper.listingString(" ", args)));
			for (final Player target : Bukkit.getOnlinePlayers())
				target.sendMessage(message);
			sender.sendMessage(message);
			plugin.getCrazyLogger().log("Chat", "[Broadcast] CONSOLE >>> " + message);
		}
	}

	@Localized("CRAZYCHATS.CHANNEL.CHANGED $Channel$")
	public void command(final Player player, final String[] args) throws CrazyException
	{
		final BroadcastChannel channel = plugin.getBroadcastChannel();
		if (!channel.hasTalkPermission(player))
			throw new CrazyCommandPermissionException();
		final ChatPlayerData data = plugin.getPlayerData(player);
		if (data.getCurrentChannel() != channel)
		{
			data.setCurrentChannel(channel);
			plugin.sendLocaleMessage("CHANNEL.CHANGED", player, channel.getName());
		}
		player.chat(ChatHelper.listingString(" ", ChatHelperExtended.shiftArray(args, 1)));
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		return PlayerParamitrisable.tabHelp(args[args.length - 1]);
	}
}
