package de.st_ddt.crazyarena.arenas;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.exceptions.CrazyArenaUnsupportedException;
import de.st_ddt.crazyarena.participants.Participant;
import de.st_ddt.crazyarena.participants.ParticipantType;
import de.st_ddt.crazyplugin.commands.CrazyCommandExecutorInterface;
import de.st_ddt.crazyplugin.data.ParameterData;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHeaderProvider;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.Named;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public abstract class Arena<S extends Participant<S, ?>> implements Named, ChatHeaderProvider, ParameterData
{

	public static final String arenaDataRootPath = "plugins" + File.separator + "CrazyArena" + File.separator + "arenas" + File.separator;
	protected final String name;
	protected final YamlConfiguration config = new YamlConfiguration();
	protected final String chatHeader;
	protected final Map<String, S> participants = new HashMap<String, S>();
	protected final CrazyLocale locale;
	protected ArenaStatus status = ArenaStatus.INITIALIZING;

	public static Arena<?> loadFromFile(final String name) throws Exception
	{
		return loadFromFile(name, new File(arenaDataRootPath + name + File.separator + "config.yml"));
	}

	public static Arena<?> loadFromFile(final String name, final File file) throws Exception
	{
		final YamlConfiguration config = new YamlConfiguration();
		config.load(file);
		return loadFromConfig(name, config);
	}

	public static Arena<?> loadFromConfig(final String name, final ConfigurationSection config) throws Exception
	{
		Arena<?> arena = null;
		arena = ObjectSaveLoadHelper.load(config, Arena.class, new Class[] { String.class, ConfigurationSection.class }, new Object[] { name, config }, null);
		if (arena.getStatus() != ArenaStatus.CONSTRUCTING && arena.getStatus() != ArenaStatus.DISABLED)
			arena.setEnabled(null, true);
		return arena;
	}

	public Arena(final String name)
	{
		super();
		this.name = name;
		getDataFolder().mkdirs();
		this.chatHeader = ChatColor.RED + "[" + ChatColor.GREEN + name + ChatColor.RED + "]" + ChatColor.WHITE;
		// Locale CRAZYARENA.ARENA.#NAME#... -> CRAZYARENA.ARENA_#TYPE#... -> CRAZYARENA.ARENA_DEFAULT...
		final CrazyLocale pluginLocale = CrazyArena.getPlugin().getLocale();
		this.locale = pluginLocale.getSecureLanguageEntry("ARENA." + name.toUpperCase());
		final CrazyLocale typeLocale = pluginLocale.getSecureLanguageEntry("ARENA_" + getType().toUpperCase());
		this.locale.setAlternative(typeLocale);
		final CrazyLocale defaultLocale = pluginLocale.getSecureLanguageEntry("ARENA_DEFAULT");
		typeLocale.setAlternative(defaultLocale);
	}

	public Arena(final String name, final ConfigurationSection config)
	{
		super();
		this.name = name;
		this.chatHeader = ChatHelper.colorise(config.getString("chatHeader", ChatColor.RED + "[" + ChatColor.GREEN + name + ChatColor.RED + "]" + ChatColor.WHITE));
		// Locale CRAZYARENA.ARENA.#NAME#... -> CRAZYARENA.ARENA_#TYPE#... -> CRAZYARENA.ARENA_DEFAULT...
		final CrazyLocale pluginLocale = CrazyArena.getPlugin().getLocale();
		this.locale = pluginLocale.getSecureLanguageEntry("ARENA." + name.toUpperCase());
		final CrazyLocale typeLocale = pluginLocale.getSecureLanguageEntry("ARENA_" + getType().toUpperCase());
		this.locale.setAlternative(typeLocale);
		final CrazyLocale defaultLocale = pluginLocale.getSecureLanguageEntry("ARENA_DEFAULT");
		typeLocale.setAlternative(defaultLocale);
	}

	public ArenaStatus getStatus()
	{
		return status;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getChatHeader()
	{
		return chatHeader;
	}

	public abstract String getType();

	public ConfigurationSection getConfig()
	{
		return config;
	}

	public final File getDataFolder()
	{
		return new File(arenaDataRootPath + name);
	}

	protected abstract void save();

	public final void saveToFile()
	{
		save();
		try
		{
			getDataFolder().mkdirs();
			config.save(arenaDataRootPath + name + File.separator + "config.yml");
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Let a player join this arena.
	 * 
	 * @param player
	 *            The joining player
	 * @param rejoin
	 *            whether he is already in this arena or not. (Rejoins after leaving the server only)
	 */
	public abstract void join(Player player, boolean rejoin) throws CrazyException;

	/**
	 * Allow a player to join although the game has started already.
	 * 
	 * @param player
	 * @return
	 */
	public boolean allowJoin(final Player player)
	{
		return false;
	}

	public abstract boolean ready(Player player) throws CrazyException;

	public void team(final Player player, final String... team) throws CrazyException
	{
		throw new CrazyArenaUnsupportedException(this, "Teams");
	}

	public void spectate(final Player player) throws CrazyException
	{
		throw new CrazyArenaUnsupportedException(this, "Spectator");
	}

	public void judge(final Player player) throws CrazyException
	{
		throw new CrazyArenaUnsupportedException(this, "Judges");
	}

	public abstract void leave(final Player player, final boolean kicked) throws CrazyException;

	public abstract void quitgame(Player player);

	public abstract void registerMatchListener();

	public abstract void unregisterMatchListener();

	public abstract void registerArenaListener();

	public abstract void unregisterArenaListener();

	/**
	 * Stops the arena and kicks every participant. This should be executed after every finished match.
	 */
	public void stop()
	{
		for (final Player player : getParticipatingPlayers())
			try
			{
				leave(player, true);
			}
			catch (final CrazyException e)
			{}
		participants.clear();
		unregisterMatchListener();
		if (status.isEnabled())
			status = ArenaStatus.READY;
	}

	/**
	 * Shutdown arena. This will be executed on server shutdown and before arena is deleted.
	 */
	public void shutdown()
	{
		status = ArenaStatus.SHUTDOWN;
		stop();
		unregisterArenaListener();
	}

	public boolean isParticipant(final Player player)
	{
		return isParticipant(player.getName());
	}

	public boolean isParticipant(final String name)
	{
		return participants.containsKey(name.toLowerCase());
	}

	public boolean isParticipant(final Player player, final ParticipantType type)
	{
		return isParticipant(player.getName(), type);
	}

	public boolean isParticipant(final String name, final ParticipantType type)
	{
		final S participant = getParticipant(name);
		if (participant == null)
			return false;
		return participant.getParticipantType() == type;
	}

	public Collection<S> getParticipants()
	{
		return participants.values();
	}

	/**
	 * The run number increases every match played and is important for rejoins. A player can only rejoin if the arena has the same run number
	 * 
	 * @return the current match number
	 * 
	 */
	public abstract int getRunNumber();

	/**
	 * Time a player can rejoin after leaving the server.
	 * 
	 * @return Time in milliseconds
	 */
	public abstract long getRejoinTime();

	public Set<S> getParticipants(final ParticipantType... types)
	{
		final Set<S> participants = new HashSet<S>();
		for (final ParticipantType type : types)
			for (final S participant : getParticipants())
				if (participant.getParticipantType() == type)
					participants.add(participant);
		return participants;
	}

	public Set<Player> getParticipatingPlayers()
	{
		final Set<Player> players = new HashSet<Player>();
		for (final S participant : getParticipants())
			players.add(participant.getPlayer());
		players.remove(null);
		return players;
	}

	public Set<Player> getParticipatingPlayers(final ParticipantType... types)
	{
		final Set<Player> players = new HashSet<Player>();
		for (final S participant : getParticipants(types))
			players.add(participant.getPlayer());
		players.remove(null);
		return players;
	}

	public TreeSet<String> getParticipatingPlayerNames()
	{
		final TreeSet<String> names = new TreeSet<String>();
		for (final Player player : getParticipatingPlayers())
			names.add(player.getName());
		names.remove(null);
		return names;
	}

	public TreeSet<String> getParticipatingPlayerNames(final ParticipantType... types)
	{
		final TreeSet<String> names = new TreeSet<String>();
		for (final S player : getParticipants(types))
			names.add(player.getName());
		names.remove(null);
		return names;
	}

	public S getParticipant(final Player player)
	{
		return getParticipant(player.getName());
	}

	public S getParticipant(final String name)
	{
		return participants.get(name.toLowerCase());
	}

	public boolean setEnabled(final CommandSender sender, final boolean enabled)
	{
		if (enabled)
		{
			if (!status.isEnabled())
				if (!checkArena(sender))
					status = ArenaStatus.CONSTRUCTING;
				else
					status = ArenaStatus.READY;
		}
		else
		{
			status = ArenaStatus.DISABLED;
			stop();
		}
		return status.isEnabled();
	}

	/**
	 * This method checks whether the arena is setup properly. Return true when arena is setup corretly, otherwise it returns false. If this returns true, it does not send anything to the sender.
	 * 
	 * @param sender
	 *            The checking CommandSender (is null when loading arenas from file)
	 * @return Whether this arena is finished or not.
	 */
	protected abstract boolean checkArena(final CommandSender sender);

	@Override
	public void show(final CommandSender target)
	{
		show(target, chatHeader, true);
	}

	@Override
	public String getShortInfo()
	{
		return toString();
	}

	@Override
	public String toString()
	{
		return "Arena " + name;
	}

	@Override
	public String getParameter(final CommandSender sender, final int index)
	{
		switch (index)
		{
			case 0:
				return name;
			case 1:
				return status.toString();
			case 2:
				return ChatHelper.listingString(getParticipatingPlayerNames());
			default:
				return "";
		}
	}

	@Override
	public int getParameterCount()
	{
		return 3;
	}

	@Override
	public void show(final CommandSender target, final String chatHeader, final boolean showDetailed)
	{
		// EDIT show
		// Name & Type
		// Separator
		// Participants sorted by TypeAndName
	}

	public abstract CrazyCommandExecutorInterface getCommandExecutor();

	public CrazyLocale getLocale()
	{
		return locale;
	}

	public final void sendLocaleMessage(final String localepath, final CommandSender target, final Object... args)
	{
		sendLocaleMessage(getLocale().getLanguageEntry(localepath), target, args);
	}

	public final void sendLocaleMessage(final CrazyLocale locale, final CommandSender target, final Object... args)
	{
		ChatHelper.sendMessage(target, getChatHeader(), locale, args);
	}

	public final void sendLocaleMessage(final String localepath, final CommandSender[] targets, final Object... args)
	{
		sendLocaleMessage(getLocale().getLanguageEntry(localepath), targets, args);
	}

	public final void sendLocaleMessage(final CrazyLocale locale, final CommandSender[] targets, final Object... args)
	{
		ChatHelper.sendMessage(targets, getChatHeader(), locale, args);
	}

	public final void sendLocaleMessage(final String localepath, final Collection<? extends CommandSender> targets, final Object... args)
	{
		sendLocaleMessage(getLocale().getLanguageEntry(localepath), targets, args);
	}

	public final void sendLocaleMessage(final CrazyLocale locale, final Collection<? extends CommandSender> targets, final Object... args)
	{
		ChatHelper.sendMessage(targets, getChatHeader(), locale, args);
	}

	public final void sendLocaleMessage(final String localepath, final S target, final Object... args)
	{
		sendLocaleMessage(getLocale().getLanguageEntry(localepath), target, args);
	}

	public final void sendLocaleMessage(final CrazyLocale locale, final S target, final Object... args)
	{
		if (target.isOnline())
			ChatHelper.sendMessage(target.getPlayer(), getChatHeader(), locale, args);
	}

	public final void sendLocaleMessage(final String localepath, final S[] targets, final Object... args)
	{
		sendLocaleMessage(getLocale().getLanguageEntry(localepath), targets, args);
	}

	public final void sendLocaleMessage(final CrazyLocale locale, final S[] targets, final Object... args)
	{
		for (final S target : targets)
			sendLocaleMessage(locale, target, args);
	}

	public final void broadcastLocaleMessage(final boolean console, final String localepath, final Object... args)
	{
		broadcastLocaleMessage(console, getLocale().getLanguageEntry(localepath), args);
	}

	public final void broadcastLocaleMessage(final boolean console, final CrazyLocale locale, final Object... args)
	{
		if (console)
			sendLocaleMessage(locale, Bukkit.getConsoleSender(), args);
		sendLocaleMessage(locale, getParticipatingPlayers(), args);
	}

	public final void broadcastLocaleMessage(final boolean console, final ParticipantType type, final String localepath, final Object... args)
	{
		broadcastLocaleMessage(console, type, getLocale().getLanguageEntry(localepath), args);
	}

	public final void broadcastLocaleMessage(final boolean console, final ParticipantType type, final CrazyLocale locale, final Object... args)
	{
		if (type == null)
			broadcastLocaleMessage(console, new ParticipantType[] {}, locale, args);
		else
			broadcastLocaleMessage(console, new ParticipantType[] { type }, locale, args);
	}

	public final void broadcastLocaleMessage(final boolean console, final ParticipantType[] types, final String localepath, final Object... args)
	{
		broadcastLocaleMessage(console, types, getLocale().getLanguageEntry(localepath), args);
	}

	public final void broadcastLocaleMessage(final boolean console, final ParticipantType[] types, final CrazyLocale locale, final Object... args)
	{
		if (console)
			sendLocaleMessage(locale, Bukkit.getConsoleSender(), args);
		for (final ParticipantType type : types)
			for (final S player : getParticipants(type))
				sendLocaleMessage(locale, player, args);
	}

	public final void broadcastLocaleMessage(final boolean console, final boolean player, final boolean spectator, final boolean judge, final String localepath, final Object... args)
	{
		broadcastLocaleMessage(console, player, spectator, judge, getLocale().getLanguageEntry(localepath), args);
	}

	public final void broadcastLocaleMessage(final boolean console, final boolean player, final boolean spectator, final boolean judge, final CrazyLocale locale, final Object... args)
	{
		if (console)
			sendLocaleMessage(locale, Bukkit.getConsoleSender(), args);
		if (player)
			for (final S participant : getParticipants())
				if (participant.isPlayer())
					sendLocaleMessage(locale, participant, args);
		if (spectator)
			for (final S participant : getParticipants(ParticipantType.SPECTATOR))
				sendLocaleMessage(locale, participant, args);
		if (judge)
			for (final S participant : getParticipants(ParticipantType.JUDGE))
				sendLocaleMessage(locale, participant, args);
	}

	public boolean hasPermission(final CommandSender sender, final String permissionSuffix)
	{
		if (PermissionModule.hasPermission(sender, "crazyarena." + permissionSuffix))
			return true;
		else if (PermissionModule.hasPermission(sender, "crazyarena.*"))
			return true;
		else if (PermissionModule.hasPermission(sender, "crazyarena.t_" + getType() + "." + permissionSuffix))
			return true;
		else if (PermissionModule.hasPermission(sender, "crazyarena.t_" + getType() + ".*"))
			return true;
		else if (PermissionModule.hasPermission(sender, "crazyarena.n_" + getName() + "." + permissionSuffix))
			return true;
		else if (PermissionModule.hasPermission(sender, "crazyarena.n_" + getName() + ".*"))
			return true;
		else
			return false;
	}
}
