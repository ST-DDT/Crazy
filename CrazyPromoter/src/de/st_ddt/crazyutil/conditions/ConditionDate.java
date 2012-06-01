package de.st_ddt.crazyutil.conditions;

import java.util.Date;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyplugin.CrazyPlugin;

public class ConditionDate<T> extends ConditionBase<T>
{

	protected Date date;
	protected boolean before;

	public ConditionDate(Date date, boolean before)
	{
		super();
		this.date = date;
		this.before = before;
	}

	public ConditionDate(ConfigurationSection config)
	{
		super(config);
		try
		{
			this.date = CrazyPlugin.DateFormat.parse(config.getString("date"));
		}
		catch (Exception e)
		{
			this.date = new Date();
		}
		this.before = config.getBoolean("before", true);
	}

	@Override
	public void save(ConfigurationSection config, String path)
	{
		super.save(config, path);
		config.set(path + "date", CrazyPlugin.DateFormat.format(date));
		config.set(path + "before", before);
	}

	@Override
	public String getTypeIdentifier()
	{
		return "Date";
	}

	@Override
	public boolean match(T tester)
	{
		if (before)
			return date.after(new Date());
		else
			return date.before(new Date());
	}
}
