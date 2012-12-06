package de.st_ddt.crazyutil.modules.permissions;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.platymuus.bukkit.permissions.Group;
import com.platymuus.bukkit.permissions.PermissionsPlugin;

import de.st_ddt.crazyutil.modules.Module.Named;
import de.st_ddt.crazyutil.modules.Module.PluginDepency;

@Named(name = "PermissionsBukkit")
@PluginDepency(depend = "PermissionsBukkit")
public class PermissionPermissionsBukkitSystem extends PermissionBukkitSystem
{

	private final PermissionsPlugin plugin;

	public PermissionPermissionsBukkitSystem()
	{
		super();
		plugin = (PermissionsPlugin) Bukkit.getServer().getPluginManager().getPlugin("PermissionsBukkit");
	}

	@Override
	public String getName()
	{
		return "PermissionsBukkit";
	}

	@Override
	public boolean hasGroup(final Player player, final String name)
	{
		return super.hasGroup(player, name) || getGroups(player).contains(name);
	}

	@Override
	public String getGroup(final Player player)
	{
		for (final Group group : plugin.getGroups(player.getName()))
			return group.getName();
		return null;
	}

	@Override
	public Set<String> getGroups(final Player player)
	{
		final Set<String> res = new HashSet<String>();
		for (final Group group : plugin.getGroups(player.getName()))
			res.add(group.getName());
		return res;
	}
}
