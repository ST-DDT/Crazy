package de.st_ddt.crazyutil.conditions;

import org.bukkit.configuration.ConfigurationSection;

public class Condition_TRUE<T> extends ConditionBase<T>
{

	public Condition_TRUE(final ConfigurationSection config)
	{
		super(config);
	}

	public Condition_TRUE()
	{
		super();
	}

	@Override
	public boolean match(final T tester)
	{
		return true;
	}

	@Override
	public String getTypeIdentifier()
	{
		return "TRUE";
	}
}
