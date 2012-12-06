package de.st_ddt.crazyutil.conditions.player;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class ConditionPlayerPermissionGroup extends ConditionPlayer
{

	protected String groupname;

	public ConditionPlayerPermissionGroup(final ConfigurationSection config)
	{
		super(config);
		this.groupname = config.getString("group", "default");
	}

	public ConditionPlayerPermissionGroup(final String groupname)
	{
		super();
		this.groupname = groupname;
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		config.set(path + "group", groupname);
	}

	@Override
	public String getTypeIdentifier()
	{
		return "PermissionsGroup";
	}

	public String getGroupname()
	{
		return groupname;
	}

	public void setGroupname(final String groupname)
	{
		this.groupname = groupname;
	}

	@Override
	public boolean match(final Player tester)
	{
		return PermissionModule.hasGroup(tester, groupname);
	}
}
