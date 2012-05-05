package de.st_ddt.crazyarena.rounds;

import java.util.ArrayList;

import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import de.st_ddt.crazyarena.participants.ParticipantList;
import de.st_ddt.crazyarena.participants.ParticipantType;
import de.st_ddt.crazyarena.pve.ArenaPvE;
import de.st_ddt.crazyarena.utils.SpawnList;

public class RoundDefault extends Round
{

	protected final ArrayList<EntityType> monsters = new ArrayList<EntityType>();

	public RoundDefault(ArenaPvE arena, int offset, int interval, boolean repeat)
	{
		super(arena, interval, interval, repeat);
	}

	public RoundDefault(ArenaPvE arena, int offset, int interval, boolean repeat, int priority, ArrayList<EntityType> monsters)
	{
		super(arena, priority, priority, repeat, priority);
		for (EntityType type : monsters)
			this.monsters.add(type);
	}

	@Override
	public void activate(int round)
	{
		SpawnList spawns = arena.getActiveMonsterSpawns();
		ParticipantList participants = arena.getParticipants(ParticipantType.PARTICIPANT);
		int anz = getMonsterCount(round);
		int sanz = spawns.size();
		int manz = monsters.size();
		while (anz-- > 0)
		{
			int a = (int) (Math.random() * manz);
			int b = (int) (Math.random() * sanz);
			Creature entity = (Creature) arena.getWorld().spawnCreature(spawns.get(b), monsters.get(a));
			entity.setTarget(participants.findNearest(entity.getLocation()).getPlayer());
			arena.addEnemy(entity);
		}
	}

	@Override
	public int getMonsterCount(int round)
	{
		return 3 * round + 5;
	}
}
