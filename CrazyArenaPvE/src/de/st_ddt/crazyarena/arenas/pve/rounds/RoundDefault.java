package de.st_ddt.crazyarena.arenas.pve.rounds;

import java.util.ArrayList;

import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;

import de.st_ddt.crazyarena.arenas.pve.PvEArena;
import de.st_ddt.crazyarena.participants.ParticipantType;
import de.st_ddt.crazyarena.utils.SpawnList;

public class RoundDefault extends Round
{

	protected final ArrayList<EntityType> monsters = new ArrayList<EntityType>();

	public RoundDefault(final PvEArena arena, final int offset, final int interval, final boolean repeat)
	{
		super(arena, interval, interval, repeat);
	}

	public RoundDefault(final PvEArena arena, final int offset, final int interval, final boolean repeat, final int priority, final ArrayList<EntityType> monsters)
	{
		super(arena, priority, priority, repeat, priority);
		for (final EntityType type : monsters)
			this.monsters.add(type);
	}

	@Override
	public void activate(final int round)
	{
		final SpawnList spawns = arena.getActiveMonsterSpawns();
		final ParticipantList participants = arena.getParticipants(ParticipantType.PARTICIPANT);
		int anz = getMonsterCount(round);
		final int sanz = spawns.size();
		final int manz = monsters.size();
		while (anz-- > 0)
		{
			final int a = (int) (Math.random() * manz);
			final int b = (int) (Math.random() * sanz);
			final Creature entity = (Creature) arena.getWorld().spawnCreature(spawns.get(b), monsters.get(a));
			entity.setTarget(participants.findNearest(entity.getLocation()).getPlayer());
			arena.addEnemy(entity);
		}
	}

	@Override
	public int getMonsterCount(final int round)
	{
		return 3 * round + 5;
	}
}
