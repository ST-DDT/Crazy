package de.st_ddt.crazyutil.conditions.entity;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.conditions.BasicCondition;
import de.st_ddt.crazyutil.conditions.checker.ConditionChecker;
import de.st_ddt.crazyutil.conditions.checker.EntityConditionChecker;

public abstract class BasicEntityCondition extends BasicCondition
{

	public BasicEntityCondition()
	{
		super();
	}

	public BasicEntityCondition(final ConfigurationSection config)
	{
		super(config);
	}

	@Override
	public boolean isApplicable(final Class<? extends ConditionChecker> clazz)
	{
		return EntityConditionChecker.class.isAssignableFrom(clazz);
	}

	@Override
	public final boolean check(final ConditionChecker checker)
	{
		return check((EntityConditionChecker) checker);
	}

	public abstract boolean check(final EntityConditionChecker checker);
}
