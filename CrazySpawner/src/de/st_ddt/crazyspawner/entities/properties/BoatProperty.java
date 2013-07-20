package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;

import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.DoubleParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;

public class BoatProperty extends BasicProperty
{

	protected final double occupiedDeceleration;
	protected final double unoccupiedDeceleration;
	protected final boolean workOnLand;

	public BoatProperty()
	{
		super();
		this.occupiedDeceleration = -1;
		this.unoccupiedDeceleration = -1;
		this.workOnLand = false;
	}

	public BoatProperty(final ConfigurationSection config)
	{
		super(config);
		this.occupiedDeceleration = config.getDouble("occupiedDeceleration", -1);
		this.unoccupiedDeceleration = config.getDouble("unoccupiedDeceleration", -1);
		this.workOnLand = config.getBoolean("workOnLand", false);
	}

	public BoatProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final DoubleParamitrisable occupiedDecelerationParam = (DoubleParamitrisable) params.get("occupieddeceleration");
		this.occupiedDeceleration = occupiedDecelerationParam.getValue();
		final DoubleParamitrisable unoccupiedDecelerationParam = (DoubleParamitrisable) params.get("unoccupieddeceleration");
		this.unoccupiedDeceleration = unoccupiedDecelerationParam.getValue();
		final BooleanParamitrisable workOnLandParam = (BooleanParamitrisable) params.get("workonland");
		this.workOnLand = workOnLandParam.getValue();
	}

	@Override
	public void apply(final Entity entity)
	{
		final Boat boat = (Boat) entity;
		if (occupiedDeceleration != -1)
			boat.setOccupiedDeceleration(occupiedDeceleration);
		if (unoccupiedDeceleration != -1)
			boat.setUnoccupiedDeceleration(unoccupiedDeceleration);
		boat.setWorkOnLand(workOnLand);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final DoubleParamitrisable occupiedDecelerationParam = new DoubleParamitrisable(occupiedDeceleration);
		params.put("od", occupiedDecelerationParam);
		params.put("odecel", occupiedDecelerationParam);
		params.put("odeceleceleration", occupiedDecelerationParam);
		params.put("occupieddeceleration", occupiedDecelerationParam);
		final DoubleParamitrisable unoccupiedDecelerationParam = new DoubleParamitrisable(unoccupiedDeceleration);
		params.put("ud", unoccupiedDecelerationParam);
		params.put("udecel", unoccupiedDecelerationParam);
		params.put("udeceleceleration", unoccupiedDecelerationParam);
		params.put("unoccupieddeceleration", unoccupiedDecelerationParam);
		final BooleanParamitrisable workOnLandParam = new BooleanParamitrisable(workOnLand);
		params.put("wol", workOnLandParam);
		params.put("workonland", workOnLandParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set("occupiedDeceleration", occupiedDeceleration);
		config.set("unoccupiedDeceleration", unoccupiedDeceleration);
		config.set("workOnLand", workOnLand);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set("occupiedDeceleration", "double (1.0 - x.y; -1 = default");
		config.set("unoccupiedDeceleration", "double (1.0 - x.y; -1 = default");
		config.set("workOnLand", "boolean (true/false)");
	}

	@Override
	public void show(final CommandSender target)
	{
		// EDIT Implementiere BasicProperty.show()
	}

	@Override
	public boolean equalsDefault()
	{
		return false;
	}
}
