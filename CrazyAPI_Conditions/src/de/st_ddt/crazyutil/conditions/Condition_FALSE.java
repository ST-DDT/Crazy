package de.st_ddt.crazyutil.conditions;

import org.bukkit.configuration.ConfigurationSection;

public class Condition_FALSE<T> extends ConditionBase<T>
{

	public Condition_FALSE(final ConfigurationSection config)
	{
		super(config);
	}

	public Condition_FALSE()
	{
		super();
	}

	@Override
	public boolean match(final T tester)
	{
		return false;
	}

	@Override
	public String getTypeIdentifier()
	{
		return "FALSE";
	}
}
