package de.st_ddt.crazyutil.conditions;

import org.bukkit.configuration.ConfigurationSection;

public class Condition_NOT<T> extends ConditionBase<T>
{

	protected ConditionBase<T> condition = null;

	@SuppressWarnings("unchecked")
	public Condition_NOT(final ConfigurationSection config)
	{
		super(config);
		condition = (ConditionBase<T>) ConditionBase.load(config.getConfigurationSection("condition"));
	}

	public Condition_NOT()
	{
		super();
		condition = new Condition_TRUE<T>();
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		condition.save(config, path + "condition.");
	}

	@Override
	public String getTypeIdentifier()
	{
		return "NOT";
	}

	@Override
	public boolean match(final T tester)
	{
		return !condition.match(tester);
	}
}
