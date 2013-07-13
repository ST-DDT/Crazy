package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.DyeColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.material.Colorable;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.EnumParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public final class ColorableProperty extends BasicProperty
{

	protected final DyeColor color;

	public ColorableProperty()
	{
		super();
		this.color = null;
	}

	public ColorableProperty(final ConfigurationSection config)
	{
		super(config);
		final String colorName = config.getString("color");
		if (colorName == null)
			this.color = null;
		else
		{
			DyeColor color = null;
			try
			{
				color = DyeColor.valueOf(colorName);
			}
			catch (final Exception e)
			{
				System.err.println(config.getName() + "'s color " + colorName + " was corrupted/invalid and has been removed!");
			}
			this.color = color;
		}
	}

	@SuppressWarnings("unchecked")
	public ColorableProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final EnumParamitrisable<DyeColor> colorParam = (EnumParamitrisable<DyeColor>) params.get("color");
		this.color = colorParam.getValue();
	}

	@Override
	public void apply(final Entity entity)
	{
		final Colorable colorable = (Colorable) entity;
		if (color != null)
			colorable.setColor(color);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final EnumParamitrisable<DyeColor> colorParam = new EnumParamitrisable<DyeColor>("DyeColor", color, DyeColor.values());
		params.put("c", colorParam);
		params.put("color", colorParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		if (color == null)
			config.set(path + "color", null);
		else
			config.set(path + "color", color.name());
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "color", "DyeColor");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.COLOR $Color$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.COLOR", target, color == null ? "Default" : color.name());
	}

	@Override
	public boolean equalsDefault()
	{
		return color == null;
	}
}
