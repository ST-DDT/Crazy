package de.st_ddt.crazyutil.conditions;

import java.lang.reflect.Constructor;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.conditions.checker.ConditionChecker;
import de.st_ddt.crazyutil.conditions.player.Condition_Player_Permission;

public abstract class BasicCondition implements Condition
{

	static
	{
		CONDITIONCLASSES.put("TRUE", Condition_TRUE.class);
		CONDITIONCLASSES.put("FALSE", Condition_FALSE.class);
		CONDITIONCLASSES.put("NOT", Condition_NOT.class);
		CONDITIONCLASSES.put("AND", Condition_AND.class);
		CONDITIONCLASSES.put("OR", Condition_OR.class);
		CONDITIONCLASSES.put("PLAYER_PERMISSION", Condition_Player_Permission.class);
	}

	public static Condition load(final ConfigurationSection config) throws Exception
	{
		if (config == null)
			throw new IllegalArgumentException("ConfigurationSection cannot be NULL!");
		final String type = config.getString("type");
		if (type == null)
			throw new IllegalArgumentException("ConditionType cannot be NULL!");
		final Class<? extends Condition> clazz = CONDITIONCLASSES.get(type);
		if (clazz == null)
			throw new IllegalArgumentException("ConditionClass cannot be NULL! The ConditionType is corruped, please check that!");
		try
		{
			final Constructor<? extends Condition> constructor = clazz.getConstructor(ConfigurationSection.class);
			return constructor.newInstance(config);
		}
		catch (final Exception e)
		{
			System.err.println("WARNING: Serious bug detected, please report this issue!");
			throw e;
		}
	}

	public BasicCondition()
	{
		super();
	}

	public BasicCondition(final ConfigurationSection config)
	{
		super();
	}

	@Override
	public abstract String getType();

	@Override
	public abstract boolean isApplicable(Class<? extends ConditionChecker> clazz);

	@Override
	public abstract boolean check(ConditionChecker checker);

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "type", getType());
	}
}
