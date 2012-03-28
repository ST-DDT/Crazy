package de.st_ddt.crazyarena.exceptions;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class CrazyArenaException extends CrazyException
{

	private static final long serialVersionUID = -5638998288487141423L;
	protected final Arena arena;

	public CrazyArenaException(Arena arena)
	{
		super();
		this.arena = arena;
	}

	public Arena getArena()
	{
		return arena;
	}
	
	@Override
	public String getLangPath()
	{
		return "CRAZYARENA.EXCEPTIONS";
	}
	
	@Override
	public void print(CommandSender sender, String header)
	{
		sender.sendMessage(header+locale.getLocaleMessage(sender,"HEAD",arena.getName()));
	}
}
