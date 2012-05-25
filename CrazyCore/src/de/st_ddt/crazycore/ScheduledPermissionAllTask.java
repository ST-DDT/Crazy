package de.st_ddt.crazycore;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

import de.st_ddt.crazyplugin.CrazyPlugin;

public class ScheduledPermissionAllTask implements Runnable
{

	@Override
	public void run()
	{
		final Permission permission = Bukkit.getPluginManager().getPermission("crazy*.*");
		for (final CrazyPlugin plugin : CrazyPlugin.getCrazyPlugins())
			permission.getChildren().put(plugin.getName().toLowerCase() + ".*", true);
		permission.recalculatePermissibles();
	}
}
