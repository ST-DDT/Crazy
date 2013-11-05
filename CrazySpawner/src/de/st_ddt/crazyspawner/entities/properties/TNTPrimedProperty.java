package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.DurationParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.NumberParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class TNTPrimedProperty extends BasicProperty
{

	protected final int fuseTicks;

	public TNTPrimedProperty()
	{
		super();
		this.fuseTicks = -1;
	}

	public TNTPrimedProperty(final ConfigurationSection config)
	{
		super(config);
		this.fuseTicks = getSecureValue(config.getInt("fuseTicks", -1));
	}

	public TNTPrimedProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final NumberParamitrisable<?> fuseTicksParam = (NumberParamitrisable<?>) params.get("fuseticks");
		this.fuseTicks = getSecureValue(fuseTicksParam.getValue().intValue());
	}

	@Override
	public boolean isApplicable(final Class<? extends Entity> clazz)
	{
		return TNTPrimed.class.isAssignableFrom(clazz);
	}

	@Override
	public void apply(final Entity entity)
	{
		final TNTPrimed tnt = (TNTPrimed) entity;
		if (fuseTicks != -1)
			tnt.setFuseTicks(fuseTicks);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final DurationParamitrisable fuseTicksParam = new DurationParamitrisable(fuseTicks);
		params.put("fuse", fuseTicksParam);
		params.put("fusetick", fuseTicksParam);
		params.put("fuseticks", fuseTicksParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "fuseTicks", fuseTicks);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "fuseTicks", "Ticks (-1 = default)");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.TNTPRIMED.FUSETICKS $Ticks$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.TNTPRIMED.FUSETICKS", target, fuseTicks == -1 ? "Default" : fuseTicks);
	}

	@Override
	public boolean equalsDefault()
	{
		return fuseTicks == -1;
	}
}
