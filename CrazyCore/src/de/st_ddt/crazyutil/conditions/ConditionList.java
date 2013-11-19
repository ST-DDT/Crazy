package de.st_ddt.crazyutil.conditions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.conditions.checker.ConditionChecker;

public abstract class ConditionList extends BasicCondition
{

	protected final List<Condition> conditions = new ArrayList<Condition>();

	public ConditionList()
	{
		super();
	}

	public ConditionList(final List<Condition> conditions)
	{
		super();
		this.conditions.addAll(conditions);
	}

	public ConditionList(final ConfigurationSection config) throws Exception
	{
		super(config);
		final ConfigurationSection entryConfig = config.getConfigurationSection("conditions");
		for (final String key : entryConfig.getKeys(false))
			conditions.add(BasicCondition.load(entryConfig.getConfigurationSection(key)));
	}

	@Override
	public boolean isApplicable(final Class<? extends ConditionChecker> clazz)
	{
		for (final Condition condition : conditions)
			if (!condition.isApplicable(clazz))
				return false;
		return true;
	}

	@Override
	public final void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		int a = 0;
		config.set(path + "conditions", null);
		for (final Condition condition : conditions)
			condition.save(config, path + "conditions." + condition.getType() + (a++) + ".");
	}

	public final List<Condition> getConditions()
	{
		return conditions;
	}
}
