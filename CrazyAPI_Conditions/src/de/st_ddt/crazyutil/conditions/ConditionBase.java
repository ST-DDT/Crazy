package de.st_ddt.crazyutil.conditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.ObjectSaveLoadHelper;

public abstract class ConditionBase<T> implements Condition<T>
{

	@SuppressWarnings("unchecked")
	public static <T> ConditionBase<T> load(ConfigurationSection config)
	{
		return (ConditionBase<T>) ObjectSaveLoadHelper.load(config, null, new Class<?>[] { ConfigurationSection.class }, new Object[] { config }, "de.st_ddt.crazyutil.conditions");
	}

	public ConditionBase(ConfigurationSection config)
	{
		super();
	}

	public ConditionBase()
	{
		super();
	}

	@Override
	public void save(ConfigurationSection config, String path)
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
	public final boolean match(T[] testers)
	{
		for (T tester : testers)
			if (!match(tester))
				return false;
		return true;
	}

	@Override
	public final boolean match(List<? extends T> testers)
	{
		for (T tester : testers)
			if (!match(tester))
				return false;
		return true;
	}

	@Override
	public final List<T> getMatching(T[] testers)
	{
		ArrayList<T> list = new ArrayList<T>();
		for (T tester : testers)
			if (match(tester))
				list.add(tester);
		return list;
	}

	@Override
	public final List<T> getMatching(Collection<? extends T> testers)
	{
		ArrayList<T> list = new ArrayList<T>();
		for (T tester : testers)
			if (match(tester))
				list.add(tester);
		return list;
	}
}
