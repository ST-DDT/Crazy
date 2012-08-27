package de.st_ddt.crazyarena.utils;

import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Wolf;

public enum Creatures
{
	// Default creatures
	ZOMBIE(EntityType.ZOMBIE), ZOMBIES(EntityType.ZOMBIE), CREEPER(EntityType.CREEPER), CREEPERS(EntityType.CREEPER), SKELETON(EntityType.SKELETON), SKELETONS(EntityType.SKELETON), SPIDER(EntityType.SPIDER), SPIDERS(EntityType.SPIDER), CAVESPIDER(EntityType.CAVE_SPIDER), CAVESPIDERS(EntityType.CAVE_SPIDER), SILVERFISH(EntityType.SILVERFISH),
	// Nether creatures
	BLAZE(EntityType.BLAZE), BLAZES(EntityType.BLAZE), GHAST(EntityType.GHAST), GHASTS(EntityType.GHAST),
	// The End creatures
	ENDERDRAGON(EntityType.ENDER_DRAGON), ENDERDRAGONS(EntityType.ENDER_DRAGON), ENDERMAN(EntityType.ENDERMAN), ENDERMEN(EntityType.ENDERMAN),
	// Tameable
	WOLF(EntityType.WOLF), WOLVES(EntityType.WOLF), CAT(EntityType.OCELOT), CATS(EntityType.OCELOT),
	// Golems
	SNOWMAN(EntityType.SNOWMAN), SNOWMEN(EntityType.SNOWMAN), GOLEM(EntityType.IRON_GOLEM), GOLEMS(EntityType.IRON_GOLEM), IRON_GOLEM(EntityType.IRON_GOLEM), IRON_GOLEMS(EntityType.IRON_GOLEM),
	// Special creatures
	ZOMBIEPIGMAN(EntityType.PIG_ZOMBIE), ZOMBIEPIGMEN(EntityType.PIG_ZOMBIE), POWEREDCREEPER(EntityType.CREEPER), POWEREDCREEPERS(EntityType.CREEPER), ANGRYWOLF(EntityType.WOLF), ANGRYWOLVES(EntityType.WOLF), GIANT(EntityType.GIANT), GIANTS(EntityType.GIANT),
	// Passive creatures
	CHICKEN(EntityType.CHICKEN), CHICKENS(EntityType.CHICKEN), COW(EntityType.COW), COWS(EntityType.COW), PIG(EntityType.PIG), PIGS(EntityType.PIG), SHEEP(EntityType.SHEEP), SQUID(EntityType.SQUID), SQUIDS(EntityType.SQUID),
	// Slimes
	SLIME(EntityType.SLIME), SLIMES(EntityType.SLIME), SLIMETINY(EntityType.SLIME), SLIMESTINY(EntityType.SLIME), SLIMESMALL(EntityType.SLIME), SLIMESSMALL(EntityType.SLIME), SLIMEBIG(EntityType.SLIME), SLIMESBIG(EntityType.SLIME), SLIMEHUGE(EntityType.SLIME), SLIMESHUGE(EntityType.SLIME),
	// MagmaSlimes
	MAGMASLIME(EntityType.MAGMA_CUBE), MAGMASLIMES(EntityType.MAGMA_CUBE), MAGMASLIMETINY(EntityType.MAGMA_CUBE), MAGMASLIMESTINY(EntityType.MAGMA_CUBE), MAGMASLIMESMALL(EntityType.MAGMA_CUBE), MAGMASLIMESSMALL(EntityType.MAGMA_CUBE), MAGMASLIMEBIG(EntityType.MAGMA_CUBE), MAGMASLIMESBIG(EntityType.MAGMA_CUBE), MAGMASLIMEHUGE(EntityType.MAGMA_CUBE), MAGMASLIMESHUGE(EntityType.MAGMA_CUBE);

	private final EntityType type;

	private Creatures(final EntityType type)
	{
		this.type = type;
	}

	public String getName()
	{
		return type.getName();
	}

	public EntityType getType()
	{
		return type;
	}

	public Entity spawn(final Location location)
	{
		final Entity e = location.getWorld().spawnEntity(location, type);
		switch (this)
		{
			case POWEREDCREEPERS:
				((Creeper) e).setPowered(true);
				break;
			case ANGRYWOLF:
			case ANGRYWOLVES:
				((Wolf) e).setAngry(true);
				break;
			case SLIMETINY:
			case SLIMESTINY:
			case MAGMASLIMETINY:
			case MAGMASLIMESTINY:
				((Slime) e).setSize(1);
				break;
			case SLIMESMALL:
			case SLIMESSMALL:
			case MAGMASLIMESMALL:
			case MAGMASLIMESSMALL:
				((Slime) e).setSize(2);
				break;
			case SLIMEBIG:
			case SLIMESBIG:
			case MAGMASLIMEBIG:
			case MAGMASLIMESBIG:
				((Slime) e).setSize(3);
				break;
			case SLIMEHUGE:
			case SLIMESHUGE:
			case MAGMASLIMEHUGE:
			case MAGMASLIMESHUGE:
				((Slime) e).setSize(4);
				break;
			case CAT:
			case CATS:
				((Ocelot) e).setTamed(true);
			default:
		}
		return e;
	}
}
