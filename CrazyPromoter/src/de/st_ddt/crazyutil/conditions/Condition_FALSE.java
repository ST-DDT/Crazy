package de.st_ddt.crazyutil.conditions;

import org.bukkit.configuration.ConfigurationSection;

public class Condition_FALSE<T> extends Condition<T>
{

	public Condition_FALSE(ConfigurationSection config)
	{
		super(config);
	}

	public Condition_FALSE()
	{
		super();
	}

	@Override
	public boolean match(T tester)
	{
		return false;
	}

	@Override
	public String getTypeIdentifier()
	{
		return "FALSE";
	}
}
