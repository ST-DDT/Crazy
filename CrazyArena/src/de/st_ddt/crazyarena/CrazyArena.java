package de.st_ddt.crazyarena;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.command.CommandMainCreate;
import de.st_ddt.crazyarena.command.CommandMainDelete;
import de.st_ddt.crazyarena.command.CommandMainDisable;
import de.st_ddt.crazyarena.command.CommandMainEdit;
import de.st_ddt.crazyarena.command.CommandMainEnable;
import de.st_ddt.crazyarena.command.CommandMainForceReady;
import de.st_ddt.crazyarena.command.CommandMainForceStop;
import de.st_ddt.crazyarena.command.CommandMainImport;
import de.st_ddt.crazyarena.command.CommandMainKick;
import de.st_ddt.crazyarena.command.CommandMainSelect;
import de.st_ddt.crazyarena.command.CommandMainTreeDefault;
import de.st_ddt.crazyarena.command.PlayerCommandInvite;
import de.st_ddt.crazyarena.command.PlayerCommandJoin;
import de.st_ddt.crazyarena.command.PlayerCommandJudge;
import de.st_ddt.crazyarena.command.PlayerCommandLeave;
import de.st_ddt.crazyarena.command.PlayerCommandReady;
import de.st_ddt.crazyarena.command.PlayerCommandSpectate;
import de.st_ddt.crazyarena.command.PlayerCommandTeam;
import de.st_ddt.crazyarena.events.CrazyArenaArenaCreateEvent;
import de.st_ddt.crazyarena.listener.CrazyArenaCrazyChatsListener;
import de.st_ddt.crazyarena.listener.CrazyArenaPlayerListener;
import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.channels.arena.ArenaChatFormatParameters;
import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.commands.CrazyPluginCommandMainMode;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.CrazyChatsChatHelper;
import de.st_ddt.crazyutil.modes.ChatFormatMode;
import de.st_ddt.crazyutil.modes.Mode;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.LocalizedVariable;

@LocalizedVariable(variables="CRAZYPLUGIN",values="CRAZYARENA")
public class CrazyArena extends CrazyPlugin
{

	public final static String ARENASIGNHEADER = ChatColor.RED + "[" + ChatColor.GREEN + "CArena" + ChatColor.RED + "]";
	private static CrazyArena plugin;
	private final CrazyPluginCommandMainMode modeCommand = new CrazyPluginCommandMainMode(this);
	private final Set<Arena<?>> arenas = new HashSet<Arena<?>>();
	private final Map<String, Arena<?>> arenasByName = new TreeMap<String, Arena<?>>();
	private final Map<Player, Arena<?>> arenasByPlayer = new HashMap<Player, Arena<?>>();
	private final Map<String, Set<Arena<?>>> arenasByType = new TreeMap<String, Set<Arena<?>>>();
	private final Map<Player, Arena<?>> invitations = new HashMap<Player, Arena<?>>();
	private final Map<CommandSender, Arena<?>> selections = new HashMap<CommandSender, Arena<?>>();
	private final Random random = new Random();
	private boolean crazyChatsEnabled;
	private String arenaChatFormat = "[Arena]%1$s: %2$s";
	private String arenaSpectatorChatFormat = "[Arena/S]%1$s: %2$s";
	private String arenaPlayerChatFormat = "[Arena/P]%1$s: %2$s";
	private String arenaJudgeChatFormat = "[Arena/J]%1$s: %2$s";

	public static CrazyArena getPlugin()
	{
		return plugin;
	}

	private void registerModesCrazyChats()
	{
		final CrazyPluginCommandMainMode chatsModeCommand = CrazyChats.getPlugin().getModeCommand();
		final Mode<?> arenaChatFormatMode = new ChatFormatMode(this, "arenaChatFormat")
		{

			@Override
			public String getValue()
			{
				return arenaChatFormat;
			}

			@Override
			public void setValue(final String newValue) throws CrazyException
			{
				arenaChatFormat = newValue;
				saveConfiguration();
			}
		};
		modeCommand.addMode(arenaChatFormatMode);
		chatsModeCommand.addMode(arenaChatFormatMode);
		final Mode<?> arenaSpectatorChatFormatMode = new ChatFormatMode(this, "arenaSpectatorChatFormat")
		{

			@Override
			public String getValue()
			{
				return arenaSpectatorChatFormat;
			}

			@Override
			public void setValue(final String newValue) throws CrazyException
			{
				arenaSpectatorChatFormat = newValue;
				saveConfiguration();
			}
		};
		modeCommand.addMode(arenaSpectatorChatFormatMode);
		chatsModeCommand.addMode(arenaSpectatorChatFormatMode);
		final Mode<?> arenaPlayerChatFormatMode = new ChatFormatMode(this, "arenaPlayerChatFormat")
		{

			@Override
			public String getValue()
			{
				return arenaPlayerChatFormat;
			}

			@Override
			public void setValue(final String newValue) throws CrazyException
			{
				arenaPlayerChatFormat = newValue;
				saveConfiguration();
			}
		};
		modeCommand.addMode(arenaPlayerChatFormatMode);
		chatsModeCommand.addMode(arenaPlayerChatFormatMode);
		final Mode<?> arenaJudgeChatFormatMode = new ChatFormatMode(this, "arenaJudgeChatFormat")
		{

			@Override
			public String getValue()
			{
				return arenaJudgeChatFormat;
			}

			@Override
			public void setValue(final String newValue) throws CrazyException
			{
				arenaJudgeChatFormat = newValue;
				saveConfiguration();
			}
		};
		modeCommand.addMode(arenaJudgeChatFormatMode);
		chatsModeCommand.addMode(arenaJudgeChatFormatMode);
	}

	private void registerCommands()
	{
		getCommand("arenajoin").setExecutor(new PlayerCommandJoin(this));
		getCommand("arenaspectate").setExecutor(new PlayerCommandSpectate(this));
		getCommand("arenajudge").setExecutor(new PlayerCommandJudge(this));
		getCommand("arenainvite").setExecutor(new PlayerCommandInvite(this));
		getCommand("arenaready").setExecutor(new PlayerCommandReady(this));
		getCommand("arenateam").setExecutor(new PlayerCommandTeam(this));
		getCommand("arenaleave").setExecutor(new PlayerCommandLeave(this));
		mainCommand.addSubCommand(new CommandMainCreate(this), "create", "new");
		mainCommand.addSubCommand(new CommandMainImport(this), "import");
		mainCommand.addSubCommand(new CommandMainSelect(this), "select");
		mainCommand.addSubCommand(new CommandMainEnable(this), "enable");
		mainCommand.addSubCommand(new CommandMainEdit(this), "edit");
		mainCommand.addSubCommand(new CommandMainForceReady(this), "forceready");
		mainCommand.addSubCommand(new CommandMainKick(this), "kick");
		mainCommand.addSubCommand(new CommandMainForceStop(this), "forcestop");
		mainCommand.addSubCommand(new CommandMainDisable(this), "disable");
		mainCommand.addSubCommand(new CommandMainDelete(this), "delete", "remove");
		mainCommand.addSubCommand(modeCommand, "mode");
		mainCommand.setDefaultExecutor(new CommandMainTreeDefault(this));
	}

	private void registerHooks()
	{
		final PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new CrazyArenaPlayerListener(this), this);
		crazyChatsEnabled = Bukkit.getPluginManager().getPlugin("CrazyChats") != null;
		if (crazyChatsEnabled)
			pm.registerEvents(new CrazyArenaCrazyChatsListener(this), this);
	}

	@Override
	public void onLoad()
	{
		plugin = this;
		super.onLoad();
	}

	@Override
	public void onEnable()
	{
		registerHooks();
		if (crazyChatsEnabled)
		{
			registerModesCrazyChats();
			CrazyChatsChatHelper.CHATFORMATPARAMETERS.add(new ArenaChatFormatParameters(this));
		}
		super.onEnable();
		registerCommands();
	}

	@Override
	public void onDisable()
	{
		for (final Arena<?> arena : arenas)
			arena.shutdown();
		super.onDisable();
	}

	@Override
	@Localized({ "CRAZYARENA.ARENA.FILENOTFOUND $Arena$", "CRAZYARENA.ARENA.LOADED $Amount$" })
	public void loadConfiguration()
	{
		super.loadConfiguration();
		final ConfigurationSection config = getConfig();
		arenas.clear();
		arenasByName.clear();
		for (final Set<Arena<?>> type : arenasByType.values())
			type.clear();
		arenasByType.clear();
		for (final String type : CrazyArenaPlugin.getRegisteredArenaTypes().keySet())
			arenasByType.put(type, new HashSet<Arena<?>>());
		arenasByPlayer.clear();
		invitations.clear();
		selections.clear();
		final List<String> arenaList = config.getStringList("arenas");
		int loadedArenas = 0;
		if (arenas != null)
			for (final String name : arenaList)
				try
				{
					final Arena<?> arena = Arena.loadFromFile(name);
					loadedArenas++;
					arenas.add(arena);
					arenasByName.put(name.toLowerCase(), arena);
					arenasByType.get(arena.getType().toLowerCase()).add(arena);
					new CrazyArenaArenaCreateEvent(arena, true).callEvent();
				}
				catch (final FileNotFoundException e)
				{
					broadcastLocaleMessage(true, "crazyarena.warnloaderror", "ARENA.FILENOTFOUND", name);
				}
				catch (final Exception e)
				{}
		sendLocaleMessage("ARENA.LOADED", Bukkit.getConsoleSender(), loadedArenas);
		if (crazyChatsEnabled)
		{
			arenaChatFormat = CrazyChatsChatHelper.makeFormat(config.getString("arenaChatFormat", "&A[Arena:$A0$] &F%1$s&F: &B%2$s"));
			arenaSpectatorChatFormat = CrazyChatsChatHelper.makeFormat(config.getString("arenaSpectatorChatFormat", "&A[Arena/S:$A0$] &F%1$s&F: &B%2$s"));
			arenaPlayerChatFormat = CrazyChatsChatHelper.makeFormat(config.getString("arenaPlayerChatFormat", "&A[Arena/P:$A0$] &F%1$s&F: &B%2$s"));
			arenaJudgeChatFormat = CrazyChatsChatHelper.makeFormat(config.getString("arenaJudgeChatFormat", "&A[Arena/&CJ&A:$A0$] &F%1$s&F: &B%2$s"));
		}
	}

	@Override
	public void saveConfiguration()
	{
		final ConfigurationSection config = getConfig();
		final LinkedList<String> names = new LinkedList<String>();
		for (final Arena<?> arena : arenas)
		{
			arena.saveToFile();
			names.add(arena.getName());
		}
		config.set("arenas", names);
		if (crazyChatsEnabled)
			config.set("arenaChatFormat", CrazyChatsChatHelper.unmakeFormat(arenaChatFormat));
		super.saveConfiguration();
	}

	public Set<Arena<?>> getArenas()
	{
		return arenas;
	}

	public Map<String, Arena<?>> getArenasByName()
	{
		return arenasByName;
	}

	public Arena<?> getArenaByName(final String name)
	{
		return arenasByName.get(name.toLowerCase());
	}

	/**
	 * Tries to find a arena based on the given args. <br>
	 * 1) ? return a random arena.<br>
	 * 2) ?_<Type> return a random arena with the given type.<br>
	 * 3) ?<Name> return a random arena, matching args. <br>
	 * 4) <Name> return an arena with the given name.<br>
	 * 5) <Player> return the arena the player is currently in.
	 * 
	 * @param param
	 *            Specifies the desired arena.
	 * @return The arena specified by param.
	 */
	public Arena<?> getArenaAdvanced(final String param)
	{
		if (param.startsWith("?"))
			if (param.length() == 1)
				return selectRandom(arenas);
			else if (param.startsWith("_", 1))
				return selectRandom(getArenasByType(param.substring(2)));
			else
				return selectRandom(searchArenasPattern(param.substring(1)));
		else
		{
			final Arena<?> arena = plugin.getArenaByName(param);
			if (arena != null)
				return arena;
			Player to = Bukkit.getPlayerExact(param);
			if (to == null)
				to = Bukkit.getPlayer(param);
			return plugin.getArenaByPlayer(to);
		}
	}

	private Arena<?> selectRandom(final Collection<Arena<?>> arenas)
	{
		if (arenas == null)
			return null;
		else if (arenas.size() == 0)
			return null;
		else
			return new ArrayList<Arena<?>>(arenas).get(random.nextInt(arenas.size()));
	}

	public Set<Arena<?>> searchArenas(String name)
	{
		name = name.toLowerCase();
		final HashSet<Arena<?>> arenas = new HashSet<Arena<?>>();
		for (final Entry<String, Arena<?>> entry : arenasByName.entrySet())
			if (entry.getKey().startsWith(name))
				arenas.add(entry.getValue());
		return arenas;
	}

	public Set<Arena<?>> searchArenasPattern(final String pattern)
	{
		final HashSet<Arena<?>> arenas = new HashSet<Arena<?>>();
		final Pattern pattern_Name = Pattern.compile(pattern.toLowerCase());
		for (final Entry<String, Arena<?>> entry : arenasByName.entrySet())
			if (pattern_Name.matcher(entry.getKey()).find())
				arenas.add(entry.getValue());
		return arenas;
	}

	public SortedSet<String> searchArenaNames(final String name)
	{
		final SortedSet<String> arenas = new TreeSet<String>();
		for (final Arena<?> arena : searchArenas(name))
			arenas.add(arena.getName());
		return arenas;
	}

	public Map<String, Set<Arena<?>>> getArenasByType()
	{
		return arenasByType;
	}

	public Set<Arena<?>> getArenasByType(final String name)
	{
		return arenasByType.get(name.toLowerCase());
	}

	public Map<Player, Arena<?>> getArenaByPlayer()
	{
		return arenasByPlayer;
	}

	public Arena<?> getArenaByPlayer(final Player player)
	{
		return arenasByPlayer.get(player);
	}

	public Map<CommandSender, Arena<?>> getSelections()
	{
		return selections;
	}

	public Map<Player, Arena<?>> getInvitations()
	{
		return invitations;
	}

	public String getArenaChatFormat()
	{
		return arenaChatFormat;
	}

	public String getArenaSpectatorChatFormat()
	{
		return arenaSpectatorChatFormat;
	}

	public String getArenaPlayerChatFormat()
	{
		return arenaPlayerChatFormat;
	}

	public String getArenaJudgeChatFormat()
	{
		return arenaJudgeChatFormat;
	}
}
