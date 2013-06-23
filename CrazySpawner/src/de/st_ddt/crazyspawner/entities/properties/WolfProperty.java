package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.DyeColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Wolf;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.EnumParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public final class WolfProperty extends BasicProperty
{

	protected final DyeColor color;
	protected final boolean angry;
	protected final boolean sitting;

	public WolfProperty()
	{
		super();
		this.color = null;
		this.angry = false;
		this.sitting = false;
	}

	public WolfProperty(final ConfigurationSection config)
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
		this.angry = config.getBoolean("angry", false);
		this.sitting = config.getBoolean("sitting", false);
	}

	@SuppressWarnings("unchecked")
	public WolfProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final EnumParamitrisable<DyeColor> colorParam = (EnumParamitrisable<DyeColor>) params.get("color");
		this.color = colorParam.getValue();
		final BooleanParamitrisable angryParam = (BooleanParamitrisable) params.get("angry");
		this.angry = angryParam.getValue();
		final BooleanParamitrisable sittingParam = (BooleanParamitrisable) params.get("sitting");
		this.sitting = sittingParam.getValue();
	}

	@Override
	public void apply(final Entity entity)
	{
		final Wolf wolf = (Wolf) entity;
		if (color != null)
			wolf.setCollarColor(color);
		wolf.setAngry(angry);
		wolf.setSitting(sitting);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params)
	{
		final EnumParamitrisable<DyeColor> colorParam = new EnumParamitrisable<DyeColor>("DyeColor", color, DyeColor.values());
		params.put("c", colorParam);
		params.put("color", colorParam);
		final BooleanParamitrisable angryParam = new BooleanParamitrisable(angry);
		params.put("a", angryParam);
		params.put("angry", angryParam);
		final BooleanParamitrisable sittingParam = new BooleanParamitrisable(sitting);
		params.put("sit", sittingParam);
		params.put("sitting", sittingParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "color", color.name());
		config.set(path + "angry", angry);
		config.set(path + "sitting", sitting);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "color", "DyeColor");
		config.set(path + "angry", "boolean (true/false)");
		config.set(path + "sitting", "boolean (true/false)");
	}

	@Override
	@Localized({ "CRAZYSPAWNER.ENTITY.PROPERTY.COLOR $Color$", "CRAZYSPAWNER.ENTITY.PROPERTY.ANGRY $Angry$", "CRAZYSPAWNER.ENTITY.PROPERTY.SITTING $Sitting$" })
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.COLOR", target, color.name());
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.ANGRY", target, angry);
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.SITTING", target, sitting);
	}
}
