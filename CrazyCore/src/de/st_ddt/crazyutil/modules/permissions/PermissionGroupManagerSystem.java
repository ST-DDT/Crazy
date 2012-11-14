package de.st_ddt.crazyutil.modules.permissions;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyutil.modules.Module.Named;
import de.st_ddt.crazyutil.modules.Module.PluginDepency;

@Named(name = "GroupManager")
@PluginDepency(depend = "GroupManager")
public class PermissionGroupManagerSystem implements PermissionSystem
{

	private final GroupManager plugin;

	public PermissionGroupManagerSystem()
	{
		super();
		plugin = (GroupManager) Bukkit.getPluginManager().getPlugin("GroupManager");
	}

	@Override
	public String getName()
	{
		return "GroupManager";
	}

	@Override
	public boolean hasPermission(final CommandSender sender, final String permission)
	{
		if (sender instanceof Player)
			return hasPermission((Player) sender, permission);
		else
			return true;
	}

	public boolean hasPermission(final Player player, final String permission)
	{
		final AnjoPermissionsHandler handler = plugin.getWorldsHolder().getWorldPermissions(player);
		return handler != null && handler.has(player, permission);
	}
}
