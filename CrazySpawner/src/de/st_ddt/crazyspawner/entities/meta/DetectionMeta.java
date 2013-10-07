package de.st_ddt.crazyspawner.entities.meta;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.MetadataValue;

public interface DetectionMeta extends MetadataValue
{

	public static final String METAHEADER = "DetectionMeta";
	public static final double ANGLECONVERTER = 180 / Math.PI;

	/**
	 * @return The view range of this {@link LivingEntity} in blocks.<br>
	 *         -1 = default
	 */
	public double getViewRange();

	/**
	 * @return The view angle (offset) from the line of view. (Radians)<br>
	 *         -1 = default<br>
	 *         PI = Can see everthing.
	 */
	public double getViewAngle();

	/**
	 * @return The view angle (offset) from the line of view. (Degree)<br>
	 *         -1 = default<br>
	 *         180 = Can see everthing.
	 */
	public double getViewAngleDegree();

	/**
	 * Checks whether the {@link LivingEntity} can detect the given {@link Entity}
	 * 
	 * @param entity
	 *            The entity whose view will be checked.
	 * @param target
	 *            The entity which should be checked whether it can be seeen.
	 * @return True, if the living entity can see the target entity, False otherwise.
	 */
	public boolean canDetect(LivingEntity entity, Entity target);
}
