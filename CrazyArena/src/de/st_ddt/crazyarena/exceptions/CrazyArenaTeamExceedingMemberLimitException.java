package de.st_ddt.crazyarena.exceptions;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.teams.Team;


public class CrazyArenaTeamExceedingMemberLimitException extends CrazyArenaTeamException
{

	private static final long serialVersionUID = -6289593070900257339L;
	protected final int maxSize ;
	
	
	
	public CrazyArenaTeamExceedingMemberLimitException(Arena arena, Team team, int maxSize)
	{
		super(arena, team);
		this.maxSize = maxSize;
	}
	
	public int getMaxSize()
	{
		return maxSize;
	}

	@Override
	public String getLangPath()
	{
		return super.getLangPath()+".FULL";
	}

	@Override
	public void print(CommandSender sender, String header)
	{
		super.print(sender, header);
		sender.sendMessage(header+locale.getLocaleMessage(sender,"MEMBERS", maxSize));
	}
}
