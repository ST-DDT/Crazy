package de.st_ddt.crazyutil.conditions.player;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.platymuus.bukkit.permissions.Group;
import com.platymuus.bukkit.permissions.PermissionsPlugin;

//requires PermissionsBukkit (PermBukkit)
public class ConditionPlayerPermissionGroup extends ConditionPlayer
{

	protected String groupname;
	protected Group group;

	public ConditionPlayerPermissionGroup(ConfigurationSection config)
	{
		super(config);
		this.groupname = config.getString("group", "default");
	}

	public ConditionPlayerPermissionGroup(String groupname)
	{
		super();
		this.groupname = groupname;
	}

	@Override
	public void save(ConfigurationSection config, String path)
	{
		super.save(config, path);
		config.set(path + "group", groupname);
	}

	@Override
	public String getTypeIdentifier()
	{
		return "PermissionsGroup";
	}

	@Override
	public boolean match(Player tester)
	{
		if (getGroup() == null)
			return false;
		return getGroup().getOnlinePlayers().contains(tester);
	}

	public String getGroupname()
	{
		return groupname;
	}

	public void setGroupname(String groupname)
	{
		this.groupname = groupname;
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
}
