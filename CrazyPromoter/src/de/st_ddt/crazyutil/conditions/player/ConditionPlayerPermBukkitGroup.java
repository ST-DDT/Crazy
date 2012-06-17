package de.st_ddt.crazyutil.conditions.player;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.platymuus.bukkit.permissions.Group;
import com.platymuus.bukkit.permissions.PermissionsPlugin;

//requires PermissionsBukkit (PermBukkit)
public class ConditionPlayerPermBukkitGroup extends ConditionPlayerGroup
{

	protected Group group;

	public ConditionPlayerPermBukkitGroup(ConfigurationSection config)
	{
		super(config);
	}

	public ConditionPlayerPermBukkitGroup(String groupname)
	{
		super(groupname);
	}

	public void setGroupname(String groupname)
	{
		super.setGroupname(groupname);
		group = null;
	}

	public Group getGroup()
	{
		if (group == null)
		{
			PermissionsPlugin plugin = (PermissionsPlugin) Bukkit.getServer().getPluginManager().getPlugin("PermissionsBukkit");
			if (plugin == null)
				return null;
			group = plugin.getGroup(groupname);
		}
		return group;
	}

	@Override
	public boolean match(Player tester)
	{
		if (getGroup() == null)
			return false;
		return getGroup().getOnlinePlayers().contains(tester);
	}
}
