package de.st_ddt.crazyutil.conditions.livingentity;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.conditions.checker.ConditionChecker;
import de.st_ddt.crazyutil.conditions.checker.EntityConditionChecker;
import de.st_ddt.crazyutil.conditions.checker.LivingEntityConditionChecker;
import de.st_ddt.crazyutil.conditions.entity.BasicEntityCondition;

public abstract class BasicLivingEntityCondition extends BasicEntityCondition
{

	public BasicLivingEntityCondition()
	{
		super();
	}

	public BasicLivingEntityCondition(final ConfigurationSection config)
	{
		super(config);
	}

	@Override
	public boolean isApplicable(final Class<? extends ConditionChecker> clazz)
	{
		return LivingEntityConditionChecker.class.isAssignableFrom(clazz);
	}

	@Override
	public final boolean check(final EntityConditionChecker checker)
	{
		return check((LivingEntityConditionChecker) checker);
	}

	public abstract boolean check(final LivingEntityConditionChecker checker);
}
