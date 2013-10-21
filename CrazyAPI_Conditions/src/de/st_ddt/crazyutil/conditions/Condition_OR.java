package de.st_ddt.crazyutil.conditions;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.conditions.checker.ConditionChecker;

public class Condition_OR extends ConditionList
{

	public Condition_OR()
	{
		super();
	}

	public Condition_OR(final List<Condition> conditions)
	{
		super(conditions);
	}

	public Condition_OR(final ConfigurationSection config) throws Exception
	{
		super(config);
	}

	@Override
	public String getType()
	{
		return "OR";
	}

	@Override
	public boolean check(final ConditionChecker checker)
	{
		for (final Condition condition : conditions)
			if (condition.check(checker))
				return true;
		return false;
	}
}
