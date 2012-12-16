package de.st_ddt.crazychats.channels;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.participants.ParticipantType;

public class ArenaSpectatorChannel extends ArenaChannel
{

	public ArenaSpectatorChannel(final CrazyArena plugin, final Arena<?> arena)
	{
		super(plugin, arena, true);
		aliases.add("spec");
		aliases.add("specs");
		aliases.add("spectator");
		aliases.add("spectators");
	}

	@Override
	public Set<Player> getTargets(final Player player)
	{
		return arena.getParticipatingPlayers(ParticipantType.SPECTATOR);
	}

	@Override
	public String getFormat(final Player player)
	{
		return StringUtils.replace(plugin.getArenaSpectatorChatFormat(), "$A0$", arena.getParticipant(player).getParticipantType().toString());
	}
}
