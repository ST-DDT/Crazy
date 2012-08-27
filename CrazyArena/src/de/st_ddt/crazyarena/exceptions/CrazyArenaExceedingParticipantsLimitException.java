package de.st_ddt.crazyarena.exceptions;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyarena.arenas.Arena;

public class CrazyArenaExceedingParticipantsLimitException extends CrazyArenaException
{

	private static final long serialVersionUID = 6628500408828309453L;
	protected final Integer maxCount;

	public CrazyArenaExceedingParticipantsLimitException(final Arena<?> arena)
	{
		super(arena);
		this.maxCount = null;
	}

	public CrazyArenaExceedingParticipantsLimitException(final Arena<?> arena, final Integer maxCount)
	{
		super(arena);
		this.maxCount = maxCount;
	}

	public Integer getMaxCount()
	{
		return maxCount;
	}

	@Override
	public String getLangPath()
	{
		return super.getLangPath() + ".PARTICIPANTS.TOMUCH";
	}

	@Override
	public void print(final CommandSender sender, final String header)
	{
		sender.sendMessage(header + locale.getLocaleMessage(sender, "HEAD", arena.getName()));
		if (maxCount != null)
			sender.sendMessage(header + locale.getLocaleMessage(sender, "MAX", maxCount));
	}
}
