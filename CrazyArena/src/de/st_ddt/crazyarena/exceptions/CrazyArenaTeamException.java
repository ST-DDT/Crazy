package de.st_ddt.crazyarena.exceptions;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.teams.Team;

public class CrazyArenaTeamException extends CrazyArenaException
{

	private static final long serialVersionUID = -1709234377602224997L;
	protected final Team team;

	public CrazyArenaTeamException(Arena arena, Team team)
	{
		super(arena);
		this.team = team;
	}

	public Team getTeam()
	{
		return team;
	}

	@Override
	public String getLangPath()
	{
		return super.getLangPath() + ".TEAM";
	}

	@Override
	public void print(CommandSender sender, String header)
	{
		sender.sendMessage(header + locale.getLocaleMessage(sender, "HEAD", arena.getName(), team.getName()));
	}
}
