package de.st_ddt.crazyarena;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.exceptions.CrazyArenaException;
import de.st_ddt.crazyarena.listener.CrazyArenaPlayerListener;
import de.st_ddt.crazyarena.participants.Participant;
import de.st_ddt.crazyarena.participants.ParticipantType;
import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandAlreadyExistsException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCrazyErrorException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandErrorException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandExecutorException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;

public class CrazyArena extends CrazyPlugin
{

	private static final HashSet<Arena<?>> arenas = new HashSet<Arena<?>>();
	private static final TreeMap<String, Arena<?>> arenasByName = new TreeMap<String, Arena<?>>();
	private static final TreeMap<String, Set<Arena<?>>> arenasByType = new TreeMap<String, Set<Arena<?>>>();
	private static final TreeMap<String, Arena<?>> arenasByPlayer = new TreeMap<String, Arena<?>>();
	private static final TreeMap<String, Class<? extends Arena<?>>> arenaTypes = new TreeMap<String, Class<? extends Arena<?>>>();
	private static CrazyArena plugin;
	private final HashMap<String, Arena<?>> invitations = new HashMap<String, Arena<?>>();
	private final HashMap<String, Arena<?>> selection = new HashMap<String, Arena<?>>();
	private CrazyArenaPlayerListener playerlistener;

	public static CrazyArena getPlugin()
	{
		return plugin;
	}

	@Override
	protected String getShortPluginName()
	{
		return "ca";
	}

	@Override
	public void onEnable()
	{
		plugin = this;
		registerHooks();
		super.onEnable();
	}

	private void registerHooks()
	{
		playerlistener = new CrazyArenaPlayerListener(this);
		getServer();
		final PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(playerlistener, this);
	}

	@Override
	public void load()
	{
		final ConfigurationSection config = getConfig();
		arenas.clear();
		arenasByName.clear();
		for (final Set<Arena<?>> type : arenasByType.values())
			type.clear();
		arenasByPlayer.clear();
		invitations.clear();
		selection.clear();
		final List<String> arenaList = config.getStringList("arenas");
		int loadedArenas = 0;
		if (arenas != null)
			for (final String name : arenaList)
			{
				try
				{
					final Arena<?> arena = Arena.loadFromFile(name);
					loadedArenas++;
					arenas.add(arena);
					arenasByName.put(name.toLowerCase(), arena);
					arenasByType.get(arena.getType().toLowerCase()).add(arena);
				}
				catch (final FileNotFoundException e)
				{
					broadcastLocaleMessage(true, "crazyarena.warnloaderror", "ARENA.FILENOTFOUND", name);
				}
				catch (final Exception e)
				{}
			}
		sendLocaleMessage("ARENA.LOADED", Bukkit.getConsoleSender(), loadedArenas);
	}

	@Override
	public void save()
	{
		final ConfigurationSection config = getConfig();
		final LinkedList<String> names = new LinkedList<String>();
		for (final Arena<?> arena : arenas)
		{
			arena.shutdown();
			arena.saveToFile();
			names.add(arena.getName());
		}
		config.set("arenas", names);
		super.save();
	}

	public Set<Arena<?>> getArenas()
	{
		return arenas;
	}

	public Set<Arena<?>> getArenaByType(final String name)
	{
		return arenasByType.get(name.toLowerCase());
	}

	public Arena<?> getArena(final OfflinePlayer player)
	{
		return getArenaByPlayer(player.getName());
	}

	public Arena<?> getArenaByPlayer(final String name)
	{
		return arenasByPlayer.get(name.toLowerCase());
	}

	public void setArenaByPlayer(final OfflinePlayer player, final Arena<?> arena)
	{
		setArenaByPlayer(player.getName(), arena);
	}

	public void setArenaByPlayer(final String name, final Arena<?> arena)
	{
		arenasByPlayer.put(name.toLowerCase(), arena);
	}

	public Arena<?> getArena(final String name)
	{
		return arenasByName.get(name.toLowerCase());
	}

	public Set<Arena<?>> searchArenas(String name)
	{
		name = name.toLowerCase();
		final HashSet<Arena<?>> arenas = new HashSet<Arena<?>>();
		for (final Entry<String, Arena<?>> entry : arenasByName.entrySet())
			if (entry.getKey().matches(".*" + name + ".*"))
				arenas.add(entry.getValue());
		return arenas;
	}

	public TreeSet<String> searchArenaNames(final String name)
	{
		final TreeSet<String> arenas = new TreeSet<String>();
		for (final Arena<?> arena : searchArenas(name))
			arenas.add(arena.getName());
		return arenas;
	}

	@Override
	public boolean command(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyException
	{
		if (commandLabel.equalsIgnoreCase("join"))
		{
			if (sender instanceof ConsoleCommandSender)
				throw new CrazyCommandExecutorException(false);
			commandJoin((Player) sender, args);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("spectate"))
		{
			if (sender instanceof ConsoleCommandSender)
				throw new CrazyCommandExecutorException(false);
			commandSpectate((Player) sender, args);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("judge"))
		{
			if (sender instanceof ConsoleCommandSender)
				throw new CrazyCommandExecutorException(false);
			commandJudge((Player) sender, args);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("ready"))
		{
			if (sender instanceof ConsoleCommandSender)
				throw new CrazyCommandExecutorException(false);
			if (args.length != 0)
				throw new CrazyCommandUsageException("/ready");
			commandReady((Player) sender);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("team") || commandLabel.equalsIgnoreCase("teams"))
		{
			if (sender instanceof ConsoleCommandSender)
				throw new CrazyCommandExecutorException(false);
			commandTeam((Player) sender, args);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("leave"))
		{
			if (sender instanceof ConsoleCommandSender)
				throw new CrazyCommandExecutorException(false);
			if (args.length != 0)
				throw new CrazyCommandUsageException("/ready");
			commandLeave((Player) sender);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("invite"))
		{
			if (sender instanceof ConsoleCommandSender)
				throw new CrazyCommandExecutorException(false);
			commandInvite((Player) sender, args);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("score"))
		{
			if (sender instanceof ConsoleCommandSender)
				throw new CrazyCommandExecutorException(false);
			commandScore((Player) sender, args);
			return true;
		}
		return false;
	}

	private void commandJoin(final Player player, final String[] args) throws CrazyException
	{
		if (!player.hasPermission("crazyarena.join"))
			throw new CrazyCommandPermissionException();
		final Arena<?> oldArena = getArena(player);
		Arena<?> arena = null;
		switch (args.length)
		{
			case 0:
				arena = oldArena;
				if (arena == null)
					arena = invitations.get(player.getName().toLowerCase());
				if (arena == null)
					throw new CrazyCommandUsageException("/join <Arena/Player>");
				break;
			case 1:
				final String name = args[0];
				arena = getArena(name);
				if (arena == null)
				{
					Player to = Bukkit.getPlayerExact(name);
					if (to == null)
						to = Bukkit.getPlayer(name);
					arena = getArena(to);
				}
				if (arena == null)
					throw new CrazyCommandNoSuchException("Arena/Player", name, searchArenaNames(name));
				break;
			default:
				throw new CrazyCommandUsageException("/join <Arena/Player>");
		}
		if (oldArena != null)
			if (!oldArena.isParticipant(player, ParticipantType.SPECTATOR))
				throw new CrazyCommandCircumstanceException("when not in arena.", "(Currently in " + oldArena.getName() + ")");
			else
				oldArena.leave(player, false);
		if (!arena.getStatus().allowJoins())
			if (arena.getStatus().isActive())
			{
				if (!arena.allowJoin(player))
					throw new CrazyCommandCircumstanceException("when arena is ready for joins", arena.getStatus().toString());
			}
			else
				throw new CrazyCommandCircumstanceException("when arena is ready for joins", arena.getStatus().toString());
		arena.join(player, false);
		return;
	}

	private void commandSpectate(final Player player, final String[] args) throws CrazyException
	{
		if (!player.hasPermission("crazyarena.spectate"))
			throw new CrazyCommandPermissionException();
		Arena<?> arena = getArena(player);
		if (arena != null)
			throw new CrazyCommandCircumstanceException("when not in arena.", "(Currently in " + arena.getName() + ")");
		switch (args.length)
		{
			case 0:
				arena = invitations.get(player);
				if (arena == null)
					throw new CrazyCommandUsageException("/spectate <Arena/Player>");
				break;
			case 1:
				final String name = args[0];
				arena = getArena(name);
				if (arena == null)
				{
					Player to = Bukkit.getPlayerExact(args[0]);
					if (to == null)
						to = Bukkit.getPlayer(args[0]);
					arena = getArena(to);
				}
				if (arena == null)
					throw new CrazyCommandNoSuchException("Arena/Player", args[0], searchArenaNames(name));
				break;
			default:
				throw new CrazyCommandUsageException("/spectate <Arena/Player>");
		}
		if (!arena.getStatus().allowSpectators())
			throw new CrazyCommandCircumstanceException("when arena is ready for spectators", arena.getStatus().toString());
		arena.spectate(player);
	}

	private void commandJudge(final Player player, final String[] args) throws CrazyException
	{
		if (!player.hasPermission("crazyarena.judge"))
			throw new CrazyCommandPermissionException();
		Arena<?> arena = getArena(player);
		if (arena != null)
			throw new CrazyCommandCircumstanceException("when not in arena.", "(Currently in " + arena.getName() + ")");
		switch (args.length)
		{
			case 0:
				arena = invitations.get(player);
				if (arena == null)
					throw new CrazyCommandUsageException("/judge <Arena/Player>");
				break;
			case 1:
				final String name = args[0];
				arena = getArena(name);
				if (arena == null)
				{
					Player to = Bukkit.getPlayerExact(args[0]);
					if (to == null)
						to = Bukkit.getPlayer(args[0]);
					arena = getArena(to);
				}
				if (arena == null)
					throw new CrazyCommandNoSuchException("Arena/Player", args[0], searchArenaNames(name));
				break;
			default:
				throw new CrazyCommandUsageException("/judge <Arena/Player>");
		}
		if (!arena.getStatus().isActive())
			throw new CrazyCommandCircumstanceException("when arena is ready for judges", arena.getStatus().toString());
		arena.judge(player);
	}

	private void commandReady(final Player player) throws CrazyException
	{
		final Arena<?> arena = getArena(player);
		if (arena == null || !arena.isParticipant(player, ParticipantType.SELECTING))
			throw new CrazyCommandCircumstanceException("when waiting inside an arena!");
		arena.ready(player);
	}

	private void commandTeam(final Player player, final String[] args) throws CrazyException
	{
		final Arena<?> arena = getArena(player);
		if (arena == null || !arena.isParticipant(player, ParticipantType.SELECTING))
			throw new CrazyCommandCircumstanceException("when participating in an arena!");
		arena.team(player);
	}

	private void commandLeave(final Player player) throws CrazyException
	{
		final Arena<?> arena = getArena(player);
		if (arena == null || !arena.isParticipant(player))
			throw new CrazyCommandCircumstanceException("when participating in an arena!");
		arena.leave(player, false);
	}

	private boolean commandInvite(final Player player, final String[] args) throws CrazyCommandException
	{
		if (player.hasPermission("crazyarena.invite"))
			throw new CrazyCommandPermissionException();
		final Arena<?> arena = getArena(player);
		if (arena == null || !arena.isParticipant(player))
			throw new CrazyCommandCircumstanceException("when participating in an arena!");
		if (args.length == 0)
			throw new CrazyCommandUsageException("/invite <Player1> [Player...]");
		if (args[0] == "*")
		{
			if (player.hasPermission("crazyarena.invite.all"))
			{
				int anz = 0;
				for (final Player invited : Bukkit.getOnlinePlayers())
					if (getArena(invited) == null)
					{
						anz++;
						invitations.put(invited.getName().toLowerCase(), arena);
						sendLocaleMessage("COMMAND.INVITATION.MESSAGE", invited, player.getName(), arena.getName());
					}
				sendLocaleMessage("COMMAND.INVITATION.SUMMARY", player, anz, arena.getName());
				return true;
			}
			else
				throw new CrazyCommandPermissionException();
		}
		int anz = 0;
		for (final String name : args)
		{
			Player invited = Bukkit.getPlayerExact(name);
			if (invited == null)
				invited = Bukkit.getPlayer(name);
			if (invited == null)
				continue;
			anz++;
			invitations.put(invited.getName().toLowerCase(), arena);
			sendLocaleMessage("COMMAND.INVITATION.MESSAGE", invited, player.getName(), arena.getName());
		}
		sendLocaleMessage("COMMAND.INVITATION.SUMMARY", player, anz, arena.getName());
		return true;
	}

	private void commandScore(final Player player, final String[] args) throws CrazyException
	{
		Arena<?> arena = null;
		final String[] newArgs = ChatHelper.shiftArray(args, 1);
		if (args.length != 0)
			arena = getArena(args[0]);
		if (arena == null)
			arena = selection.get(player.getName().toLowerCase());
		arena.command(player, "score", newArgs);
	}

	@Override
	public boolean commandMain(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyException
	{
		if (command(sender, commandLabel, args))
			return true;
		if (commandLabel.equalsIgnoreCase("enable"))
		{
			commandEnable(sender, args, true);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("disable"))
		{
			commandEnable(sender, args, false);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("import"))
		{
			commandImport(sender, args);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("create") || commandLabel.equalsIgnoreCase("new"))
		{
			commandCreate(sender, args);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("delete") || commandLabel.equalsIgnoreCase("remove"))
		{
			commandDelete(sender, args);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("sel") || commandLabel.equalsIgnoreCase("select"))
		{
			commandSelect(sender, args);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("forceready"))
		{
			commandForceReady(sender, args);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("forcestop"))
		{
			commandForceStop(sender, args);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("kick"))
		{
			commandKick(sender, args);
			return true;
		}
		final Arena<?> arena = selection.get(sender.getName().toLowerCase());
		if (arena != null)
			if (arena.command(sender, commandLabel, args))
				return true;
		return false;
	}

	private void commandEnable(final CommandSender sender, final String[] args, final boolean enabled) throws CrazyCommandException
	{
		if (sender.hasPermission("crazyarena.arena.switchmode"))
			throw new CrazyCommandPermissionException();
		if (args.length != 1)
			throw new CrazyCommandUsageException("/crazyarena " + (enabled ? "enabled" : "disabled") + " <Arena>");
		final String name = args[0];
		final Arena<?> arena = getArena(name);
		try
		{
			arena.setEnabled(enabled);
		}
		catch (final CrazyArenaException e)
		{
			throw new CrazyCommandCrazyErrorException(e);
		}
		sendLocaleMessage("COMMAND.ARENA.ENABLED", sender, name, enabled ? "TRUE" : "FALSE");
		if (sender != Bukkit.getConsoleSender())
			sendLocaleMessage("COMMAND.ARENA.ENABLED", Bukkit.getConsoleSender(), name);
	}

	private void commandImport(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (sender.hasPermission("crazyarena.arena.import"))
			throw new CrazyCommandPermissionException();
		if (args.length != 1)
			throw new CrazyCommandUsageException();
		final String name = args[0];
		Arena<?> arena = getArena(name);
		if (arena != null)
			throw new CrazyCommandAlreadyExistsException("Arena", name);
		final File file = new File(Arena.arenaDataRootPath + name + File.separator + "config.yml");
		if (!file.exists())
			throw new CrazyCommandNoSuchException("ArenaFile", name);
		try
		{
			arena = Arena.loadFromFile(name, file);
		}
		catch (final Exception e)
		{
			throw new CrazyCommandErrorException(e);
		}
		arenas.add(arena);
		arenasByName.put(name, arena);
		arenasByType.get(arena.getType()).add(arena);
		sendLocaleMessage("COMMAND.ARENA.LOADED", sender, arena.getName());
		if (sender != Bukkit.getConsoleSender())
			sendLocaleMessage("COMMAND.ARENA.LOADED", Bukkit.getConsoleSender(), arena.getName());
		arena.show(sender);
	}

	private void commandCreate(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazyarena.arena.create"))
			throw new CrazyCommandPermissionException();
		if (sender instanceof ConsoleCommandSender)
			throw new CrazyCommandExecutorException(false);
		if (args.length != 2)
			throw new CrazyCommandUsageException("/crazyarena create <Name> <ArenaClass/Type>");
		final Player player = (Player) sender;
		final String name = args[0];
		if (getArena(name) != null)
			throw new CrazyCommandAlreadyExistsException("Arena", name);
		final String type = args[1];
		Class<?> clazz = arenaTypes.get(type.toLowerCase());
		if (clazz == null)
			try
			{
				clazz = Class.forName(type);
			}
			catch (final ClassNotFoundException e)
			{
				try
				{
					clazz = Class.forName("de.st_ddt.crazyarena.arenas." + type);
				}
				catch (final ClassNotFoundException e2)
				{
					throw new CrazyCommandNoSuchException("ArenaClass/Type", type);
				}
			}
		if (!Arena.class.isAssignableFrom(clazz))
			throw new CrazyCommandParameterException(2, "ArenaClass/Type");
		Arena<?> arena = null;
		try
		{
			arena = (Arena<?>) clazz.getConstructor(String.class, World.class).newInstance(name, player.getWorld());
		}
		catch (final Exception e)
		{
			throw new CrazyCommandErrorException(e);
		}
		if (arena == null)
			throw new CrazyCommandException();
		arenas.add(arena);
		arenasByName.put(name.toLowerCase(), arena);
		arenasByType.get(arena.getType()).add(arena);
		sendLocaleMessage("COMMAND.ARENA.CREATED", player, arena.getName());
		selection.put(player.getName().toLowerCase(), arena);
		sendLocaleMessage("COMMAND.ARENA.SELECTED", player, arena.getName());
	}

	private void commandDelete(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazyarena.arena.delete"))
			throw new CrazyCommandPermissionException();
		if (args.length != 1)
			throw new CrazyCommandUsageException("/crazyarena delete <Arena>");
		final String name = args[0];
		final Arena<?> arena = getArena(name);
		if (arena == null)
			throw new CrazyCommandNoSuchException("Arena", name, arenasByName.keySet());
		arena.shutdown();
		arena.saveToFile();
		arenas.remove(arena);
		arenasByName.remove(name);
		arenasByType.get(arena.getType()).remove(arena);
		Iterator<Entry<String, Arena<?>>> it = invitations.entrySet().iterator();
		while (it.hasNext())
			if (it.next().getValue() == arena)
				it.remove();
		it = selection.entrySet().iterator();
		while (it.hasNext())
			if (it.next().getValue() == arena)
				it.remove();
		sendLocaleMessage("COMMAND.ARENA.DELETED", sender, name);
		sendLocaleMessage("COMMAND.ARENA.DELETED.RECOVER", sender, name);
	}

	private void commandSelect(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazyarena.arena.modify"))
			throw new CrazyCommandPermissionException();
		if (sender instanceof ConsoleCommandSender)
			throw new CrazyCommandExecutorException(false);
		final Player player = (Player) sender;
		switch (args.length)
		{
			case 0:
				Arena<?> arena = selection.get(player);
				if (arena == null)
					throw new CrazyCommandUsageException("/crazyarena select <Arena>");
				else
					sendLocaleMessage("COMMAND.ARENA.SELECTED", player, arena.getName());
				return;
			case 1:
				arena = getArena(args[0]);
				if (arena == null)
					throw new CrazyCommandNoSuchException("Arena", args[0]);
				selection.put(player.getName().toLowerCase(), arena);
				sendLocaleMessage("COMMAND.ARENA.SELECTED", player, arena.getName());
				return;
			default:
				throw new CrazyCommandUsageException("/crazyarena select [Arena]");
		}
	}

	private void commandForceReady(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!sender.hasPermission("crazyarena.forceready"))
			throw new CrazyCommandPermissionException();
		if (args.length != 1)
			throw new CrazyCommandUsageException("/crazyarena forceready <Arena>");
		final Arena<?> arena = getArena(args[0]);
		if (arena == null)
			throw new CrazyCommandNoSuchException("Arena", args[0]);
		for (final Participant<?, ?> player : arena.getParticipants(ParticipantType.SELECTING))
			if (!arena.ready(player.getPlayer()))
				arena.leave(player.getPlayer(), true);
	}

	private void commandForceStop(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!sender.hasPermission("crazyarena.forcestop"))
			throw new CrazyCommandPermissionException();
		if (args.length != 1)
			throw new CrazyCommandUsageException("/crazyarena forcestop <Arena>");
		final Arena<?> arena = getArena(args[0]);
		if (arena == null)
			throw new CrazyCommandNoSuchException("Arena", args[0]);
		arena.stop();
		sendLocaleMessage("COMMAND.ARENA.STOP.FORCE", sender, arena.getName());
		sendLocaleMessage("COMMAND.ARENA.STOP.FORCE", arena.getParticipatingPlayers(), arena.getName());
		sendLocaleMessage("COMMAND.ARENA.STOP.FORCE", Bukkit.getConsoleSender(), arena.getName());
	}

	private boolean commandKick(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!sender.hasPermission("crazyarena.kick"))
			throw new CrazyCommandPermissionException();
		if (args.length == 0)
			throw new CrazyCommandUsageException("/crazyarena kick <Player> [Player...]");
		for (final String name : args)
		{
			Player player = Bukkit.getPlayerExact(name);
			if (player == null)
				player = Bukkit.getPlayer(name);
			if (player == null)
				throw new CrazyCommandNoSuchException("Player", name);
			final Arena<?> arena = getArena(player);
			if (arena != null)
				arena.leave(player, true);
			sendLocaleMessage("COMMAND.ARENA.KICK", Bukkit.getOnlinePlayers(), arena.getName(), player.getName());
			sendLocaleMessage("COMMAND.ARENA.KICK", Bukkit.getConsoleSender(), arena.getName(), player.getName());
		}
		return true;
	}

	public static Map<String, Class<? extends Arena<?>>> getArenaTypes()
	{
		return arenaTypes;
	}

	public static void registerArenaType(final String mainType, final Class<? extends Arena<?>> clazz, final String... aliases)
	{
		arenaTypes.put(mainType.toLowerCase(), clazz);
		for (final String alias : aliases)
			arenaTypes.put(alias.toLowerCase(), clazz);
		arenasByType.put(mainType.toLowerCase(), new HashSet<Arena<?>>());
	}
}
