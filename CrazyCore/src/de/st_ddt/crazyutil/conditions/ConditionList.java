package de.st_ddt.crazyutil.conditions;

import java.util.ArrayList;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class ConditionList<T> extends Condition<T>
{

	protected final ArrayList<Condition<T>> conditions = new ArrayList<Condition<T>>();

	public ConditionList(ConfigurationSection config)
	{
		super(config);
		config = config.getConfigurationSection("conditions");
		if (config == null)
			return;
		for (String name : config.getKeys(false))
		{
			try
			{
				@SuppressWarnings("unchecked")
				Condition<T> condition = (Condition<T>) Condition.load(config.getConfigurationSection(name));
				if (condition != null)
					conditions.add(condition);
			}
			catch (Exception e)
			{
				System.out.println("Error loading condition: " + name);
				e.printStackTrace();
			}
		}
	}

	public ConditionList()
	{
		super();
	}

	@Override
	public final void save(FileConfiguration config, String path)
	{
		super.save(config, path);
		int a = 0;
		config.set(path + "conditions", null);
		for (Condition<? extends T> condition : conditions)
			condition.save(config, path + "conditions." + condition.getTypeIdentifier() + (a++)+".");
	}

	public final ArrayList<Condition<T>> getConditions()
	{
		return conditions;
	}
}
