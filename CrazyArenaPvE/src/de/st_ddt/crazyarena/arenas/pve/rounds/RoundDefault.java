package de.st_ddt.crazyarena.arenas.pve.rounds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.arenas.pve.PvEArena;
import de.st_ddt.crazyarena.participants.ParticipantType;
import de.st_ddt.crazyarena.utils.SpawnList;
import de.st_ddt.crazyutil.ExtendedCreatureType;

public class RoundDefault extends AbstractRound
{

	protected final List<ExtendedCreatureType> spawnableMonsterTypes = new ArrayList<ExtendedCreatureType>();
	protected final Random random = new Random();
	protected final long delayStart;
	protected final long maxRoundTime;
	protected final boolean fastFinish;

	public RoundDefault(final PvEArena arena, final int priority, final int offset, final int interval, final boolean repeat, final boolean clearAfter, final long delayStart, final long maxRoundTime, final boolean fastFinish, final Collection<? extends ExtendedCreatureType> monsters)
	{
		super(arena, priority, offset, interval, repeat, clearAfter);
		this.delayStart = delayStart;
		this.maxRoundTime = maxRoundTime;
		this.fastFinish = fastFinish;
		this.spawnableMonsterTypes.addAll(monsters);
	}

	public RoundDefault(final PvEArena arena, final int priority, final int offset, final int interval, final boolean repeat, final boolean clearAfter, final long delayStart, final long maxRoundTime, final boolean fastFinish, final ExtendedCreatureType... monsters)
	{
		super(arena, priority, offset, interval, repeat, clearAfter);
		this.delayStart = delayStart;
		this.maxRoundTime = maxRoundTime;
		this.fastFinish = fastFinish;
		for (final ExtendedCreatureType type : monsters)
			this.spawnableMonsterTypes.add(type);
	}

	public RoundDefault(final PvEArena arena, final ConfigurationSection config)
	{
		super(arena, config);
		this.delayStart = Math.max(0, config.getLong("delayStart", 200));
		this.maxRoundTime = Math.max(-1, config.getLong("maxRoundTime", -1));
		this.fastFinish = config.getBoolean("fastFinish", true);
	}

	@Override
	public String getType()
	{
		return "Default";
	}

	@Override
	public void activate(final int round)
	{
		final SpawnList spawns = arena.getActiveMonsterSpawns();
		final Set<Player> players = arena.getParticipatingPlayers(ParticipantType.PARTICIPANT);
		int anz = getMonsterCount(round);
		while (anz-- > 0)
		{
			final Creature entity = (Creature) spawnableMonsterTypes.get(random.nextInt(spawnableMonsterTypes.size())).spawn(spawns.randomSpawn());
			entity.setTarget(findNearest(entity.getLocation(), players));
			entities.add(entity);
		}
	}

	public LivingEntity findNearest(final Location location, final Collection<? extends LivingEntity> targets)
	{
		double dist = Double.MAX_VALUE;
		LivingEntity res = null;
		for (final LivingEntity entity : targets)
		{
			final double temp = entity.getLocation().distance(location);
			if (temp < dist)
			{
				res = entity;
				dist = temp;
			}
		}
		return res;
	}

	public int getMonsterCount(final int round)
	{
		return 3 * round + 5;
	}
}
