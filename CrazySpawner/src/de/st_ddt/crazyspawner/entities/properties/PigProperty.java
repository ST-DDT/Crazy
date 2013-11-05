package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pig;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class PigProperty extends BasicProperty
{

	protected final boolean saddle;

	public PigProperty()
	{
		super();
		this.saddle = false;
	}

	public PigProperty(final ConfigurationSection config)
	{
		super(config);
		this.saddle = config.getBoolean("saddle", false);
	}

	public PigProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final BooleanParamitrisable saddleParam = (BooleanParamitrisable) params.get("saddle");
		this.saddle = saddleParam.getValue();
	}

	@Override
	public boolean isApplicable(final Class<? extends Entity> clazz)
	{
		return Pig.class.isAssignableFrom(clazz);
	}

	@Override
	public void apply(final Entity entity)
	{
		final Pig pig = (Pig) entity;
		pig.setSaddle(saddle);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final BooleanParamitrisable saddleParam = new BooleanParamitrisable(saddle);
		params.put("saddle", saddleParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "saddle", saddle);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "saddle", "boolean (true/false)");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.SADDLE $Saddle$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.SADDLE", target, saddle);
	}

	@Override
	public boolean equalsDefault()
	{
		return saddle == false;
	}
}
