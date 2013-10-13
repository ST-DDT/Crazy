package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.util.Vector;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.DoubleParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.VectorParamistrisable;
import de.st_ddt.crazyutil.source.Localized;

public class MinecartProperty extends BasicProperty
{

	protected final double topSpeed;
	protected final Boolean slowWhenEmpty;
	protected final Vector flyingVelocity;
	protected final Vector derailedVelocity;

	public MinecartProperty()
	{
		super();
		this.topSpeed = -1;
		this.slowWhenEmpty = null;
		this.flyingVelocity = null;
		this.derailedVelocity = null;
	}

	public MinecartProperty(final double topSpeed, final Boolean slowWhenEmpty, final Vector flyingVelocity, final Vector derailedVelocity)
	{
		super();
		this.topSpeed = getSecureValue(topSpeed);
		this.slowWhenEmpty = slowWhenEmpty;
		this.flyingVelocity = flyingVelocity;
		this.derailedVelocity = derailedVelocity;
	}

	public MinecartProperty(final ConfigurationSection config)
	{
		super(config);
		this.topSpeed = getSecureValue(config.getDouble("topSpeed", -1));
		if (config.getBoolean("slowWhenEmpty", false))
			this.slowWhenEmpty = false;
		else if (!config.getBoolean("slowWhenEmpty", true))
			this.slowWhenEmpty = false;
		else
			this.slowWhenEmpty = null;
		this.flyingVelocity = config.getVector("flyingVelocity", null);
		this.derailedVelocity = config.getVector("derailedVelocity", null);
		;
	}

	public MinecartProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final DoubleParamitrisable topSpeedParam = (DoubleParamitrisable) params.get("topspeed");
		this.topSpeed = getSecureValue(topSpeedParam.getValue());
		final BooleanParamitrisable slowWhenEmptyParam = (BooleanParamitrisable) params.get("slowwhenempty");
		this.slowWhenEmpty = slowWhenEmptyParam.getValue();
		final VectorParamistrisable flyingVelocityParam = (VectorParamistrisable) params.get("flyingvelocity");
		this.flyingVelocity = flyingVelocityParam.getValue();
		final VectorParamistrisable derailedVelocityParam = (VectorParamistrisable) params.get("derailedvelocity");
		this.derailedVelocity = derailedVelocityParam.getValue();
	}

	@Override
	public void apply(final Entity entity)
	{
		final Minecart minecart = (Minecart) entity;
		if (topSpeed != -1)
			minecart.setMaxSpeed(topSpeed);
		if (slowWhenEmpty != null)
			minecart.setSlowWhenEmpty(slowWhenEmpty);
		if (flyingVelocity != null)
			minecart.setFlyingVelocityMod(flyingVelocity);
		if (derailedVelocity != null)
			minecart.setDerailedVelocityMod(derailedVelocity);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final DoubleParamitrisable topSpeedParam = new DoubleParamitrisable(topSpeed);
		params.put("ts", topSpeedParam);
		params.put("tops", topSpeedParam);
		params.put("tspeed", topSpeedParam);
		params.put("topspeed", topSpeedParam);
		final BooleanParamitrisable slowWhenEmptyParam = new BooleanParamitrisable(slowWhenEmpty);
		params.put("swe", slowWhenEmptyParam);
		params.put("slowwe", slowWhenEmptyParam);
		params.put("slowwhenempty", slowWhenEmptyParam);
		final VectorParamistrisable flyingVelocityParam = new VectorParamistrisable(flyingVelocity);
		params.put("fv", flyingVelocityParam);
		params.put("flyv", flyingVelocityParam);
		params.put("flyvelo", flyingVelocityParam);
		params.put("flyvelocity", flyingVelocityParam);
		params.put("flyingvelocity", flyingVelocityParam);
		final VectorParamistrisable derailedVelocityParam = new VectorParamistrisable(derailedVelocity);
		params.put("dv", derailedVelocityParam);
		params.put("derv", derailedVelocityParam);
		params.put("dervelo", derailedVelocityParam);
		params.put("dervelocity", derailedVelocityParam);
		params.put("derailedvelocity", derailedVelocityParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set("topSpeed", topSpeed);
		config.set("slowWhenEmpty", slowWhenEmpty);
		config.set("flyingVelocity", flyingVelocity);
		config.set("derailedVelocity", derailedVelocity);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set("topSpeed", "double (0.0 - X.Y; -1 = default)");
		config.set("slowWhenEmpty", "Boolean (true/false/default)");
		config.set("flyingVelocity", "Vector");
		config.set("derailedVelocity", "Vector");
	}

	@Override
	@Localized({ "CRAZYSPAWNER.ENTITY.PROPERTY.TOPSPEED $TopSpeed$", "CRAZYSPAWNER.ENTITY.PROPERTY.SLOWWHENEMPTY $SlowWhenEmpty$", "CRAZYSPAWNER.ENTITY.PROPERTY.FYLINGVELOCITY $FlyingVelocity$", "CRAZYSPAWNER.ENTITY.PROPERTY.DERAILEDVELOCITY $DerailedVelocity$" })
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.TOPSPEED", target, topSpeed == -1 ? "Default" : topSpeed);
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.SLOWWHENEMPTY", target, slowWhenEmpty == null ? "Default" : slowWhenEmpty);
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.FYLINGVELOCITY", target, flyingVelocity == null ? "Default" : flyingVelocity);
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.DERAILEDVELOCITY", target, derailedVelocity == null ? "Default" : derailedVelocity);
	}

	@Override
	public boolean equalsDefault()
	{
		return topSpeed == -1 && slowWhenEmpty == null && flyingVelocity == null && derailedVelocity == null;
	}
}
