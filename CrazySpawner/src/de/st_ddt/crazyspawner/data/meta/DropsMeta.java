package de.st_ddt.crazyspawner.data.meta;

import java.util.Collection;

import org.bukkit.inventory.ItemStack;

import de.st_ddt.crazyspawner.data.CustomCreature;

public final class DropsMeta extends CustomCreatureMeta
{

	public static final String METAHEADER = "DropsMeta";

	public DropsMeta(final CustomCreature customCreature)
	{
		super(customCreature);
	}

	public Collection<ItemStack> getDrops()
	{
		return customCreature.getDrops();
	}

	public <S extends Collection<ItemStack>> S updateDrops(final S collection)
	{
		return customCreature.updateDrops(collection);
	}
}
