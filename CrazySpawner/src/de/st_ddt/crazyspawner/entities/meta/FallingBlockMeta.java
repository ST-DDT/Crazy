package de.st_ddt.crazyspawner.entities.meta;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.metadata.MetadataValue;

public interface FallingBlockMeta extends MetadataValue
{

	public final static String METAHEADER = "FallingBlockMeta";

	public boolean isDespawningOnImpactEnabled();

	public Material getPlacedMaterial();

	public Byte getPlacedMaterialData();

	public void apply(Block block);
}
