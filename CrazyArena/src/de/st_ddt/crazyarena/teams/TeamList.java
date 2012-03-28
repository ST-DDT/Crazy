package de.st_ddt.crazyarena.teams;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.exceptions.CrazyArenaTeamExceedingTeamLimitException;
import de.st_ddt.crazyarena.exceptions.CrazyArenaTeamException;
import de.st_ddt.crazyarena.exceptions.CrazyArenaTeamNotEmptyException;

public class TeamList
{

	protected final ArrayList<Team> teams = new ArrayList<Team>();
	protected final Arena arena;
	protected int maxTeams = 0;
	protected int maxTeamSize = 0;
	protected boolean autoColor;

	public TeamList(Arena arena)
	{
		this.arena = arena;
	}

	public TeamList(Arena arena, int maxTeams, int maxTeamSize)
	{
		this(arena);
		this.maxTeams = maxTeams;
		this.maxTeamSize = maxTeamSize;
	}

	public Team addTeam(String name, int maxTeamSize) throws CrazyArenaTeamException
	{
		Team team = new Team(arena, name, maxTeamSize);
		addTeam(team);
		return team;
	}

	public Team addTeam(String name) throws CrazyArenaTeamException
	{
		Team team = new Team(arena, name, maxTeamSize);
		addTeam(team);
		return team;
	}

	private void addTeam(Team team) throws CrazyArenaTeamException
	{
		if (team == null)
			return;
		if (maxTeams > 0 && maxTeams < teams.size())
			throw new CrazyArenaTeamExceedingTeamLimitException(arena, team, maxTeams);
		teams.add(team);
	}

	public Team quickjoin(Player player, boolean addNew) throws CrazyArenaTeamException
	{
		if (addNew)
		{
			if (maxTeams > 0 || getTeamCount() > maxTeams)
				return quickjoin(player, false);
			Team team = addTeam(player.getName());
			team.add(player);
			return team;
		}
		int min = Integer.MAX_VALUE;
		Team minteam = null;
		for (Team team : teams)
			if (team.getMemberCount() < min)
			{
				minteam = team;
				min = team.getMemberCount();
			}
		minteam.add(player);
		return minteam;
	}

	public Team getTeam(String name)
	{
		for (Team team : teams)
			if (team.getName().equalsIgnoreCase(name))
				return team;
		return null;
	}

	public Team getTeam(ChatColor color)
	{
		for (Team team : teams)
			if (team.getColor().equals(color))
				return team;
		return null;
	}

	public Team getTeam(Player player)
	{
		for (Team team : teams)
			if (team.isMember(player))
				return team;
		return null;
	}

	public Team getTeam(int index) throws IndexOutOfBoundsException
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
		for (Team team : teams)
			team.clear();
	}

	/**
	 * Löscht leere Teams
	 */
	public void clean()
	{
		for (int i = teams.size(); i >= 0; i--)
			try
			{
				clean(i);
			}
			catch (CrazyArenaTeamException e)
			{
			}
	}

	/**
	 * Prüft ob das Team gelöscht werden kann
	 * 
	 * @throws CrazyArenaTeamNotEmptyException
	 */
	public void clean(int i) throws CrazyArenaTeamException
	{
		if (teams.get(i).getMemberCount() == 0)
			teams.remove(i);
		else
			throw new CrazyArenaTeamNotEmptyException(arena, teams.get(i));
	}

	/**
	 * Prüft ob das Team gelöscht werden kann
	 * 
	 * @throws CrazyArenaTeamNotEmptyException
	 */
	public void clean(Team team) throws CrazyArenaTeamException
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

	public void setMaxTeams(int maxTeams)
	{
		this.maxTeams = maxTeams;
	}

	public Arena getArena()
	{
		return arena;
	}

	public void setNewMaxTeamSize(int maxSize, boolean updateOld)
	{
		this.maxTeamSize = maxSize;
		if (updateOld)
			for (Team team : teams)
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

	public void setAutoColor(boolean autoColor)
	{
		this.autoColor = autoColor;
		for (Team team : teams)
			team.setAutoColors(autoColor);
	}
}
