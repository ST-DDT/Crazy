package de.st_ddt.crazychats.commands;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.data.ChatPlayerData;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.source.Localized;

public class CrazyChatsCommandPlayerSearch extends CrazyChatsCommandExecutor
{

	public CrazyChatsCommandPlayerSearch(final CrazyChats plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYCHATS.COMMAND.PLAYER.SEARCH.HEADER $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$", "CRAZYCHATS.COMMAND.PLAYER.SEARCH.LISTFORMAT $Index$ $Entry$ $ChatHeader$", "CRAZYCHATS.COMMAND.PLAYER.SEARCH.ENTRYFORMAT" })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length == 0)
			throw new CrazyCommandUsageException("<Name>");
		final Pattern pattern = Pattern.compile(ChatHelper.listingString(" ", args), Pattern.CASE_INSENSITIVE);
		final Set<String> names = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		for (final Player player : Bukkit.getOnlinePlayers())
			if (pattern.matcher(player.getName()).find())
				names.add(player.getName());
			else if (pattern.matcher(ChatColor.stripColor(player.getDisplayName())).find())
				names.add(player.getName() + " (" + player.getDisplayName() + ")");
			else if (pattern.matcher(ChatColor.stripColor(player.getPlayerListName())).find())
				names.add(player.getName() + " (" + player.getPlayerListName() + ")");
		for (final ChatPlayerData data : plugin.getPlayerData())
			if (pattern.matcher(data.getName()).find())
				names.add(data.getName());
			else if (data.getDisplayName() != null && pattern.matcher(ChatColor.stripColor(data.getDisplayName())).find())
				names.add(data.getName() + " (" + data.getDisplayName() + ")");
			else if (data.getListName() != null && pattern.matcher(ChatColor.stripColor(data.getListName())).find())
				names.add(data.getName() + " (" + data.getListName() + ")");
			else if (data.getHeadName() != null && pattern.matcher(ChatColor.stripColor(data.getHeadName())).find())
				names.add(data.getName() + " (" + data.getHeadName() + ")");
		plugin.sendLocaleList(sender, "COMMAND.PLAYER.SEARCH", -1, -1, new ArrayList<String>(names));
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazychats.player.search");
	}
}
