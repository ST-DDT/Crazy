package de.st_ddt.crazyutil.conditions.player;

import org.bukkit.configuration.ConfigurationSection;

public abstract class ConditionPlayerGroup extends ConditionPlayer
{

	protected String groupname;

	public ConditionPlayerGroup(ConfigurationSection config)
	{
		super(config);
		this.groupname = config.getString("group", "default");
	}

	public ConditionPlayerGroup(String groupname)
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

	public String getGroupname()
	{
		return groupname;
	}

	public void setGroupname(String groupname)
	{
		this.groupname = groupname;
	}
}
