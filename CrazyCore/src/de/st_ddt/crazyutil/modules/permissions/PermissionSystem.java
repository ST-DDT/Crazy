package de.st_ddt.crazyutil.modules.permissions;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyutil.Named;

public interface PermissionSystem extends Named
{

	public boolean hasPermission(CommandSender sender, String permission);
}
