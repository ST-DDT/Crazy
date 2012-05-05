package de.st_ddt.crazyarena.participants;

import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.classes.ParticipantClass;
import de.st_ddt.crazyarena.utils.PlayerSaver;

public class ParticipantPvE extends Participant
{

	protected final PlayerSaver saver;
	protected ParticipantClass participantClass = null;

	public ParticipantPvE(Player player, Arena arena)
	{
		super(player, arena);
		this.saver = new PlayerSaver(player);
	}

	public PlayerSaver getSaver()
	{
		return saver;
	}
	
	public void backup()
	{
		saver.backup();
	}
	
	public void restore()
	{
		saver.restore();
	}

	public ParticipantClass getParticipantClass()
	{
		return participantClass;
	}

	public void setParticipantClass(ParticipantClass participantClass)
	{
		this.participantClass = participantClass;
		participantClass.activate(player);
	}

	public void giveRewards()
	{
		//rewards
	}
	
}
