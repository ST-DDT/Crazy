package de.st_ddt.crazyutil.conditions.player;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.conditions.BasicCondition;
import de.st_ddt.crazyutil.conditions.checker.ConditionChecker;
import de.st_ddt.crazyutil.conditions.checker.PlayerConditionChecker;

public abstract class BasicPlayerCondition extends BasicCondition
{

	public BasicPlayerCondition()
	{
		super();
	}

	public BasicPlayerCondition(final ConfigurationSection config)
	{
		super(config);
	}

	@Override
	public boolean isApplicable(final Class<? extends ConditionChecker> clazz)
	{
		return PlayerConditionChecker.class.isAssignableFrom(clazz);
	}

	@Override
	public final boolean check(final ConditionChecker checker)
	{
		return check((PlayerConditionChecker) checker);
	}

	public abstract boolean check(final PlayerConditionChecker checker);
}
