package de.st_ddt.crazyarena.exceptions;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazyArenaException extends CrazyException
{

	private static final long serialVersionUID = -5638998288487141423L;
	protected final Arena<?> arena;

	public CrazyArenaException(final Arena<?> arena)
	{
		super();
		this.arena = arena;
	}

	public final Arena<?> getArena()
	{
		return arena;
	}

	@Override
	public String getLangPath()
	{
		return "CRAZYARENA.ARENA." + arena.getName().toUpperCase() + ".EXCEPTION";
	}

	@Localized({ "CRAZYARENA.ARENA_DEFAULT.EXCEPTION $Name$ $Type$ $Status$", "CRAZYARENA.ARENA_#TYPE#.EXCEPTION $Name$ $Type$ $Status$", "CRAZYARENA.ARENA.#NAME#.EXCEPTION $Name$ $Type$ $Status$" })
	public void print(final CommandSender sender, final String header)
	{
		ChatHelper.sendMessage(sender, header, locale, arena.getName(), arena.getType(), arena.getStatus().toString());
		if (printStackTrace)
			printStackTrace();
	}
}
