package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.meta.DetectionMeta;
import de.st_ddt.crazyutil.paramitrisable.DoubleParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class DetectionProperty extends MetadataProperty implements DetectionMeta
{

	/**
	 * The maximum range an enemy is detected/seen with the entity's own eyes.<br>
	 * -1 = default
	 */
	protected final double viewRange;
	/**
	 * The maximum angle an enemy is detected/seen with the entity's own eyes.<br>
	 * -1 = default<br>
	 * PI = sees everything within range.
	 */
	protected final double viewAngle;

	/**
	 * The maximum range an enemy is detected/seen with the entity's own ears,<br>
	 * if emiting a noise level of exactly 1. <br>
	 * Noise level is dependend on what the entity moves.<br>
	 * Sneaking halves the noise level.<br>
	 * Sprinting doubles the noise level.<br>
	 * Flying entities do not make any movement sounds.<br>
	 * -1 = default
	 */
	// protected final double noiseRange;
	public DetectionProperty()
	{
		super();
		this.viewRange = -1;
		this.viewAngle = -1;
	}

	public DetectionProperty(final ConfigurationSection config)
	{
		super(config);
		this.viewRange = getSecureValue(config.getDouble("viewRange", config.getDouble("detectionRange", -1)));
		this.viewAngle = getSecureValue(Math.min(config.getDouble("viewAngle") / ANGLECONVERTER, Math.PI));
	}

	public DetectionProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final DoubleParamitrisable viewRangeParam = (DoubleParamitrisable) params.get("viewrange");
		this.viewRange = getSecureValue(viewRangeParam.getValue());
		final DoubleParamitrisable viewAngleParam = (DoubleParamitrisable) params.get("viewangle");
		this.viewAngle = getSecureValue(Math.min(viewAngleParam.getValue() / ANGLECONVERTER, Math.PI));
	}

	@Override
	public boolean isApplicable(final Class<? extends Entity> clazz)
	{
		return LivingEntity.class.isAssignableFrom(clazz);
	}

	@Override
	public void apply(final Entity entity)
	{
		final Creature creature = (Creature) entity;
		if (viewRange != -1 || viewAngle != -1)
			creature.setMetadata(DetectionMeta.METAHEADER, this);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final DoubleParamitrisable viewRangeParam = new DoubleParamitrisable(viewRange);
		params.put("vr", viewRangeParam);
		params.put("viewrange", viewRangeParam);
		final DoubleParamitrisable viewAngleParam = new DoubleParamitrisable(viewAngle * ANGLECONVERTER);
		params.put("va", viewAngleParam);
		params.put("viewangle", viewAngleParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "viewRange", viewRange);
		config.set(path + "viewAngle", viewAngle * ANGLECONVERTER);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "viewRange", "double (-1=default)");
		config.set(path + "viewAngle", "double (0.0 - " + Math.PI + "; -1=default)");
	}

	@Override
	public double getViewRange()
	{
		return viewRange;
	}

	@Override
	public double getViewAngle()
	{
		return viewAngle;
	}

	@Override
	public double getViewAngleDegree()
	{
		if (viewAngle == -1)
			return -1;
		else
			return viewAngle * ANGLECONVERTER;
	}

	@Override
	public boolean canDetect(final LivingEntity entity, final Entity target)
	{
		final Location eLoc = entity.getLocation();
		final Location tLoc = target.getLocation();
		if (eLoc.getWorld() != tLoc.getWorld())
			return false;
		if (eLoc.distance(tLoc) > viewRange)
			return false;
		final float angle = entity.getEyeLocation().getDirection().angle(tLoc.subtract(eLoc).toVector());
		return angle <= viewAngle;
	}

	@Override
	@Localized({ "CRAZYSPAWNER.ENTITY.PROPERTY.DETECTION.VIEWRANGE $ViewRange$", "CRAZYSPAWNER.ENTITY.PROPERTY.DETECTION.VIEWANGLE $ViewAngleRadians$ $ViewAngleDegree$" })
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.DETECTION.VIEWRANGE", target, viewRange == -1 ? "Default" : viewRange);
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.DETECTION.VIEWANGLE", target, viewAngle == -1 ? "Default" : viewAngle, viewAngle == -1 ? "Default" : getViewAngleDegree());
	}

	@Override
	public boolean equalsDefault()
	{
		return viewRange == -1 && viewAngle == -1;
	}
}
