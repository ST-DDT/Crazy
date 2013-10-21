package de.st_ddt.crazyutil.conditions;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.conditions.checker.ConditionChecker;

public class Condition_NOT extends BasicCondition
{

	protected Condition condition = null;

	public Condition_NOT()
	{
		super();
		condition = new Condition_TRUE();
	}

	public Condition_NOT(final Condition condition)
	{
		super();
		this.condition = condition;
	}

	public Condition_NOT(final ConfigurationSection config) throws Exception
	{
		super(config);
		condition = BasicCondition.load(config.getConfigurationSection("condition"));
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		condition.save(config, path + "condition.");
	}

	@Override
	public String getType()
	{
		return "NOT";
	}

	@Override
	public boolean isApplicable(final Class<? extends ConditionChecker> clazz)
	{
		return condition.isApplicable(clazz);
	}

	@Override
	public boolean check(final ConditionChecker property)
	{
		return !condition.check(property);
	}
}
