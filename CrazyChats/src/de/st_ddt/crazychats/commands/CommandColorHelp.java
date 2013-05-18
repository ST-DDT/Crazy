package de.st_ddt.crazychats.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.source.Localized;

public class CommandColorHelp extends CommandExecutor
{

	public CommandColorHelp(final CrazyChats plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYCHATS.COMMAND.COLORHELP $ColorHelp$")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		final StringBuilder builder = new StringBuilder();
		for (final ChatColor color : ChatColor.values())
			builder.append(color.toString()).append(Character.toUpperCase(color.getChar())).append(color.getChar()).append(ChatColor.RESET);
		plugin.sendLocaleMessage("COMMAND.COLORHELP", sender, builder.toString());
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazychats.coloredchat");
	}
}
