package de.st_ddt.crazyutil;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public interface ExtendedCreatureType extends Named
{

	public EntityType getType();

	public Entity spawn(Location location);

	public Collection<? extends Entity> getEntities(World world);
}
