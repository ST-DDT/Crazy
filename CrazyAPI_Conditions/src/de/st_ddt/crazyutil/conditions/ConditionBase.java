package de.st_ddt.crazyutil.conditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.ObjectSaveLoadHelper;

public abstract class ConditionBase<T> implements Condition<T>
{

	@SuppressWarnings("unchecked")
	public static <T> ConditionBase<T> load(final ConfigurationSection config)
	{
		return ObjectSaveLoadHelper.load(config, ConditionBase.class, new Class<?>[] { ConfigurationSection.class }, new Object[] { config }, "de.st_ddt.crazyutil.conditions");
	}

	public ConditionBase(final ConfigurationSection config)
	{
		super();
	}

	public ConditionBase()
	{
		super();
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "type", getClass().getName());
	}

	@Override
	public String getTypeIdentifier()
	{
		return "Condition";
	}

	@Override
	public abstract boolean match(T tester);

	@Override
	public final boolean match(final T[] testers)
	{
		for (final T tester : testers)
			if (!match(tester))
				return false;
		return true;
	}

	@Override
	public final boolean match(final List<? extends T> testers)
	{
		for (final T tester : testers)
			if (!match(tester))
				return false;
		return true;
	}

	@Override
	public final List<T> getMatching(final T[] testers)
	{
		final ArrayList<T> list = new ArrayList<T>();
		for (final T tester : testers)
			if (match(tester))
				list.add(tester);
		return list;
	}

	@Override
	public final List<T> getMatching(final Collection<? extends T> testers)
	{
		final ArrayList<T> list = new ArrayList<T>();
		for (final T tester : testers)
			if (match(tester))
				list.add(tester);
		return list;
	}
}
