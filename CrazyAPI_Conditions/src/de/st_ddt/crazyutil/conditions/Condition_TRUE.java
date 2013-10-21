package de.st_ddt.crazyutil.conditions;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.conditions.checker.ConditionChecker;

public class Condition_TRUE extends BasicCondition
{

	public Condition_TRUE()
	{
		super();
	}

	public Condition_TRUE(final ConfigurationSection config)
	{
		super(config);
	}

	@Override
	public String getType()
	{
		return "TRUE";
	}

	@Override
	public boolean isApplicable(final Class<? extends ConditionChecker> clazz)
	{
		return true;
	}

	@Override
	public boolean check(final ConditionChecker property)
	{
		return true;
	}
}
