package de.st_ddt.crazychats.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.channels.PrivateChannel;
import de.st_ddt.crazychats.data.ChatPlayerData;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.paramitrisable.OfflinePlayerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.VisiblePlayerParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class CrazyChatsPlayerCommandChatTo extends CrazyChatsPlayerCommandExecutor
{

	public CrazyChatsPlayerCommandChatTo(final CrazyChats plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYCHATS.COMMAND.CHANNEL.PRIVATE.TARGET.LIST.HEADER $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$", "CRAZYCHATS.COMMAND.CHANNEL.PRIVATE.TARGET.LIST.LISTFORMAT $Index$ $Entry$ $ChatHeader$", "CRAZYCHATS.CHANNEL.CHANGED $Channel$", "CRAZYCHATS.COMMAND.CHANNEL.PRIVATE.TARGET.SET $Targets$" })
	public void command(final Player player, final String[] args) throws CrazyException
	{
		final ChatPlayerData data = plugin.getPlayerData(player);
		final PrivateChannel channel = data.getPrivateChannel();
		final Set<Player> targets = channel.getTargets(null);
		if (args.length == 0)
		{
			final List<String> targetList = new ArrayList<String>();
			synchronized (targets)
			{
				for (final Player target : targets)
					targetList.add(target.getName());
			}
			Collections.sort(targetList);
			plugin.sendLocaleList(player, "COMMAND.CHANNEL.PRIVATE.TARGET.LIST.HEADER", "COMMAND.CHANNEL.PRIVATE.TARGET.LIST.LISTFORMAT", null, -1, -1, targetList);
		}
		else
		{
			final List<Player> targetList = new ArrayList<Player>();
			for (final String arg : args)
			{
				final Player target = Bukkit.getPlayerExact(arg);
				if (target == null || !player.canSee(target))
					throw new CrazyCommandNoSuchException("Player", arg);
				targetList.add(target);
			}
			if (data.getCurrentChannel() != channel)
			{
				data.setCurrentChannel(channel);
				plugin.sendLocaleMessage("CHANNEL.CHANGED", player, channel.getName());
			}
			targets.clear();
			targets.addAll(targetList);
			plugin.sendLocaleMessage("COMMAND.CHANNEL.PRIVATE.TARGET.SET", player, ChatHelper.listingString(OfflinePlayerParamitrisable.getPlayerNames(targetList)));
		}
	}

	@Override
	public List<String> tab(final Player player, final String[] args)
	{
		return VisiblePlayerParamitrisable.tabHelp(player, args[args.length - 1]);
	}
}
