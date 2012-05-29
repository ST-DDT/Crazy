package de.st_ddt.crazyutil.conditions;

import org.bukkit.configuration.ConfigurationSection;

public class Condition_OR<T> extends ConditionList<T>
{

	public Condition_OR(ConfigurationSection config)
	{
		super(config);
	}

	public Condition_OR()
	{
		super();
	}

	@Override
	public boolean match(T tester)
	{
		for (Condition<T> condition : conditions)
			if (condition.match(tester))
				return true;
		return false;
	}

	@Override
	public String getTypeIdentifier()
	{
		return "OR";
	}
}
