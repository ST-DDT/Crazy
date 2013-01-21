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
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.command.CrazyArenaCommandMainCreate;
import de.st_ddt.crazyarena.command.CrazyArenaCommandMainDelete;
import de.st_ddt.crazyarena.command.CrazyArenaCommandMainDisable;
import de.st_ddt.crazyarena.command.CrazyArenaCommandMainEnable;
import de.st_ddt.crazyarena.command.CrazyArenaCommandMainForceReady;
import de.st_ddt.crazyarena.command.CrazyArenaCommandMainForceStop;
import de.st_ddt.crazyarena.command.CrazyArenaCommandMainImport;
import de.st_ddt.crazyarena.command.CrazyArenaCommandMainKick;
import de.st_ddt.crazyarena.command.CrazyArenaCommandMainSelect;
import de.st_ddt.crazyarena.command.CrazyArenaCommandMainTreeDefault;
import de.st_ddt.crazyarena.command.CrazyArenaPlayerCommandInvite;
import de.st_ddt.crazyarena.command.CrazyArenaPlayerCommandJoin;
import de.st_ddt.crazyarena.command.CrazyArenaPlayerCommandJudge;
import de.st_ddt.crazyarena.command.CrazyArenaPlayerCommandLeave;
import de.st_ddt.crazyarena.command.CrazyArenaPlayerCommandReady;
import de.st_ddt.crazyarena.command.CrazyArenaPlayerCommandSpectate;
import de.st_ddt.crazyarena.command.CrazyArenaPlayerCommandTeam;
import de.st_ddt.crazyarena.events.CrazyArenaArenaCreateEvent;
import de.st_ddt.crazyarena.listener.CrazyArenaCrazyChatsListener;
import de.st_ddt.crazyarena.listener.CrazyArenaPlayerListener;
import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.channels.arena.ArenaChatFormatParameters;
import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.commands.CrazyPluginCommandMainMode;
import de.st_ddt.crazyplugin.commands.CrazyPluginCommandMainMode.Mode;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.CrazyChatsChatHelper;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazyArena extends CrazyPlugin
{

	private final static Set<Arena<?>> arenas = new HashSet<Arena<?>>();
	private static CrazyArena plugin;
	private final CrazyPluginCommandMainMode modeCommand = new CrazyPluginCommandMainMode(this);
	private final Map<String, Class<? extends Arena<?>>> arenaTypes = new TreeMap<String, Class<? extends Arena<?>>>();
	private final Map<String, Arena<?>> arenasByName = new TreeMap<String, Arena<?>>();
	private final Map<String, Arena<?>> arenasByPlayer = new HashMap<String, Arena<?>>();
	private final Map<String, Set<Arena<?>>> arenasByType = new TreeMap<String, Set<Arena<?>>>();
	private final Map<String, Arena<?>> invitations = new HashMap<String, Arena<?>>();
	private final Map<String, Arena<?>> selections = new HashMap<String, Arena<?>>();
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
		final Mode<?> arenaChatFormatMode = modeCommand.new Mode<String>("arenaChatFormat", String.class)
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
		final Mode<?> arenaSpectatorChatFormatMode = modeCommand.new Mode<String>("arenaSpectatorChatFormat", String.class)
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
		final Mode<?> arenaPlayerChatFormatMode = modeCommand.new Mode<String>("arenaPlayerChatFormat", String.class)
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
		final Mode<?> arenaJudgeChatFormatMode = modeCommand.new Mode<String>("arenaJudgeChatFormat", String.class)
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
		getCommand("arenajoin").setExecutor(new CrazyArenaPlayerCommandJoin(this));
		getCommand("arenaspectate").setExecutor(new CrazyArenaPlayerCommandSpectate(this));
		getCommand("arenajudge").setExecutor(new CrazyArenaPlayerCommandJudge(this));
		getCommand("arenainvite").setExecutor(new CrazyArenaPlayerCommandInvite(this));
		getCommand("arenaready").setExecutor(new CrazyArenaPlayerCommandReady(this));
		getCommand("arenateam").setExecutor(new CrazyArenaPlayerCommandTeam(this));
		getCommand("arenaleave").setExecutor(new CrazyArenaPlayerCommandLeave(this));
		mainCommand.addSubCommand(new CrazyArenaCommandMainCreate(this), "create", "new");
		mainCommand.addSubCommand(new CrazyArenaCommandMainImport(this), "import");
		mainCommand.addSubCommand(new CrazyArenaCommandMainSelect(this), "select");
		mainCommand.addSubCommand(new CrazyArenaCommandMainEnable(this), "enable");
		mainCommand.addSubCommand(new CrazyArenaCommandMainForceReady(this), "forceready");
		mainCommand.addSubCommand(new CrazyArenaCommandMainKick(this), "kick");
		mainCommand.addSubCommand(new CrazyArenaCommandMainForceStop(this), "forcestop");
		mainCommand.addSubCommand(new CrazyArenaCommandMainDisable(this), "disable");
		mainCommand.addSubCommand(new CrazyArenaCommandMainDelete(this), "delete", "remove");
		mainCommand.addSubCommand(modeCommand, "mode");
		mainCommand.setDefaultExecutor(new CrazyArenaCommandMainTreeDefault(this));
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

	public Map<String, Class<? extends Arena<?>>> getArenaTypes()
	{
		return arenaTypes;
	}

	public void registerArenaType(final String mainType, final Class<? extends Arena<?>> clazz, final String... aliases)
	{
		arenaTypes.put(mainType.toLowerCase(), clazz);
		for (final String alias : aliases)
			arenaTypes.put(alias.toLowerCase(), clazz);
		arenasByType.put(mainType.toLowerCase(), new HashSet<Arena<?>>());
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

	public Map<String, Arena<?>> getSelections()
	{
		return selections;
	}

	public Map<String, Arena<?>> getInvitations()
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
