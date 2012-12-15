package de.st_ddt.crazychats.channels;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;

public class ArenaChannel implements ChannelInterface
{

	protected final CrazyArena plugin;
	protected final List<String> aliases = new ArrayList<String>();
	protected final Arena<?> arena;

	public ArenaChannel(final CrazyArena plugin, final Arena<?> arena)
	{
		super();
		aliases.add("a");
		aliases.add("arena");
		this.plugin = plugin;
		this.arena = arena;
	}

	@Override
	public String getName()
	{
		return arena.getName();
	}

	@Override
	public List<String> getAliases()
	{
		return aliases;
	}

	@Override
	public boolean hasTalkPermission(final Player player)
	{
		return arena.isParticipant(player);
	}

	public CrazyArena getPlugin()
	{
		return plugin;
	}

	public Arena<?> getArena()
	{
		return arena;
	}

	@Override
	public Set<Player> getTargets(final Player player)
	{
		return arena.getParticipatingPlayers();
	}

	@Override
	public String getFormat()
	{
		return plugin.getArenaChatFormat();
	}
}
