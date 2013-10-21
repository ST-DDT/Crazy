package de.st_ddt.crazyutil.conditions;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.conditions.checker.ConditionChecker;

public class Condition_AND extends ConditionList
{

	public Condition_AND()
	{
		super();
	}

	public Condition_AND(final List<Condition> conditions)
	{
		super(conditions);
	}

	public Condition_AND(final ConfigurationSection config) throws Exception
	{
		super(config);
	}

	@Override
	public String getType()
	{
		return "AND";
	}

	@Override
	public boolean check(final ConditionChecker property)
	{
		for (final Condition condition : conditions)
			if (!condition.check(property))
				return false;
		return true;
	}
}
