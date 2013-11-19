package de.st_ddt.crazyutil.conditions.player;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.conditions.checker.PlayerConditionChecker;

public class Condition_Player_Permission extends BasicPlayerCondition
{

	private final String permission;

	public Condition_Player_Permission()
	{
		super();
		this.permission = null;
	}

	public Condition_Player_Permission(final String permission)
	{
		super();
		this.permission = permission;
	}

	public Condition_Player_Permission(final ConfigurationSection config)
	{
		super(config);
		final String permission = config.getString("permission", "<Permission>");
		if (permission.equalsIgnoreCase("<Permission>"))
			this.permission = null;
		else
			this.permission = permission;
	}

	@Override
	public String getType()
	{
		return "PLAYER_PERMISSION";
	}

	@Override
	public boolean check(final PlayerConditionChecker checker)
	{
		if (permission == null)
			return true;
		else
			return checker.getEntity().hasPermission(permission);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		if (permission == null)
			config.set(path + "permission", "<Permission>");
		else
			config.set(path + "permission", permission);
	}
}
