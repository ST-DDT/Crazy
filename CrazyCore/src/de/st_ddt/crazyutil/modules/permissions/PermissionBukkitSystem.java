package de.st_ddt.crazyutil.modules.permissions;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyutil.modules.permissions.PermissionSystem.Named;

@Named(name = "Bukkit")
public class PermissionBukkitSystem implements PermissionSystem
{

	public PermissionBukkitSystem()
	{
		super();
	}

	@Override
	public String getName()
	{
		return "Bukkit";
	}

	@Override
	public boolean hasPermission(final CommandSender sender, final String permission)
	{
		return sender.hasPermission(permission);
	}
}
