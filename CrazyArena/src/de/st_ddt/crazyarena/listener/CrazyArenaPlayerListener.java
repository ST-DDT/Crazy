package de.st_ddt.crazyarena.listener;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.participants.ParticipantType;
import de.st_ddt.crazyarena.utils.SignRotation;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazyArenaPlayerListener implements Listener
{

	private final Map<String, Rejoin> rejoins = new HashMap<String, Rejoin>();
	private final CrazyArena plugin;

	public CrazyArenaPlayerListener(final CrazyArena plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void PlayerJoin(final PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		final Rejoin rejoin = rejoins.get(player.getName());
		if (rejoin != null)
		{
			final Arena<?> arena = rejoin.getArena();
			rejoins.remove(player.getName());
			// Arena aktiv?
			if (!arena.getStatus().isActive())
				return;
			// noch gleicher Durchlauf?
			if (rejoin.getRun() != arena.getRunNumber())
				return;
			// Been away to long?
			if (player.getLastPlayed() + arena.getRejoinTime() < System.currentTimeMillis())
				return;
			try
			{
				if (arena.join(player, true))
					plugin.getArenaByPlayer().put(player, arena);
			}
			catch (final CrazyException e)
			{}
		}
	}

	@EventHandler
	public void PlayerQuit(final PlayerQuitEvent event)
	{
		final Player player = event.getPlayer();
		final Arena<?> arena = plugin.getArenaByPlayer().remove(player);
		if (arena != null)
		{
			rejoins.put(player.getName(), new Rejoin(arena));
			final ParticipantType type = arena.getParticipant(player).getParticipantType();
			if (type.isPlaying() || type.isJudge())
				arena.quitgame(player);
			else
				try
				{
					arena.leave(player, true);
				}
				catch (final CrazyException e)
				{}
		}
		plugin.getSelections().remove(player);
		plugin.getInvitations().remove(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void SignChangeEvent(final SignChangeEvent event)
	{
		final Block block = event.getBlock();
		if (block.getType() != Material.WALL_SIGN)
			return;
		final String[] lines = event.getLines();
		if (lines[0] == null || lines[1] == null)
			return;
		if (!lines[0].equals("[CA]") && !lines[0].equals("[CArena]"))
			return;
		final SignRotation rotation = SignRotation.getByBytes(block.getData());
		if (lines[1].length() == 0)
		{
			final Vector flipped = rotation.getTextVector().multiply(-1);
			final Location search = block.getLocation().clone();
			while (search.add(flipped).getBlock().getType() == Material.WALL_SIGN)
			{
				final Block searchBlock = search.getBlock();
				if (rotation != SignRotation.getByBytes(searchBlock.getData()))
					return;
				final Sign start = (Sign) searchBlock.getState();
				final String[] lines2 = start.getLines();
				if (!lines2[0].equals(CrazyArena.ARENASIGNHEADER))
					return;
				if (lines2[1].length() == 0)
					continue;
				if (searchSigns(plugin.getArenaByName(lines2[1]), searchBlock, rotation, null, event.getPlayer()))
					lines[0] = CrazyArena.ARENASIGNHEADER;
				else
					event.setCancelled(true);
				return;
			}
		}
		else
		{
			final Arena<?> arena = plugin.getArenaByName(lines[1]);
			if (searchSigns(arena, block, rotation, lines[2], event.getPlayer()))
			{
				lines[0] = CrazyArena.ARENASIGNHEADER;
				lines[1] = arena.getName();
			}
			else
				event.setCancelled(true);
		}
	}

	@Localized({ "CRAZYARENA.ARENA_DEFAULT.SIGNS.NOPERMISSION $ArenaName$", "CRAZYARENA.ARENA_$TYPE$.SIGNS.NOPERMISSION $ArenaName$" })
	private boolean searchSigns(final Arena<?> arena, final Block block, final SignRotation rotation, final String type, final Player player)
	{
		if (arena == null)
			return false;
		if (!arena.hasPermission(player, "signs.create"))
		{
			arena.sendLocaleMessage("SIGNS.NOPERMISSION", player, arena.getName());
			return false;
		}
		if (type != null)
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
			{

				@Override
				public void run()
				{
					arena.attachSign(block, rotation, type, player);
				}
			});
		return true;
	}

	private final class Rejoin
	{

		private final Arena<?> arena;
		private final int run;

		public Rejoin(final Arena<?> arena)
		{
			super();
			this.arena = arena;
			run = arena.getRunNumber();
		}

		public Arena<?> getArena()
		{
			return arena;
		}

		public int getRun()
		{
			return run;
		}
	}
}
