package de.st_ddt.crazyutil.conditions.player;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

//requires PermissionsEx (PEX)
public class ConditionPlayerPEXGroup extends ConditionPlayer
{

	protected String groupname;

	public ConditionPlayerPEXGroup(ConfigurationSection config)
	{
		super(config);
		this.groupname = config.getString("group", "default");
	}

	public ConditionPlayerPEXGroup(String groupname)
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
		PermissionManager manager=getPermissionManager();
		if (manager==null)
			return false;
		for(PermissionGroup group:manager.getUser(tester).getGroups())
			if (group.getName().equalsIgnoreCase(groupname))
				return true;
		return false;
	}

	public String getGroupname()
	{
		return groupname;
	}

	public void setGroupname(String groupname)
	{
		this.groupname = groupname;
	}

	public PermissionManager getPermissionManager()
	{
		try
		{
			return PermissionsEx.getPermissionManager();
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
