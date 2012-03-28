package de.st_ddt.crazyutil.conditions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class Condition<T>
{

	@SuppressWarnings("unchecked")
	public static <T> Condition<T> load(ConfigurationSection config)
	{
		if (config == null)
			return null;
		String type = config.getString("type", "-1");
		if (type == "-1")
		{
			System.out.println("Invalid Condition Type!");
			return null;
		}
		Class<?> clazz = null;
		try
		{
			clazz = Class.forName(type);
		}
		catch (ClassNotFoundException e)
		{
			try
			{
				clazz = Class.forName("de.st_ddt.crazyarena.arenas." + type);
			}
			catch (ClassNotFoundException e2)
			{
				e.printStackTrace();
				return null;
			}
		}
		// if (Condition.class.isAssignableFrom(clazz))
		// {
		// System.out.println("Invalid ConditionType " + clazz.toString().substring(6));
		// return null;
		// }
		Condition<T> condition = null;
		try
		{
			condition = (Condition<T>) clazz.getConstructor(ConfigurationSection.class).newInstance(config);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return condition;
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
		config.set(path + "type", getClass().toString().substring(6));
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
