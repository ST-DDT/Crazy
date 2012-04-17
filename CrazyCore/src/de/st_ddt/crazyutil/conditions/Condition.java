package de.st_ddt.crazyutil.conditions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import de.st_ddt.crazyutil.ObjectSaveLoadHelper;

public abstract class Condition<T>
{

	@SuppressWarnings("unchecked")
	public static <T> Condition<T> load(ConfigurationSection config)
	{
		return (Condition<T>) ObjectSaveLoadHelper.load(config, null, new Class<?>[] { ConfigurationSection.class }, new Object[] { config }, "de.st_ddt.crazyutil.conditions");
	}

	public Condition(ConfigurationSection config)
	{
		super();
	}

	public Condition()
	{
		super();
	}

	public void save(FileConfiguration config, String path)
	{
		config.set(path + "type", getClass().getName());
	}

	public String getTypeIdentifier()
	{
		return "Condition";
	}

	public abstract boolean match(T tester);

	public final boolean match(T[] testers)
	{
		for (T tester : testers)
			if (!match(tester))
				return false;
		return true;
	}

	public final boolean match(List<? extends T> testers)
	{
		for (T tester : testers)
			if (!match(tester))
				return false;
		return true;
	}

	public final List<T> getMatching(T[] testers)
	{
		ArrayList<T> list = new ArrayList<T>();
		for (T tester : testers)
			if (match(tester))
				list.add(tester);
		return list;
	}

	public final List<T> getMatching(List<? extends T> testers)
	{
		ArrayList<T> list = new ArrayList<T>();
		for (T tester : testers)
			if (match(tester))
				list.add(tester);
		return list;
	}
}
