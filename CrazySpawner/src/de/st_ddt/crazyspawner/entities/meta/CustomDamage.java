package de.st_ddt.crazyspawner.entities.meta;

import org.bukkit.metadata.MetadataValue;

public interface CustomDamage extends MetadataValue
{

	public static final String METAHEADER = "DamageMeta";

	public double getMinDamage();

	public double getMaxDamage();

	public double getDamage();
}
