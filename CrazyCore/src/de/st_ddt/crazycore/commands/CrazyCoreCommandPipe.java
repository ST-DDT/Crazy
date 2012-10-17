package de.st_ddt.crazycore.commands;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.CrazyPipe;

public class CrazyCoreCommandPipe extends CrazyCoreCommandExecutor
{

	public CrazyCoreCommandPipe(CrazyCore plugin)
	{
		super(plugin);
	}

	@Override
	public void command(CommandSender sender, String[] args) throws CrazyException
	{
		if (args.length == 0)
			throw new CrazyCommandUsageException("<$PresetList> [> PipeCommand]", "<Arg1>, [Arg2], ... [> PipeCommand]");
		String[] pipeArgs = new String[] { "show", "$0$" };
		if (args[0].startsWith("$"))
		{
			List<String> datas = new LinkedList<String>();
			String value = args[0].toLowerCase().substring(1);
			if (value.equals("ops"))
				for (OfflinePlayer player : Bukkit.getOperators())
					datas.add(player.getName());
			else if (value.equals("onlines"))
				for (Player player : Bukkit.getOnlinePlayers())
					datas.add(player.getName());
			else if (value.equals("offlines"))
				for (OfflinePlayer player : Bukkit.getOfflinePlayers())
				{
					if (!player.isOnline())
						datas.add(player.getName());
				}
			else if (value.equals("banned"))
				for (OfflinePlayer player : Bukkit.getBannedPlayers())
					datas.add(player.getName());
			else if (value.equals("banned-ips"))
				for (String ip : Bukkit.getIPBans())
					datas.add(ip);
			else
				throw new CrazyCommandNoSuchException("PresetList", args[0], "ops", "onlines", "offlines", "banned", "banned-ips");
			if (args.length > 1)
				if (args[1].equals(">"))
					pipeArgs = ChatHelperExtended.shiftArray(args, 2);
				else
					throw new CrazyCommandUsageException("<$PresetList> [> PipeCommand]");
			for (String data : datas)
				CrazyPipe.pipe(sender, data, pipeArgs);
			return;
		}
		for (int i = 0; i < args.length; i++)
			if (args[i].equals(">"))
			{
				pipeArgs = ChatHelperExtended.shiftArray(args, i + 1);
				args = ChatHelperExtended.cutArray(args, i);
				break;
			}
		String arg = ChatHelper.listingString(" ", args);
		for (String data : arg.split(","))
			CrazyPipe.pipe(sender, data.trim(), pipeArgs);
	}
}
