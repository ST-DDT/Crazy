package de.st_ddt.crazychats.channels.arena;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazychats.channels.ChannelInterface;

public class ArenaChannel implements ChannelInterface
{

	protected final CrazyArena plugin;
	protected final List<String> aliases = new ArrayList<String>();
	protected final Arena<?> arena;

	public ArenaChannel(final CrazyArena plugin, final Arena<?> arena)
	{
		this(plugin, arena, true);
		aliases.add("a");
		aliases.add("arena");
	}

	protected ArenaChannel(final CrazyArena plugin, final Arena<?> arena, final boolean foo)
	{
		this.plugin = plugin;
		this.arena = arena;
	}

	@Override
	public String getName()
	{
		return arena.getName();
	}

	@Override
	public final List<String> getAliases()
	{
		return aliases;
	}

	@Override
	public boolean hasTalkPermission(final Player player)
	{
		return arena.isParticipant(player);
	}

	public final CrazyArena getPlugin()
	{
		return plugin;
	}

	public final Arena<?> getArena()
	{
		return arena;
	}

	@Override
	public Set<Player> getTargets(final Player player)
	{
		return arena.getParticipatingPlayers();
	}

	@Override
	public String getFormat(final Player player)
	{
		return StringUtils.replace(plugin.getArenaChatFormat(), "$A0$", arena.getParticipant(player).getParticipantType().toString());
	}

	@Override
	public String toString()
	{
		return arena.getType() + "ArenaChatChannel (Arena: " + arena.getName() + ")";
	}

	@Override
	public boolean isAffectedByServerSilence()
	{
		return false;
	}
}
