package de.st_ddt.crazychats.commands;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.data.ChatPlayerData;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class CrazyChatsCommandPlayerListName extends CrazyChatsCommandExecutor
{

	public CrazyChatsCommandPlayerListName(final CrazyChats plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYCHATS.COMMAND.PLAYER.LISTNAME.WARNLENGTH $ListName$ $Length$", "CRAZYCHATS.COMMAND.PLAYER.LISTNAME.DONE $Player$ $ListName$", "CRAZYCHATS.COMMAND.PLAYER.LISTNAME.REMOVED $Player$" })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length < 1 || args.length > 2)
			throw new CrazyCommandUsageException("<Player> [ListName]");
		final String name = args[0];
		final ChatPlayerData data = plugin.getPlayerData(name);
		if (data == null)
			throw new CrazyCommandNoSuchException("Player", name);
		if (!PermissionModule.hasPermission(sender, "crazychats.player.listname." + (data.getPlayer().equals(sender) ? "self" : "other")))
			throw new CrazyCommandPermissionException();
		if (args.length == 2)
		{
			String listName = ChatHelper.colorise(args[1]);
			if (listName.length() < 3 || listName.length() > 16)
				plugin.sendLocaleMessage("COMMAND.PLAYER.LISTNAME.WARNLENGTH", sender, listName, listName.length());
			if (listName.length() > 16)
				listName = listName.substring(0, 16);
			data.setListName(listName);
			final Player player = data.getPlayer();
			if (player != null)
				player.setPlayerListName(listName);
			plugin.sendLocaleMessage("COMMAND.PLAYER.LISTNAME.DONE", sender, data.getName(), listName);
		}
		else
		{
			data.setListName(null);
			final Player player = data.getPlayer();
			if (player != null)
				player.setPlayerListName(null);
			plugin.sendLocaleMessage("COMMAND.PLAYER.LISTNAME.REMOVED", sender, data.getName());
		}
		plugin.getCrazyDatabase().save(data);
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		if (args.length != -1)
			return null;
		final String arg = args[0].toLowerCase();
		final List<String> res = new LinkedList<String>();
		for (final Player player : Bukkit.getOnlinePlayers())
			if (player.getName().toLowerCase().startsWith(arg))
				res.add(arg);
		return res;
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazychats.player.listname.self") || PermissionModule.hasPermission(sender, "crazychats.player.listname.other");
	}
}
