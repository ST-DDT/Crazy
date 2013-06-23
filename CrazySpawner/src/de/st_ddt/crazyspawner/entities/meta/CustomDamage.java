package de.st_ddt.crazyspawner.entities.meta;

import org.bukkit.metadata.MetadataValue;

public interface CustomDamage extends MetadataValue
{

	public static final String METAHEADER = "DamageMeta";

	public int getMinDamage();

	public int getMaxDamage();

	public int getDamage();
}
