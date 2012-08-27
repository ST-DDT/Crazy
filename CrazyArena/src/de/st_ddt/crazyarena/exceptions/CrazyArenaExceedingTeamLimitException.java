package de.st_ddt.crazyarena.exceptions;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyarena.arenas.Arena;

public class CrazyArenaExceedingTeamLimitException extends CrazyArenaException
{

	private static final long serialVersionUID = -2237622908149259926L;
	protected final String teamName;
	protected final int maxCount;

	public <S extends Arena<?>> CrazyArenaExceedingTeamLimitException(final S arena, final String teamName, final int maxCount)
	{
		super(arena);
		this.teamName = teamName;
		this.maxCount = maxCount;
	}

	public int getMaxCount()
	{
		return maxCount;
	}

	@Override
	public String getLangPath()
	{
		return super.getLangPath() + ".TEAMS.TOMUCH";
	}

	@Override
	public void print(final CommandSender sender, final String header)
	{
		sender.sendMessage(header + locale.getLocaleMessage(sender, "HEAD", teamName));
		sender.sendMessage(header + locale.getLocaleMessage(sender, "MAX", maxCount));
	}
}
