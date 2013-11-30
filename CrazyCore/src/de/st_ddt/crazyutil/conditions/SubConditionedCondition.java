package de.st_ddt.crazyutil.conditions;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.conditions.checker.ConditionChecker;

public abstract class SubConditionedCondition extends BasicCondition
{

	protected final Condition condition;

	public SubConditionedCondition()
	{
		super();
		condition = new Condition_TRUE();
	}

	public SubConditionedCondition(final Condition condition)
	{
		super();
		this.condition = condition;
	}

	public SubConditionedCondition(final ConfigurationSection config) throws Exception
	{
		super(config);
		condition = BasicCondition.load(config.getConfigurationSection("condition"));
	}

	@Override
	public abstract String getType();

	@Override
	public boolean isApplicable(final Class<? extends ConditionChecker> clazz)
	{
		return condition.isApplicable(clazz);
	}

	@Override
	public boolean check(final ConditionChecker checker)
	{
		return !condition.check(checker);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		condition.save(config, path + "condition.");
	}
}
