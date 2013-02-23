package de.st_ddt.crazypromoter.commands;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazypromoter.CrazyPromoter;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.source.Localized;

public class CrazyPromoterCommandCheck extends CrazyPromoterCommandExecutor
{

	public CrazyPromoterCommandCheck(final CrazyPromoter plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYPROMOTER.COMMAND.CHECK.FAIL $Name$")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length > 2)
			throw new CrazyCommandUsageException("[Player]");
		Player player = null;
		if (args.length == 0)
			if (sender instanceof Player)
				player = (Player) sender;
			else
				throw new CrazyCommandUsageException("<Player>");
		else
			player = Bukkit.getPlayer(args[0]);
		if (player == null)
			throw new CrazyCommandNoSuchException("Player", args[0]);
		if (!sender.hasPermission(sender == player ? "crazypromoter.check.self" : "crazypromoter.check.other"))
			throw new CrazyCommandPermissionException();
		if (!plugin.checkStatus(player))
			plugin.sendLocaleMessage("COMMAND.CHECK.FAIL", sender, player.getName());
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		if (args.length != 1)
			return null;
		final List<String> res = new LinkedList<String>();
		final String arg = args[0].toLowerCase();
		for (final Player player : Bukkit.getOnlinePlayers())
			if (player.getName().toLowerCase().startsWith(arg))
				res.add(player.getName());
		return res;
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazypromoter.check.self") || PermissionModule.hasPermission(sender, "crazypromoter.check.other");
	}
}
