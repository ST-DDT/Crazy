package de.st_ddt.crazyutil.conditions;

import java.util.Date;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyplugin.CrazyLightPluginInterface;

public class ConditionDate<T> extends ConditionBase<T>
{

	protected Date date;
	protected boolean before;

	public ConditionDate(final Date date, final boolean before)
	{
		super();
		this.date = date;
		this.before = before;
	}

	public ConditionDate(final ConfigurationSection config)
	{
		super(config);
		try
		{
			this.date = CrazyLightPluginInterface.DATETIMEFORMAT.parse(config.getString("date"));
		}
		catch (final Exception e)
		{
			this.date = new Date();
		}
		this.before = config.getBoolean("before", true);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		config.set(path + "date", CrazyLightPluginInterface.DATETIMEFORMAT.format(date));
		config.set(path + "before", before);
	}

	@Override
	public String getTypeIdentifier()
	{
		return "Date";
	}

	@Override
	public boolean match(final T tester)
	{
		if (before)
			return date.after(new Date());
		else
			return date.before(new Date());
	}
}
