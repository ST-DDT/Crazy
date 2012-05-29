package de.st_ddt.crazyutil.conditions;

import java.util.ArrayList;
import org.bukkit.configuration.ConfigurationSection;

public abstract class ConditionList<T> extends ConditionBase<T>
{

	protected final ArrayList<ConditionBase<T>> conditions = new ArrayList<ConditionBase<T>>();

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
				ConditionBase<T> condition = (ConditionBase<T>) ConditionBase.load(config.getConfigurationSection(name));
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
	public final void save(ConfigurationSection config, String path)
	{
		super.save(config, path);
		int a = 0;
		config.set(path + "conditions", null);
		for (ConditionBase<? extends T> condition : conditions)
			condition.save(config, path + "conditions." + condition.getTypeIdentifier() + (a++) + ".");
	}

	public final ArrayList<ConditionBase<T>> getConditions()
	{
		return conditions;
	}
}
