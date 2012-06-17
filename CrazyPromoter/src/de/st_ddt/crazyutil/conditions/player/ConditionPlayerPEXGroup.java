package de.st_ddt.crazyutil.conditions.player;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

//requires PermissionsEx (PEX)
public class ConditionPlayerPEXGroup extends ConditionPlayerGroup
{

	public ConditionPlayerPEXGroup(ConfigurationSection config)
	{
		super(config);
	}

	public ConditionPlayerPEXGroup(String groupname)
	{
		super(groupname);
	}

	@Override
	public boolean match(Player tester)
	{
		PermissionManager manager = getPermissionManager();
		if (manager == null)
			return false;
		for (PermissionGroup group : manager.getUser(tester).getGroups())
			if (group.getName().equalsIgnoreCase(groupname))
				return true;
		return false;
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
