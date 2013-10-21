package de.st_ddt.crazyutil.conditions;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.conditions.checker.ConditionChecker;

public class Condition_FALSE extends BasicCondition
{

	public Condition_FALSE()
	{
		super();
	}

	public Condition_FALSE(final ConfigurationSection config)
	{
		super(config);
	}

	@Override
	public String getType()
	{
		return "FALSE";
	}

	@Override
	public boolean isApplicable(final Class<? extends ConditionChecker> clazz)
	{
		return true;
	}

	@Override
	public boolean check(final ConditionChecker property)
	{
		return false;
	}
}
