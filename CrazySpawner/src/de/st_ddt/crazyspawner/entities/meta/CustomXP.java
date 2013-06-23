package de.st_ddt.crazyspawner.entities.meta;

import org.bukkit.metadata.MetadataValue;

public interface CustomXP extends MetadataValue
{

	public static final String METAHEADER = "XPMeta";

	public int getMinXP();

	public int getMaxXP();

	public int getXP();
}
