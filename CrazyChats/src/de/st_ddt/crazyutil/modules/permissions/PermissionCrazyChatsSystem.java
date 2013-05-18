package de.st_ddt.crazyutil.modules.permissions;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazyutil.modules.Module.Named;
import de.st_ddt.crazyutil.modules.Module.PluginDepency;

@Named(name = "CrazyChats")
@PluginDepency(depend = "CrazyChats")
public class PermissionCrazyChatsSystem implements PermissionSystem
{

	private final CrazyChats plugin = CrazyChats.getPlugin();

	@Override
	public String getName()
	{
		return "CrazyChats";
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
		final Map<String, String> groupPrefixes = plugin.getGroupPrefixes();
		for (final Entry<String, String> entry : groupPrefixes.entrySet())
			if (!entry.getKey().equals("nogroup"))
				if (PermissionModule.hasGroup(player, entry.getKey()))
					return entry.getValue();
		return groupPrefixes.get("nogroup");
	}

	@Override
	public String getGroupSuffix(final Player player)
	{
		final Map<String, String> groupSuffixes = plugin.getGroupSuffixes();
		for (final Entry<String, String> entry : groupSuffixes.entrySet())
			if (!entry.getKey().equals("nogroup"))
				if (PermissionModule.hasGroup(player, entry.getKey()))
					return entry.getValue();
		return groupSuffixes.get("nogroup");
	}

	@Override
	public Set<String> getGroups(final Player player)
	{
		return null;
	}
}
