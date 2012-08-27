package de.st_ddt.crazyarena.exceptions;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.teams.Team;

public class CrazyArenaTeamNotEmptyException extends CrazyArenaTeamException
{

	private static final long serialVersionUID = -5779151059736556417L;

	public <S extends Arena<?>> CrazyArenaTeamNotEmptyException(S arena, Team<?, S> team)
	{
		super(arena, team);
	}

	@Override
	public String getLangPath()
	{
		return super.getLangPath() + ".NOTEMPTY";
	}
}
