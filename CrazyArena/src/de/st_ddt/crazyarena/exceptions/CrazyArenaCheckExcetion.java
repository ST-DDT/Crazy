/**
 * 
 */
package de.st_ddt.crazyarena.exceptions;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyarena.arenas.Arena;

public class CrazyArenaCheckExcetion extends CrazyArenaException
{

	private static final long serialVersionUID = 2691206292664703390L;

	public CrazyArenaCheckExcetion(Arena arena)
	{
		super(arena);
	}
	
	@Override
	public String getLangPath()
	{
		return super.getLangPath()+".CHECK";
	}
	
	@Override
	public void print(CommandSender sender, String header)
	{
		sender.sendMessage(header+locale.getLocaleMessage(sender,"CHECK",arena.getName()));
	}
	
	
}
