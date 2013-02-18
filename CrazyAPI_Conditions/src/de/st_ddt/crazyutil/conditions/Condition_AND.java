package de.st_ddt.crazyutil.conditions;

import org.bukkit.configuration.ConfigurationSection;

public class Condition_AND<T> extends ConditionList<T>
{

	public Condition_AND(final ConfigurationSection config)
	{
		super(config);
	}

	public Condition_AND()
	{
		super();
	}

	@Override
	public boolean match(final T tester)
	{
		for (final Condition<T> condition : conditions)
			if (!condition.match(tester))
				return false;
		return true;
	}

	@Override
	public String getTypeIdentifier()
	{
		return "AND";
	}
}
