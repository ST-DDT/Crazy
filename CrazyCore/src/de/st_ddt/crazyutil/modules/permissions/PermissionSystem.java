package de.st_ddt.crazyutil.modules.permissions;

import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyutil.Named;

public interface PermissionSystem extends Named
{

	public boolean hasPermission(CommandSender sender, String permission);

	public boolean hasGroup(Player player, String name);

	public String getGroup(Player player);

	public Set<String> getGroups(Player player);
}
