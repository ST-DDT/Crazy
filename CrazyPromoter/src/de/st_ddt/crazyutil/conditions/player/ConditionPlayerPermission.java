package de.st_ddt.crazyutil.conditions.player;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ConditionPlayerPermission extends ConditionPlayer
{

	protected String permission;

	public ConditionPlayerPermission(ConfigurationSection config)
	{
		super(config);
		permission = config.getString("permission", "sample");
	}

	public ConditionPlayerPermission(String permission)
	{
		super();
		this.permission = permission;
	}

	@Override
	public void save(FileConfiguration config, String path)
	{
		super.save(config, path);
		config.set(path + "permission", permission);
	}

	@Override
	public String getTypeIdentifier()
	{
		return "Permission";
	}

	@Override
	public boolean match(Player tester)
	{
		return tester.hasPermission(permission);
	}
}
