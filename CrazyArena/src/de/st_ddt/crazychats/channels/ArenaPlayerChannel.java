package de.st_ddt.crazychats.channels;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.participants.ParticipantType;

public class ArenaPlayerChannel extends ArenaChannel
{

	public ArenaPlayerChannel(final CrazyArena plugin, final Arena<?> arena)
	{
		super(plugin, arena, true);
		aliases.add("plr");
		aliases.add("player");
		aliases.add("players");
		aliases.add("participant");
		aliases.add("participants");
	}

	@Override
	public Set<Player> getTargets(final Player player)
	{
		return arena.getParticipatingPlayers(ParticipantType.PLAYERTYPES);
	}

	@Override
	public String getFormat(final Player player)
	{
		return StringUtils.replace(plugin.getArenaPlayerChatFormat(), "$A0$", arena.getParticipant(player).getParticipantType().toString());
	}
}
