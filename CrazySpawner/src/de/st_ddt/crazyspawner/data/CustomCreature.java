package de.st_ddt.crazyspawner.data;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;

import de.st_ddt.crazyutil.ConfigurationSaveable;
import de.st_ddt.crazyutil.ExtendedCreatureType;

public interface CustomCreature extends ExtendedCreatureType, ConfigurationSaveable
{

	@Override
	public String getName();

	@Override
	public EntityType getType();

	@Override
	public Entity spawn(Location location);

	@Override
	public Collection<? extends Entity> getEntities(World world);

	@Override
	public void save(ConfigurationSection config, String path);

	public interface CustomDrops
	{

		public static final String METAHEADER = "DropsMeta";

		public Collection<ItemStack> getDrops();

		public <S extends Collection<ItemStack>> S updateDrops(final S collection);
	}

	public interface CustomCreatureMeta extends MetadataValue
	{

		public static final String METAHEADER = "CustomCreatureMeta";

		@Override
		public CustomCreature value();
	}
}
