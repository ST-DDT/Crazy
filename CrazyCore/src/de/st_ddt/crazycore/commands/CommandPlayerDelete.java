package de.st_ddt.crazycore.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.events.CrazyPlayerRemoveEvent;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.OfflinePlayerParamitrisable;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.Permission;

public class CommandPlayerDelete extends CommandExecutor
{

	public CommandPlayerDelete(final CrazyCore plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYCORE.COMMAND.PLAYER.DELETE.SUCCESS $Name$ $Amount$", "CRAZYCORE.COMMAND.PLAYER.DELETE.LISTHEADER $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$", "CRAZYCORE.COMMAND.PLAYER.DELETE.LISTFORMAT $Index$ $Entry$ $ChatHeader$" })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		String name = ChatHelper.listingString(" ", args);
		plugin.checkProtectedPlayer(name, sender, "crazycore.player.delete.protected", plugin.getName(), "deleting all player data");
		final Player player = Bukkit.getPlayer(name);
		if (player != null)
			name = player.getName();
		final boolean self = sender.getName().equalsIgnoreCase(name);
		if (!PermissionModule.hasPermission(sender, "crazycore.player.delete." + (self ? "self" : "other")))
			throw new CrazyCommandPermissionException();
		final CrazyPlayerRemoveEvent event = new CrazyPlayerRemoveEvent(name);
		if (self)
			event.callEvent();
		else
			event.checkAndCallEvent();
		plugin.sendLocaleMessage("COMMAND.PLAYER.DELETE.SUCCESS", sender, name, event.getDeletionsCount());
		if (event.getDeletionsCount() != 0)
			plugin.sendLocaleList(sender, "COMMAND.PLAYER.DELETE.LISTHEADER", "COMMAND.PLAYER.DELETE.LISTFORMAT", null, -1, 1, new ArrayList<String>(event.getDeletions()));
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		if (args.length != 1)
			return null;
		return OfflinePlayerParamitrisable.tabHelp(args[0]);
	}

	@Override
	@Permission({ "crazycore.player.delete.self", "crazycore.player.delete.other" })
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazycore.player.delete.self") || PermissionModule.hasPermission(sender, "crazycore.player.delete.other");
	}
}
