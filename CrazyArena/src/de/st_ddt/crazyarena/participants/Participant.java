package de.st_ddt.crazyarena.participants;

import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.arenas.Arena;

public class Participant
{

	protected final Player player;
	protected final Arena arena;
	protected ParticipantType participantType = ParticipantType.NONE;

	public Participant(Player player, Arena arena)
	{
		super();
		this.player = player;
		this.arena = arena;
	}

	public final Player getPlayer()
	{
		return player;
	}

	public final Arena getArena()
	{
		return arena;
	}

	public ParticipantType getParticipantType()
	{
		return participantType;
	}

	public void setParticipantType(ParticipantType type)
	{
		this.participantType = type;
	}
}
