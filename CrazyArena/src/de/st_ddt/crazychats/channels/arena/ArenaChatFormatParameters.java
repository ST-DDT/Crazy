package de.st_ddt.crazychats.channels.arena;

import java.util.Arrays;

import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyutil.ChatFormatParameters;

public class ArenaChatFormatParameters implements ChatFormatParameters
{

	private final CrazyArena plugin;

	public ArenaChatFormatParameters(final CrazyArena plugin)
	{
		super();
		this.plugin = plugin;
	}

	@Override
	public String getParameterPrefix()
	{
		return "Arena";
	}

	@Override
	public Object[] getParameters(final Player player)
	{
		final String[] res = new String[3];
		final Arena<?> arena = plugin.getArena(player);
		if (arena == null)
			Arrays.fill(res, "");
		else
		{
			res[0] = arena.getName();
			res[1] = arena.getType();
			res[2] = arena.getParticipant(player).getParticipantType().name();
		}
		return res;
	}

	@Override
	public int getParameterCount()
	{
		return 2;
	}
}
