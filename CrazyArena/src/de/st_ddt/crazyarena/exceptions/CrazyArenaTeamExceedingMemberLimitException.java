package de.st_ddt.crazyarena.exceptions;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.teams.Team;

public class CrazyArenaTeamExceedingMemberLimitException extends CrazyArenaTeamException
{

	private static final long serialVersionUID = -6289593070900257339L;
	protected final int maxCount;

	public <S extends Arena<?>> CrazyArenaTeamExceedingMemberLimitException(final S arena, final Team<?, S> team, final int maxCount)
	{
		super(arena, team);
		this.maxCount = maxCount;
	}

	public int getMaxCount()
	{
		return maxCount;
	}

	@Override
	public String getLangPath()
	{
		return super.getLangPath() + ".MEMBERS.TOMUCH";
	}

	@Override
	public void print(final CommandSender sender, final String header)
	{
		super.print(sender, header);
		sender.sendMessage(header + locale.getLocaleMessage(sender, "MAX", maxCount));
	}
}
