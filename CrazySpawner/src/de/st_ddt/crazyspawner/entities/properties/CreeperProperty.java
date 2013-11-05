package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class CreeperProperty extends BasicProperty
{

	protected final Boolean powered;

	public CreeperProperty()
	{
		super();
		this.powered = null;
	}

	public CreeperProperty(final ConfigurationSection config)
	{
		super(config);
		if (config.getBoolean("powered", false))
			this.powered = false;
		else if (!config.getBoolean("powered", true))
			this.powered = false;
		else
			this.powered = null;
	}

	public CreeperProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final BooleanParamitrisable poweredParam = (BooleanParamitrisable) params.get("powered");
		this.powered = poweredParam.getValue();
	}

	@Override
	public boolean isApplicable(final Class<? extends Entity> clazz)
	{
		return Creeper.class.isAssignableFrom(clazz);
	}

	@Override
	public void apply(final Entity entity)
	{
		final Creeper creeper = (Creeper) entity;
		if (powered != null)
			creeper.setPowered(powered);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final BooleanParamitrisable poweredParam = new BooleanParamitrisable(powered);
		params.put("powered", poweredParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "powered", powered);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "powered", "Boolean (true/false/default)");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.POWERED $Powered$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.POWERED", target, powered == null ? "Default" : powered);
	}

	@Override
	public boolean equalsDefault()
	{
		return powered == null;
	}
}
