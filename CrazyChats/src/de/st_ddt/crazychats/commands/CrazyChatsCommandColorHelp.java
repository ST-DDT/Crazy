package de.st_ddt.crazychats.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class CrazyChatsCommandColorHelp extends CrazyChatsCommandExecutor
{

	public CrazyChatsCommandColorHelp(final CrazyChats plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYCHATS.COMMAND.COLORHELP $ColorHelp$")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		final StringBuilder builder = new StringBuilder();
		for (final ChatColor color : ChatColor.values())
			builder.append(color.toString()).append(color.getChar());
		plugin.sendLocaleMessage("COMMAND.COLORHELP", sender, builder.toString());
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazychats.coloredchat");
	}
}
