package de.st_ddt.crazyutil.conditions.player;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class ConditionPlayerPermission extends ConditionPlayer
{

	protected String permission;

	public ConditionPlayerPermission(final ConfigurationSection config)
	{
		super(config);
		permission = config.getString("permission", "sample");
	}

	public ConditionPlayerPermission(final String permission)
	{
		super();
		this.permission = permission;
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
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
	public boolean match(final Player tester)
	{
		return PermissionModule.hasPermission(tester, permission);
	}
}
