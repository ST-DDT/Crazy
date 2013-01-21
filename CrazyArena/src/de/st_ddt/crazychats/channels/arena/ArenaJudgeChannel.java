package de.st_ddt.crazychats.channels.arena;

import java.util.Set;

import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.participants.ParticipantType;

public class ArenaJudgeChannel extends ArenaChannel
{

	public ArenaJudgeChannel(final CrazyArena plugin, final Arena<?> arena)
	{
		super(plugin, arena, true);
		aliases.add("judge");
		aliases.add("judges");
	}

	@Override
	public Set<Player> getTargets(final Player player)
	{
		return arena.getParticipatingPlayers(ParticipantType.JUDGE);
	}

	@Override
	public String getFormat(final Player player)
	{
		return plugin.getArenaJudgeChatFormat();
	}

	@Override
	public String toString()
	{
		return arena.getType() + "ArenaJudgeChatChannel (Arena: " + arena.getName() + ")";
	}
}
