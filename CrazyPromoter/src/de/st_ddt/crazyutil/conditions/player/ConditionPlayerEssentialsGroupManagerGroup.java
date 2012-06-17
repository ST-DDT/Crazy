package de.st_ddt.crazyutil.conditions.player;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.data.User;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

//requires Essentials Group Manager
public class ConditionPlayerEssentialsGroupManagerGroup extends ConditionPlayerGroup
{

	protected static GroupManager plugin = null;

	public ConditionPlayerEssentialsGroupManagerGroup(final ConfigurationSection config)
	{
		super(config);
	}

	public ConditionPlayerEssentialsGroupManagerGroup(final String groupname)
	{
		super(groupname);
	}

	@Override
	public boolean match(final Player tester)
	{
		final User user = getGroupManager().getWorldsHolder().getWorldData(tester).getUser(tester.getName());
		if (user == null)
			return false;
		return user.getGroupName().equals(groupname);
	}

	public static GroupManager getGroupManager()
	{
		try
		{
			if (plugin == null)
				plugin = (GroupManager) Bukkit.getPluginManager().getPlugin("GroupManager");
		}
		catch (final Exception e)
		{
			plugin = null;
		}
		return plugin;
	}
}
