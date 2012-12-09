package de.st_ddt.crazyarena.teams;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.exceptions.CrazyArenaExceedingTeamLimitException;
import de.st_ddt.crazyarena.exceptions.CrazyArenaException;
import de.st_ddt.crazyarena.exceptions.CrazyArenaTeamException;
import de.st_ddt.crazyarena.exceptions.CrazyArenaTeamNotEmptyException;
import de.st_ddt.crazyarena.participants.Participant;

public class TeamList<S extends Participant<S, T>, T extends Arena<S>>
{

	protected final ArrayList<Team<S, T>> teams = new ArrayList<Team<S, T>>();
	protected final T arena;
	protected int maxTeams = 0;
	protected int maxTeamSize = 0;
	protected boolean autoColor;

	public TeamList(final T arena)
	{
		this.arena = arena;
	}

	public TeamList(final T arena, final int maxTeams, final int maxTeamSize)
	{
		this(arena);
		this.maxTeams = maxTeams;
		this.maxTeamSize = maxTeamSize;
	}

	public Team<S, T> addTeam(final String name, final int maxTeamSize) throws CrazyArenaException
	{
		if (maxTeams > 0 && maxTeams < teams.size())
			throw new CrazyArenaExceedingTeamLimitException(arena, name, maxTeams);
		final Team<S, T> team = new Team<S, T>(arena, name, maxTeamSize);
		teams.add(team);
		return team;
	}

	public Team<S, T> addTeam(final String name) throws CrazyArenaException
	{
		if (maxTeams > 0 && maxTeams < teams.size())
			throw new CrazyArenaExceedingTeamLimitException(arena, name, maxTeams);
		final Team<S, T> team = new Team<S, T>(arena, name, maxTeamSize);
		teams.add(team);
		return team;
	}

	public Team<S, T> quickjoin(final Player player, final boolean addNew) throws CrazyArenaException
	{
		if (addNew)
		{
			if (maxTeams > 0 || getTeamCount() > maxTeams)
				return quickjoin(player, false);
			final Team<S, T> team = addTeam(player.getName());
			team.add(player);
			return team;
		}
		int min = Integer.MAX_VALUE;
		Team<S, T> minteam = null;
		for (final Team<S, T> team : teams)
			if (team.getMemberCount() < min)
			{
				minteam = team;
				min = team.getMemberCount();
			}
		minteam.add(player);
		return minteam;
	}

	public Team<S, T> getTeam(final String name)
	{
		for (final Team<S, T> team : teams)
			if (team.getName().equalsIgnoreCase(name))
				return team;
		return null;
	}

	public Team<S, T> getTeam(final ChatColor color)
	{
		for (final Team<S, T> team : teams)
			if (team.getColor().equals(color))
				return team;
		return null;
	}

	public Team<S, T> getTeam(final Player player)
	{
		for (final Team<S, T> team : teams)
			if (team.isMember(player))
				return team;
		return null;
	}

	public Team<S, T> getTeam(final int index) throws IndexOutOfBoundsException
	{
		return teams.get(index);
	}

	/**
	 * Löscht alle Teams
	 */
	public void clear()
	{
		teams.clear();
	}

	/**
	 * Leert alle Teams
	 */
	public void clearTeams()
	{
		for (final Team<S, T> team : teams)
			team.clear();
	}

	/**
	 * Löscht leere Teams
	 */
	public void clean()
	{
		final Iterator<Team<S, T>> it = teams.iterator();
		while (it.hasNext())
			if (it.next().getMemberCount() == 0)
				it.remove();
	}

	/**
	 * Prüft ob das Team gelöscht werden kann
	 * 
	 * @throws CrazyArenaTeamNotEmptyException
	 */
	public void clean(final Team<S, T> team) throws CrazyArenaTeamException
	{
		if (team.getMemberCount() == 0)
			teams.remove(team);
		else
			throw new CrazyArenaTeamNotEmptyException(arena, team);
	}

	public int getMaxTeams()
	{
		return maxTeams;
	}

	public void setMaxTeams(final int maxTeams)
	{
		this.maxTeams = maxTeams;
	}

	public T getArena()
	{
		return arena;
	}

	public void setNewMaxTeamSize(final int maxSize, final boolean updateOld)
	{
		this.maxTeamSize = maxSize;
		if (updateOld)
			for (final Team<S, T> team : teams)
				team.setMaxSize(maxSize);
	}

	public int getMaxTeamSize()
	{
		return maxTeamSize;
	}

	public int getTeamCount()
	{
		return teams.size();
	}

	public void setAutoColor(final boolean autoColor)
	{
		this.autoColor = autoColor;
		for (final Team<S, T> team : teams)
			team.setAutoColors(autoColor);
	}
}
