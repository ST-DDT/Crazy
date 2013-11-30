package de.st_ddt.crazyutil.conditions;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.conditions.checker.ConditionChecker;

public class Condition_NOT extends SubConditionedCondition
{

	public Condition_NOT()
	{
		super();
	}

	public Condition_NOT(final Condition condition)
	{
		super(condition);
	}

	public Condition_NOT(final ConfigurationSection config) throws Exception
	{
		super(config);
	}

	@Override
	public String getType()
	{
		return "NOT";
	}

	@Override
	public boolean check(final ConditionChecker checker)
	{
		return !condition.check(checker);
	}
}
