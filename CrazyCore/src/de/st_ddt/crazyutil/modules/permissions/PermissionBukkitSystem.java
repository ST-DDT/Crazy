package de.st_ddt.crazyutil.modules.permissions;

import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyutil.modules.Module.Named;

@Named(name = "Bukkit")
class PermissionBukkitSystem implements PermissionSystem
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

	@Override
	public boolean hasGroup(final Player player, final String name)
	{
		return player.hasPermission("group." + name);
	}

	@Override
	public String getGroup(final Player player)
	{
		return null;
	}

	@Override
	public String getGroupPrefix(final Player player)
	{
		return null;
	}

	@Override
	public String getGroupSuffix(final Player player)
	{
		return null;
	}

	@Override
	public Set<String> getGroups(final Player player)
	{
		return null;
	}
}
