package de.st_ddt.crazyarena.exceptions;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.teams.Team;

public class CrazyArenaTeamExceedingTeamLimitException extends CrazyArenaTeamException
{

	private static final long serialVersionUID = -2237622908149259926L;
	protected final int maxAmount;

	public CrazyArenaTeamExceedingTeamLimitException(Arena arena, Team team, int maxTeamAmount)
	{
		super(arena, team);
		this.maxAmount = maxTeamAmount;
	}

	public int getMaxAmount()
	{
		return maxAmount;
	}

	@Override
	public String getLangPath()
	{
		return super.getLangPath() + ".FULL";
	}

	@Override
	public void print(CommandSender sender, String header)
	{
		super.print(sender, header);
		sender.sendMessage(header + locale.getLocaleMessage(sender, "TEAMS", String.valueOf(maxAmount)));
	}
}
