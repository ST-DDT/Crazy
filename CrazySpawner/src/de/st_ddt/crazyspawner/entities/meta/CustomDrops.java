package de.st_ddt.crazyspawner.entities.meta;

import java.util.Collection;

import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;

public interface CustomDrops extends MetadataValue
{

	public static final String METAHEADER = "DropsMeta";

	public Collection<ItemStack> getDrops();

	public <S extends Collection<ItemStack>> S updateDrops(final S collection);
}
