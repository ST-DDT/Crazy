package de.st_ddt.crazyutil.modules.permissions;

import java.util.LinkedHashSet;
import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
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

	@Override
	public boolean hasGroup(final Player player, final String name)
	{
		final PermissionUser user = getUser(player);
		if (user == null)
			return false;
		for (final String group : user.getGroupsNames())
			if (name.equals(group))
				return true;
		for (final String group : user.getGroupsNames(player.getWorld().getName()))
			if (name.equals(group))
				return true;
		return false;
	}

	@Override
	public String getGroup(final Player player)
	{
		final PermissionUser user = getUser(player);
		if (user == null)
			return null;
		else if (user.getGroups().length == 0)
			if (user.getGroups(player.getWorld().getName()).length == 0)
				return null;
			else
				return user.getGroupsNames(player.getWorld().getName())[0];
		else
			return user.getGroupsNames()[0];
	}

	@Override
	public Set<String> getGroups(final Player player)
	{
		final PermissionUser user = getUser(player);
		if (user == null)
			return null;
		final Set<String> groups = new LinkedHashSet<String>();
		for (final String group : user.getGroupsNames())
			groups.add(group);
		for (final String group : user.getGroupsNames(player.getWorld().getName()))
			groups.add(group);
		return groups;
	}

	private PermissionUser getUser(final Player player)
	{
		return plugin.getUser(player);
	}
}
