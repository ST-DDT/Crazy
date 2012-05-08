package de.st_ddt.crazyarena.participants;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public class ParticipantList extends ArrayList<Participant>
{

	protected final Arena arena;
	private static final long serialVersionUID = 7274877975420209958L;

	public ParticipantList(Arena arena)
	{
		super();
		this.arena = arena;
	}

	public Participant getParticipant(Player player)
	{
		for (Participant participant : this)
			if (participant.getPlayer() == this)
				return participant;
		return null;
	}

	public ParticipantList getParticipants(ParticipantType type)
	{
		ParticipantList list = new ParticipantList(arena);
		for (Participant participant : this)
			if (participant.getParticipantType() == type)
				list.add(participant);
		return list;
	}

	public ArrayList<Player> getPlayerList()
	{
		ArrayList<Player> players = new ArrayList<Player>();
		for (Participant participant : this)
			players.add(participant.getPlayer());
		return players;
	}

	public Player[] getPlayers()
	{
		return getPlayerList().toArray(new Player[this.size()]);
	}

	public Participant findNearest(Location location)
	{
		double dist = Double.MAX_VALUE;
		Participant res = null;
		for (Participant participant : this)
			if (participant.getPlayer().getLocation().distance(location) < dist)
			{
				dist = participant.getPlayer().getLocation().distance(location);
				res = participant;
			}
		return res;
	}

	public ArrayList<CommandSender> getCommandSenderList()
	{
		ArrayList<CommandSender> players = new ArrayList<CommandSender>();
		for (Participant participant : this)
			players.add(participant.getPlayer());
		return players;
	}

	public final void sendLocaleMessage(final String localepath, final Object... args)
	{
		arena.sendLocaleMessage(localepath, getCommandSenderList(), args);
	}

	public final void sendLocaleMessage(final CrazyLocale locale, final Object... args)
	{
		arena.sendLocaleMessage(locale, getCommandSenderList(), args);
	}
}
