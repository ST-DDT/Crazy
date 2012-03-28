package de.st_ddt.crazyarena.utils;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Wolf;

public enum Creatures {

	// Default creatures
	
	ENDERDRAGON(EntityType.ENDER_DRAGON),ENDERDRAGONS(EntityType.ENDER_DRAGON),
	SKELETON(EntityType.SKELETON), SKELETONS(EntityType.SKELETON), 
	SPIDER(EntityType.SPIDER), SPIDERS(EntityType.SPIDER),
	CAVESPIDER(EntityType.CAVE_SPIDER), CAVESPIDERS(EntityType.CAVE_SPIDER),

	BLAZE(EntityType.BLAZE), BLAZES(EntityType.BLAZE),
	CREEPER(EntityType.CREEPER), CREEPERS(EntityType.CREEPER), 
	WOLF(EntityType.WOLF), WOLVES(EntityType.WOLF),
	
	ZOMBIE(EntityType.ZOMBIE), ZOMBIES(EntityType.ZOMBIE), 
		
	SNOWMAN(EntityType.SNOWMAN),SNOWMEN(EntityType.SNOWMAN),

	// Special creatures
	ZOMBIEPIGMAN(EntityType.PIG_ZOMBIE), ZOMBIEPIGMEN(EntityType.PIG_ZOMBIE), 
	POWEREDCREEPER(EntityType.CREEPER), POWEREDCREEPERS(EntityType.CREEPER), 
	ANGRYWOLF(EntityType.WOLF), ANGRYWOLVES(EntityType.WOLF),
	// HUMAN(EntityType.MONSTER), HUMANS(EntityType.MONSTER), <--
	// InstantiationException
	HUMAN(EntityType.ZOMBIE), HUMANS(EntityType.ZOMBIE), 
	GIANT(EntityType.GIANT), GIANTS(EntityType.GIANT), 
	GHAST(EntityType.GHAST), GHASTS(EntityType.GHAST), 
	ENDERMAN(EntityType.ENDERMAN), ENDERMEN(EntityType.ENDERMAN), 
	
	SILVERFISH(EntityType.SILVERFISH),

	// Passive creatures
	CHICKEN(EntityType.CHICKEN), CHICKENS(EntityType.CHICKEN), 
	COW(EntityType.COW), COWS(EntityType.COW), 
	PIG(EntityType.PIG), PIGS(EntityType.PIG), 
	SHEEP(EntityType.SHEEP), 
	SQUID(EntityType.SQUID), SQUIDS(EntityType.SQUID),

	// Slimes
	SLIME(EntityType.SLIME), SLIMES(EntityType.SLIME), 
	SLIMETINY(EntityType.SLIME), SLIMESTINY(EntityType.SLIME), 
	SLIMESMALL(EntityType.SLIME), SLIMESSMALL(EntityType.SLIME), 
	SLIMEBIG(EntityType.SLIME), SLIMESBIG(EntityType.SLIME), 
	SLIMEHUGE(EntityType.SLIME), SLIMESHUGE(EntityType.SLIME),
	
	// MagmaSlimes
	MAGMASLIME(EntityType.MAGMA_CUBE), MAGMASLIMES(EntityType.MAGMA_CUBE), 
	MAGMASLIMETINY(EntityType.MAGMA_CUBE), MAGMASLIMESTINY(EntityType.MAGMA_CUBE), 
	MAGMASLIMESMALL(EntityType.MAGMA_CUBE), MAGMASLIMESSMALL(EntityType.MAGMA_CUBE), 
	MAGMASLIMEBIG(EntityType.MAGMA_CUBE), MAGMASLIMESBIG(EntityType.MAGMA_CUBE), 
	MAGMASLIMEHUGE(EntityType.MAGMA_CUBE), MAGMASLIMESHUGE(EntityType.MAGMA_CUBE)
	;

	private EntityType type;

	private Creatures(EntityType type)
	{
		this.type = type;
	}

	public LivingEntity spawn(Location location)
	{
		LivingEntity e = location.getWorld().spawnCreature(location, type);
		
		switch (this)
		{
		case POWEREDCREEPERS:
			((Creeper) e).setPowered(true);
			break;
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
		}

		return e;
	}

}
