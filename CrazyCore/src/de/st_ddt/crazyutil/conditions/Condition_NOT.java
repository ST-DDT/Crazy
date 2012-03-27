package de.st_ddt.crazyutil.conditions;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Condition_NOT<T> extends Condition<T>
{

	protected Condition<T> condition = null;

	@SuppressWarnings("unchecked")
	public Condition_NOT(ConfigurationSection config)
	{
		super(config);
		condition = (Condition<T>) Condition.load(config.getConfigurationSection("condition"));
	}

	public Condition_NOT()
	{
		super();
		condition = new Condition_TRUE<T>();
	}

	@Override
	public void save(FileConfiguration config, String path)
	{
		super.save(config, path);
		condition.save(config, path + "condition.");
	}

	@Override
	public String getTypeIdentifier()
	{
		return "NOT";
	}

	@Override
	public boolean match(T tester)
	{
		return !condition.match(tester);
	}
}
