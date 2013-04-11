package de.st_ddt.crazyutil.modules.permissions;

import java.util.LinkedHashSet;
import java.util.Set;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyutil.modules.Module.Named;
import de.st_ddt.crazyutil.modules.Module.PluginDepency;

@Named(name = "Vault")
@PluginDepency(depend = "Vault")
class PermissionVaultSystem implements PermissionSystem
{

	private final Permission permission;
	private final Chat chat;

	public PermissionVaultSystem()
	{
		super();
		permission = Bukkit.getServicesManager().getRegistration(Permission.class).getProvider();
		chat = Bukkit.getServicesManager().getRegistration(Chat.class).getProvider();
	}

	@Override
	public String getName()
	{
		return "Vault";
	}

	@Override
	public boolean hasPermission(final CommandSender sender, final String permission)
	{
		return sender.hasPermission(permission);
	}

	@Override
	public boolean hasGroup(final Player player, final String name)
	{
		if (permission == null)
			return false;
		final String[] groups = permission.getPlayerGroups(player);
		if (groups == null)
			return false;
		for (final String group : groups)
			if (group.equals(name))
				return true;
		return false;
	}

	@Override
	public String getGroup(final Player player)
	{
		if (permission == null)
			return null;
		else
			return permission.getPrimaryGroup(player);
	}

	@Override
	public String getGroupPrefix(final Player player)
	{
		if (chat == null)
			return null;
		else
			return chat.getPlayerPrefix(player);
	}

	@Override
	public String getGroupSuffix(final Player player)
	{
		if (chat == null)
			return null;
		else
			return chat.getPlayerSuffix(player);
	}

	@Override
	public Set<String> getGroups(final Player player)
	{
		if (permission == null)
			return null;
		final String[] groups = permission.getPlayerGroups(player);
		if (groups == null)
			return null;
		final Set<String> res = new LinkedHashSet<String>();
		for (final String group : groups)
			res.add(group);
		return res;
	}
}
