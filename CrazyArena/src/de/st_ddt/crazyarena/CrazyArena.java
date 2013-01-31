package de.st_ddt.crazyarena;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
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
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.CrazyChatsChatHelper;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modes.Mode;

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
		final Mode<?> arenaChatFormatMode = new Mode<String>(this, "arenaChatFormat", String.class)
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				final String raw = getValue();
				plugin.sendLocaleMessage("FORMAT.CHANGE", sender, name, raw);
				plugin.sendLocaleMessage("FORMAT.EXAMPLE", sender, ChatHelper.putArgs(ChatHelper.colorise(StringUtils.replace(raw, "$A0$", "ParticipantType")), "Sender", "Message", "GroupPrefix", "GroupSuffix", "World"));
			}

			@Override
			public String getValue()
			{
				return CrazyChatsChatHelper.unmakeFormat(arenaChatFormat);
			}

			@Override
			public void setValue(final CommandSender sender, final String... args) throws CrazyException
			{
				setValue(CrazyChatsChatHelper.makeFormat(ChatHelper.listingString(" ", args)));
				showValue(sender);
			}

			@Override
			public void setValue(final String newValue) throws CrazyException
			{
				arenaChatFormat = newValue;
				saveConfiguration();
			}

			@Override
			public List<String> tab(final String... args)
			{
				if (args.length != 1 && args[0].length() != 0)
					return null;
				final List<String> res = new ArrayList<String>(1);
				res.add(getValue());
				return res;
			}
		};
		modeCommand.addMode(arenaChatFormatMode);
		chatsModeCommand.addMode(arenaChatFormatMode);
		final Mode<?> arenaSpectatorChatFormatMode = new Mode<String>(this, "arenaSpectatorChatFormat", String.class)
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				final String raw = getValue();
				plugin.sendLocaleMessage("FORMAT.CHANGE", sender, name, raw);
				plugin.sendLocaleMessage("FORMAT.EXAMPLE", sender, ChatHelper.putArgs(ChatHelper.colorise(StringUtils.replace(raw, "$A0$", "ParticipantType")), "Sender", "Message", "GroupPrefix", "GroupSuffix", "World"));
			}

			@Override
			public String getValue()
			{
				return CrazyChatsChatHelper.unmakeFormat(arenaSpectatorChatFormat);
			}

			@Override
			public void setValue(final CommandSender sender, final String... args) throws CrazyException
			{
				setValue(CrazyChatsChatHelper.makeFormat(ChatHelper.listingString(" ", args)));
				showValue(sender);
			}

			@Override
			public void setValue(final String newValue) throws CrazyException
			{
				arenaSpectatorChatFormat = newValue;
				saveConfiguration();
			}

			@Override
			public List<String> tab(final String... args)
			{
				if (args.length != 1 && args[0].length() != 0)
					return null;
				final List<String> res = new ArrayList<String>(1);
				res.add(getValue());
				return res;
			}
		};
		modeCommand.addMode(arenaSpectatorChatFormatMode);
		chatsModeCommand.addMode(arenaSpectatorChatFormatMode);
		final Mode<?> arenaPlayerChatFormatMode = new Mode<String>(this, "arenaPlayerChatFormat", String.class)
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				final String raw = getValue();
				plugin.sendLocaleMessage("FORMAT.CHANGE", sender, name, raw);
				plugin.sendLocaleMessage("FORMAT.EXAMPLE", sender, ChatHelper.putArgs(ChatHelper.colorise(StringUtils.replace(raw, "$A0$", "ParticipantType")), "Sender", "Message", "GroupPrefix", "GroupSuffix", "World"));
			}

			@Override
			public String getValue()
			{
				return CrazyChatsChatHelper.unmakeFormat(arenaPlayerChatFormat);
			}

			@Override
			public void setValue(final CommandSender sender, final String... args) throws CrazyException
			{
				setValue(CrazyChatsChatHelper.makeFormat(ChatHelper.listingString(" ", args)));
				showValue(sender);
			}

			@Override
			public void setValue(final String newValue) throws CrazyException
			{
				arenaPlayerChatFormat = newValue;
				saveConfiguration();
			}

			@Override
			public List<String> tab(final String... args)
			{
				if (args.length != 1 && args[0].length() != 0)
					return null;
				final List<String> res = new ArrayList<String>(1);
				res.add(getValue());
				return res;
			}
		};
		modeCommand.addMode(arenaPlayerChatFormatMode);
		chatsModeCommand.addMode(arenaPlayerChatFormatMode);
		final Mode<?> arenaJudgeChatFormatMode = new Mode<String>(this, "arenaJudgeChatFormat", String.class)
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				final String raw = getValue();
				plugin.sendLocaleMessage("FORMAT.CHANGE", sender, name, raw);
				plugin.sendLocaleMessage("FORMAT.EXAMPLE", sender, ChatHelper.putArgs(ChatHelper.colorise(StringUtils.replace(raw, "$A0$", "ParticipantType")), "Sender", "Message", "GroupPrefix", "GroupSuffix", "World"));
			}

			@Override
			public String getValue()
			{
				return CrazyChatsChatHelper.unmakeFormat(arenaJudgeChatFormat);
			}

			@Override
			public void setValue(final CommandSender sender, final String... args) throws CrazyException
			{
				setValue(CrazyChatsChatHelper.makeFormat(ChatHelper.listingString(" ", args)));
				showValue(sender);
			}

			@Override
			public void setValue(final String newValue) throws CrazyException
			{
				arenaJudgeChatFormat = newValue;
				saveConfiguration();
			}

			@Override
			public List<String> tab(final String... args)
			{
				if (args.length != 1 && args[0].length() != 0)
					return null;
				final List<String> res = new ArrayList<String>(1);
				res.add(getValue());
				return res;
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
			arena.shutdown();
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

	public Set<Arena<?>> searchArenas(String name)
	{
		name = name.toLowerCase();
		final HashSet<Arena<?>> arenas = new HashSet<Arena<?>>();
		for (final Entry<String, Arena<?>> entry : arenasByName.entrySet())
			if (entry.getKey().toLowerCase().startsWith(name))
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
