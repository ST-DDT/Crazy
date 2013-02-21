package de.st_ddt.crazyarena.participants.pve;

import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.arenas.pve.PvEArena;
import de.st_ddt.crazyarena.classes.ParticipantClass;
import de.st_ddt.crazyarena.participants.Participant;
import de.st_ddt.crazyutil.PlayerSaver;

public class PvEParticipant extends Participant<PvEParticipant, PvEArena>
{

	protected final PlayerSaver saver;
	protected ParticipantClass participantClass = null;

	public PvEParticipant(final Player player, final PvEArena arena)
	{
		super(player, arena);
		this.saver = new PlayerSaver(player);
	}
}
