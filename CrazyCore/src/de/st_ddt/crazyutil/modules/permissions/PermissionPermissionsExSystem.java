package de.st_ddt.crazyutil.modules.permissions;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import de.st_ddt.crazyutil.modules.Module.Named;
import de.st_ddt.crazyutil.modules.Module.PluginDepency;

@Named(name = "PermissionsEx")
@PluginDepency(depend = "PermissionsEx")
public class PermissionPermissionsExSystem implements PermissionSystem
{

	private final PermissionManager plugin;

	public PermissionPermissionsExSystem()
	{
		super();
		plugin = PermissionsEx.getPermissionManager();
	}

	@Override
	public String getName()
	{
		return "PermissionsEx";
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
		return plugin.has(player, permission);
	}
}
