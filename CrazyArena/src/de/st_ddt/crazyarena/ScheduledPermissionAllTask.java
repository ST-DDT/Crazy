package de.st_ddt.crazyarena;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

import de.st_ddt.crazyplugin.CrazyPlugin;

public class ScheduledPermissionAllTask implements Runnable
{

	@Override
	public void run()
	{
		Permission permission = Bukkit.getPluginManager().getPermission("crazyarena*.*");
		for (CrazyPlugin plugin : CrazyArenaPlugin.getCrazyArenaPlugins())
			permission.getChildren().put(plugin.getName().toLowerCase() + ".*", true);
		permission.recalculatePermissibles();
	}
}
