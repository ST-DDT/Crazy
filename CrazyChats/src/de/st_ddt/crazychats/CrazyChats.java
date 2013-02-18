package de.st_ddt.crazychats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazychats.channels.AbstractChannel;
import de.st_ddt.crazychats.channels.AdminChannel;
import de.st_ddt.crazychats.channels.BroadcastChannel;
import de.st_ddt.crazychats.channels.ControlledChannelInterface;
import de.st_ddt.crazychats.channels.CustomChannel;
import de.st_ddt.crazychats.channels.GlobalChannel;
import de.st_ddt.crazychats.channels.LocalChannel;
import de.st_ddt.crazychats.channels.WorldChannel;
import de.st_ddt.crazychats.commands.CrazyChatsCommandClearChat;
import de.st_ddt.crazychats.commands.CrazyChatsCommandColorHelp;
import de.st_ddt.crazychats.commands.CrazyChatsCommandGroupListnamePrefix;
import de.st_ddt.crazychats.commands.CrazyChatsCommandGroupPrefix;
import de.st_ddt.crazychats.commands.CrazyChatsCommandGroupSuffix;
import de.st_ddt.crazychats.commands.CrazyChatsCommandMainReload;
import de.st_ddt.crazychats.commands.CrazyChatsCommandPlayerDisplayName;
import de.st_ddt.crazychats.commands.CrazyChatsCommandPlayerHeadName;
import de.st_ddt.crazychats.commands.CrazyChatsCommandPlayerListName;
import de.st_ddt.crazychats.commands.CrazyChatsCommandPlayerMute;
import de.st_ddt.crazychats.commands.CrazyChatsCommandPlayerSilence;
import de.st_ddt.crazychats.commands.CrazyChatsCommandPlayerUnmute;
import de.st_ddt.crazychats.commands.CrazyChatsCommandSay;
import de.st_ddt.crazychats.commands.CrazyChatsCommandServerSilence;
import de.st_ddt.crazychats.commands.CrazyChatsCommandTell;
import de.st_ddt.crazychats.commands.CrazyChatsPlayerCommandAnswer;
import de.st_ddt.crazychats.commands.CrazyChatsPlayerCommandChatAdd;
import de.st_ddt.crazychats.commands.CrazyChatsPlayerCommandChatChannel;
import de.st_ddt.crazychats.commands.CrazyChatsPlayerCommandChatRemove;
import de.st_ddt.crazychats.commands.CrazyChatsPlayerCommandChatTo;
import de.st_ddt.crazychats.commands.CrazyChatsPlayerCommandMuteAll;
import de.st_ddt.crazychats.commands.CrazyChatsPlayerCommandMuteChannel;
import de.st_ddt.crazychats.commands.CrazyChatsPlayerCommandMutePlayer;
import de.st_ddt.crazychats.commands.CrazyChatsPlayerCommandUnmuteChannel;
import de.st_ddt.crazychats.commands.CrazyChatsPlayerCommandUnmutePlayer;
import de.st_ddt.crazychats.data.ChatPlayerData;
import de.st_ddt.crazychats.databases.CrazyChatsConfigurationDatabase;
import de.st_ddt.crazychats.listener.CrazyChatsCrazyListener;
import de.st_ddt.crazychats.listener.CrazyChatsGameListener;
import de.st_ddt.crazychats.listener.CrazyChatsPlayerListener;
import de.st_ddt.crazychats.listener.CrazyChatsPlayerListener_125;
import de.st_ddt.crazychats.listener.CrazyChatsPlayerListener_132;
import de.st_ddt.crazychats.listener.CrazyChatsTagAPIListener;
import de.st_ddt.crazyplugin.CrazyPlayerDataPlugin;
import de.st_ddt.crazyplugin.commands.CrazyCommandTreeExecutor;
import de.st_ddt.crazyplugin.commands.CrazyPluginCommandMainMode;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.CrazyChatsChatHelper;
import de.st_ddt.crazyutil.VersionComparator;
import de.st_ddt.crazyutil.databases.DatabaseType;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modes.BooleanFalseMode;
import de.st_ddt.crazyutil.modes.BooleanTrueMode;
import de.st_ddt.crazyutil.modes.ChatFormatMode;
import de.st_ddt.crazyutil.modes.DoubleMode;
import de.st_ddt.crazyutil.modes.DurationMode;
import de.st_ddt.crazyutil.modes.Mode;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public final class CrazyChats extends CrazyPlayerDataPlugin<ChatPlayerData, ChatPlayerData>
{

	private static CrazyChats plugin;
	private final Map<Player, String> lastPrivateChatSenders = new HashMap<Player, String>();
	private final Map<String, String> groupPrefixes = new LinkedHashMap<String, String>();
	private final Map<String, String> groupSuffixes = new LinkedHashMap<String, String>();
	private final Map<String, String> groupListnamePrefixes = new LinkedHashMap<String, String>();
	private final BroadcastChannel broadcastChannel = new BroadcastChannel();
	private final GlobalChannel globalChannel = new GlobalChannel();
	private final Map<String, WorldChannel> worldChannels = Collections.synchronizedMap(new HashMap<String, WorldChannel>());
	private final LocalChannel localChannel = new LocalChannel(this);
	private final AdminChannel adminChannel = new AdminChannel();
	private final Set<ControlledChannelInterface> controlledChannels = Collections.synchronizedSet(new HashSet<ControlledChannelInterface>());
	private final Map<Integer, CustomChannel> customChannels = Collections.synchronizedMap(new HashMap<Integer, CustomChannel>());
	private CrazyChatsPlayerListener playerListener;
	private int newChannelID;
	private String consoleDisplayName = "[CONSOLE]";
	private String broadcastChatFormat = "[All]%1$s: %2$s";
	private String globalChatFormat = "[Global]%1$s: %2$s";
	private String worldChatFormat = "[World]%1$s: %2$s";
	private boolean localChatEnabled = true;
	private String localChatFormat = "[Local]%1$s: %2$s";
	private double localChatRange = 50;
	private String privateChatFormat = "[Private]%1$s: %2$s";
	private boolean privateChatSpyingEnabled = true;
	private String adminChatFormat = "[Admin]%1$s: %2$s";
	private String ownChatNamePrefix = ChatColor.ITALIC.toString();
	private String defaultChannelKey;
	private long serverSilence = getNow();
	private long maxSilenceTime;
	private boolean cleanRepetitions;
	private boolean cleanCaps;
	private int clearChatLength;
	private boolean tagAPIenabled;

	public static CrazyChats getPlugin()
	{
		return plugin;
	}

	public CrazyChats()
	{
		super();
		registerModes();
	}

	@Localized("CRAZYCHATS.MODE.CHANGE $Name$ $Value$")
	private void registerModes()
	{
		modeCommand.addMode(new Mode<String>(this, "consoleDisplayName", String.class)
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				plugin.sendLocaleMessage("MODE.CHANGE", sender, name, getValue() + ChatColor.RESET);
			}

			@Override
			public String getValue()
			{
				return consoleDisplayName;
			}

			@Override
			public void setValue(final CommandSender sender, final String... args) throws CrazyException
			{
				setValue(ChatHelper.colorise(ChatHelper.listingString(" ", args)));
				showValue(sender);
			}

			@Override
			public void setValue(final String newValue) throws CrazyException
			{
				consoleDisplayName = newValue;
				saveConfiguration();
			}

			@Override
			public List<String> tab(final String... args)
			{
				if (args.length != 1 && args[0].length() != 0)
					return null;
				final List<String> res = new ArrayList<String>(5);
				res.add(getValue());
				res.add("&DCONSOLE");
				res.add("&DConsole");
				res.add("&DSERVER");
				res.add("&DServer");
				return res;
			}
		});
		modeCommand.addMode(new ChatFormatMode(this, "broadcastChatFormat")
		{

			@Override
			public String getValue()
			{
				return broadcastChatFormat;
			}

			@Override
			public void setValue(final String newValue) throws CrazyException
			{
				broadcastChatFormat = newValue;
				saveConfiguration();
			}
		});
		modeCommand.addMode(new ChatFormatMode(this, "globalChatFormat")
		{

			@Override
			public String getValue()
			{
				return globalChatFormat;
			}

			@Override
			public void setValue(final String newValue) throws CrazyException
			{
				globalChatFormat = newValue;
				saveConfiguration();
			}
		});
		modeCommand.addMode(new ChatFormatMode(this, "worldChatFormat")
		{

			@Override
			public String getValue()
			{
				return worldChatFormat;
			}

			@Override
			public void setValue(final String newValue) throws CrazyException
			{
				worldChatFormat = newValue;
				saveConfiguration();
			}
		});
		modeCommand.addMode(new BooleanTrueMode(this, "localChatEnabled")
		{

			@Override
			public Boolean getValue()
			{
				return localChatEnabled;
			}

			@Override
			public void setValue(final Boolean newValue) throws CrazyException
			{
				localChatEnabled = newValue;
				localChannel.setEnabled(newValue);
				saveConfiguration();
			}
		});
		modeCommand.addMode(new ChatFormatMode(this, "localChatFormat")
		{

			@Override
			public String getValue()
			{
				return localChatFormat;
			}

			@Override
			public void setValue(final String newValue) throws CrazyException
			{
				localChatFormat = newValue;
				saveConfiguration();
			}
		});
		modeCommand.addMode(new DoubleMode(this, "localChatRange")
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				sendLocaleMessage("MODE.CHANGE", sender, name, getValue() + " blocks");
			}

			@Override
			public Double getValue()
			{
				return localChatRange;
			}

			@Override
			public void setValue(final Double newValue) throws CrazyException
			{
				localChatRange = Math.max(1, newValue);
				saveConfiguration();
			}
		});
		modeCommand.addMode(new ChatFormatMode(this, "privateChatFormat")
		{

			@Override
			public String getValue()
			{
				return privateChatFormat;
			}

			@Override
			public void setValue(final String newValue) throws CrazyException
			{
				privateChatFormat = newValue;
				saveConfiguration();
			}
		});
		modeCommand.addMode(new BooleanFalseMode(this, "privateChatSpyingEnabled")
		{

			@Override
			public Boolean getValue()
			{
				return privateChatSpyingEnabled;
			}

			@Override
			public void setValue(final Boolean newValue) throws CrazyException
			{
				privateChatSpyingEnabled = newValue;
				saveConfiguration();
			}
		});
		modeCommand.addMode(new ChatFormatMode(this, "adminChatFormat")
		{

			@Override
			public String getValue()
			{
				return adminChatFormat;
			}

			@Override
			public void setValue(final String newValue) throws CrazyException
			{
				adminChatFormat = newValue;
				saveConfiguration();
			}
		});
		modeCommand.addMode(new Mode<String>(this, "ownChatNamePrefix", String.class)
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				sendLocaleMessage("FORMAT.CHANGE", sender, name, getValue(), ownChatNamePrefix + "Playername" + ChatColor.RESET);
			}

			@Override
			public String getValue()
			{
				return ChatHelper.decolorise(ownChatNamePrefix);
			}

			@Override
			public void setValue(final CommandSender sender, final String... args) throws CrazyException
			{
				setValue(ChatHelper.colorise(ChatHelper.listingString(" ", args)));
				showValue(sender);
			}

			@Override
			public void setValue(final String newValue) throws CrazyException
			{
				ownChatNamePrefix = newValue;
				saveConfiguration();
			}
		});
		modeCommand.addMode(new Mode<String>(this, "defaultChannelKey", String.class)
		{

			@Override
			public String getValue()
			{
				return defaultChannelKey;
			}

			@Override
			public void setValue(final CommandSender sender, final String... args) throws CrazyException
			{
				setValue(ChatHelper.listingString(" ", args));
				showValue(sender);
			}

			@Override
			public void setValue(final String newValue) throws CrazyException
			{
				defaultChannelKey = newValue;
				saveConfiguration();
			}
		});
		modeCommand.addMode(new DurationMode(this, "maxSilenceTime")
		{

			@Override
			public Long getValue()
			{
				return maxSilenceTime;
			}

			@Override
			public void setValue(final Long newValue) throws CrazyException
			{
				maxSilenceTime = newValue;
				saveConfiguration();
			}
		});
		modeCommand.addMode(new BooleanFalseMode(this, "cleanRepetitions")
		{

			@Override
			public Boolean getValue()
			{
				return cleanRepetitions;
			}

			@Override
			public void setValue(final Boolean newValue) throws CrazyException
			{
				cleanRepetitions = newValue;
				saveConfiguration();
			}
		});
		modeCommand.addMode(new BooleanFalseMode(this, "cleanCaps")
		{

			@Override
			public Boolean getValue()
			{
				return cleanCaps;
			}

			@Override
			public void setValue(final Boolean newValue) throws CrazyException
			{
				cleanCaps = newValue;
				saveConfiguration();
			}
		});
	}

	public final CrazyPluginCommandMainMode getModeCommand()
	{
		return modeCommand;
	}

	private void registerCommands()
	{
		getCommand("tell").setExecutor(new CrazyChatsCommandTell(this));
		getCommand("answer").setExecutor(new CrazyChatsPlayerCommandAnswer(this));
		getCommand("say").setExecutor(new CrazyChatsCommandSay(this));
		getCommand("colorhelp").setExecutor(new CrazyChatsCommandColorHelp(this));
		getCommand("chatto").setExecutor(new CrazyChatsPlayerCommandChatTo(this));
		getCommand("chatadd").setExecutor(new CrazyChatsPlayerCommandChatAdd(this));
		getCommand("chatremove").setExecutor(new CrazyChatsPlayerCommandChatRemove(this));
		getCommand("chatchannel").setExecutor(new CrazyChatsPlayerCommandChatChannel(this));
		getCommand("muteplayer").setExecutor(new CrazyChatsPlayerCommandMutePlayer(this));
		getCommand("unmuteplayer").setExecutor(new CrazyChatsPlayerCommandUnmutePlayer(this));
		getCommand("mutechannel").setExecutor(new CrazyChatsPlayerCommandMuteChannel(this));
		getCommand("unmutechannel").setExecutor(new CrazyChatsPlayerCommandUnmuteChannel(this));
		getCommand("muteall").setExecutor(new CrazyChatsPlayerCommandMuteAll(this));
		getCommand("clearchat").setExecutor(new CrazyChatsCommandClearChat(this));
		getCommand("serversilence").setExecutor(new CrazyChatsCommandServerSilence(this));
		mainCommand.addSubCommand(modeCommand, "mode");
		mainCommand.addSubCommand(new CrazyChatsCommandMainReload(this), "reload");
		playerCommand.addSubCommand(new CrazyChatsCommandPlayerDisplayName(this), "displayname", "dispname", "dname");
		if (tagAPIenabled)
			playerCommand.addSubCommand(new CrazyChatsCommandPlayerHeadName(this), "headname", "hname");
		playerCommand.addSubCommand(new CrazyChatsCommandPlayerListName(this), "listname", "lname");
		playerCommand.addSubCommand(new CrazyChatsCommandPlayerSilence(this), "silence", "globalmute");
		playerCommand.addSubCommand(new CrazyChatsCommandPlayerMute(this), "mute");
		playerCommand.addSubCommand(new CrazyChatsCommandPlayerUnmute(this), "unmute");
		final CrazyCommandTreeExecutor<CrazyChats> groupCommand = new CrazyCommandTreeExecutor<CrazyChats>(this);
		groupCommand.addSubCommand(new CrazyChatsCommandGroupPrefix(this), "prefix");
		groupCommand.addSubCommand(new CrazyChatsCommandGroupSuffix(this), "suffix");
		groupCommand.addSubCommand(new CrazyChatsCommandGroupListnamePrefix(this), "lnameprefix", "listnameprefix");
	}

	private void registerHooks()
	{
		final String mcVersion = ChatHelper.getMinecraftVersion();
		if (VersionComparator.compareVersions(mcVersion, "1.2.5") == 1)
			this.playerListener = new CrazyChatsPlayerListener_132(this);
		else
			this.playerListener = new CrazyChatsPlayerListener_125(this);
		final PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(playerListener, this);
		pm.registerEvents(new CrazyChatsCrazyListener(this), this);
		pm.registerEvents(new CrazyChatsGameListener(this), this);
		tagAPIenabled = Bukkit.getPluginManager().getPlugin("TagAPI") != null;
		if (tagAPIenabled)
			pm.registerEvents(new CrazyChatsTagAPIListener(this), this);
	}

	@Override
	public void onLoad()
	{
		plugin = this;
		AbstractChannel.setPlugin(this);
		super.onLoad();
	}

	@Override
	public void onEnable()
	{
		registerHooks();
		for (final World world : Bukkit.getWorlds())
			worldChannels.put(world.getName(), new WorldChannel(world));
		super.onEnable();
		registerCommands();
		// Online Players
		for (final Player player : Bukkit.getOnlinePlayers())
			playerListener.PlayerJoinEnabled(player);
	}

	@SuppressWarnings("deprecation")
	@Override
	@Localized({ "CRAZYCHATS.DATABASE.ACCESSWARN $SaveType$", "CRAZYCHATS.DATABASE.LOADED $EntryCount$" })
	public void loadDatabase()
	{
		final ConfigurationSection config = getConfig();
		final String saveType = config.getString("database.saveType", "CONFIG").toUpperCase();
		DatabaseType type = null;
		try
		{
			type = DatabaseType.valueOf(saveType);
		}
		catch (final Exception e)
		{
			consoleLog(ChatColor.RED + "NO SUCH SAVETYPE " + saveType);
		}
		if (type == DatabaseType.CONFIG)
			database = new CrazyChatsConfigurationDatabase(this, config.getConfigurationSection("database.CONFIG"));
		if (database != null)
			try
			{
				database.save(config, "database.");
				database.initialize();
			}
			catch (final Exception e)
			{
				e.printStackTrace();
				database = null;
			}
		if (database == null)
		{
			broadcastLocaleMessage(true, "crazychats.warndatabase", "DATABASE.ACCESSWARN", saveType);
			Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable()
			{

				@Override
				public void run()
				{
					if (database == null)
						broadcastLocaleMessage(true, "crazychats.warndatabase", "DATABASE.ACCESSWARN", saveType);
				}
			}, 600, 600);
		}
		else
			sendLocaleMessage("DATABASE.LOADED", Bukkit.getConsoleSender(), database.getAllEntries().size());
	}

	@Override
	public void loadConfiguration()
	{
		super.loadConfiguration();
		final ConfigurationSection config = getConfig();
		groupPrefixes.clear();
		groupPrefixes.put("nogroup", "");
		final ConfigurationSection groupPrefixConfig = config.getConfigurationSection("groupPrefix");
		if (groupPrefixConfig == null)
		{
			groupPrefixes.put("op", "&C[Admin]&F");
			groupPrefixes.put("default", "&A[User]&F");
		}
		else
			for (final String key : groupPrefixConfig.getKeys(false))
				groupPrefixes.put(key, ChatHelper.colorise(groupPrefixConfig.getString(key, "")));
		groupSuffixes.clear();
		groupSuffixes.put("nogroup", "");
		final ConfigurationSection groupSuffixConfig = config.getConfigurationSection("groupSuffix");
		if (groupSuffixConfig == null)
		{
			groupSuffixes.put("op", "");
			groupSuffixes.put("default", "");
		}
		else
			for (final String key : groupSuffixConfig.getKeys(false))
				groupSuffixes.put(key, ChatHelper.colorise(groupSuffixConfig.getString(key, "")));
		groupListnamePrefixes.clear();
		groupListnamePrefixes.put("nogroup", "");
		final ConfigurationSection groupListnamePrefixConfig = config.getConfigurationSection("groupListnamePrefix");
		if (groupListnamePrefixConfig == null)
		{
			groupListnamePrefixes.put("op", "");
			groupListnamePrefixes.put("default", "");
		}
		else
			for (final String key : groupListnamePrefixConfig.getKeys(false))
				groupListnamePrefixes.put(key, ChatHelper.colorise(groupListnamePrefixConfig.getString(key, "")));
		newChannelID = config.getInt("newChannelID", config.getInt("newID", 0));
		final ConfigurationSection customChannelConfig = config.getConfigurationSection("customChannels");
		customChannels.clear();
		if (customChannelConfig != null)
			for (final String key : customChannelConfig.getKeys(false))
			{
				final CustomChannel channel = new CustomChannel(this, customChannelConfig.getConfigurationSection(key));
				customChannels.put(channel.getId(), channel);
			}
		consoleDisplayName = ChatHelper.colorise(config.getString("consoleDisplayName", "&DCONSOLE"));
		broadcastChatFormat = CrazyChatsChatHelper.makeFormat(config.getString("broadcastChatFormat", "&C&L[All] &F%1$s&F: &E%2$s"));
		globalChatFormat = CrazyChatsChatHelper.makeFormat(config.getString("globalChatFormat", "&6[Global] &F%1$s&F: &F%2$s"));
		worldChatFormat = CrazyChatsChatHelper.makeFormat(config.getString("worldChatFormat", "&A[World] &F%1$s&F: &F%2$s"));
		localChatEnabled = config.getBoolean("localChatEnabled", true);
		localChatFormat = CrazyChatsChatHelper.makeFormat(config.getString("localChatFormat", "&2[Local] &F%1$s&F: &F%2$s"));
		localChatRange = config.getDouble("localChatRange", 30);
		privateChatFormat = CrazyChatsChatHelper.makeFormat(config.getString("privateChatFormat", "&7[Private] &F%1$s&F: &F%2$s"));
		privateChatSpyingEnabled = config.getBoolean("privateChatSpyingEnabled", false);
		adminChatFormat = CrazyChatsChatHelper.makeFormat(config.getString("adminChatFormat", "&C[Admin] &F%1$s&F: &F%2$s"));
		ownChatNamePrefix = ChatHelper.colorise(config.getString("ownChatNamePrefix", ChatColor.ITALIC.toString()));
		defaultChannelKey = config.getString("defaultChannelKey", "w");
		maxSilenceTime = config.getLong("maxSilenceTime", 31556952000L);
		cleanRepetitions = config.getBoolean("cleanRepetitions", true);
		cleanCaps = config.getBoolean("cleanCaps", true);
		clearChatLength = config.getInt("clearChatLength", 100);
		// Logger
		logger.createLogChannels(config.getConfigurationSection("logs"), "Chat");
	}

	@Override
	public void saveConfiguration()
	{
		final ConfigurationSection config = getConfig();
		config.set("groupPrefix", null);
		for (final Entry<String, String> entry : groupPrefixes.entrySet())
			config.set("groupPrefix." + entry.getKey(), ChatHelper.decolorise(entry.getValue()));
		config.set("groupSuffix", null);
		for (final Entry<String, String> entry : groupSuffixes.entrySet())
			config.set("groupSuffix." + entry.getKey(), ChatHelper.decolorise(entry.getValue()));
		config.set("groupListnamePrefix", null);
		for (final Entry<String, String> entry : groupListnamePrefixes.entrySet())
			config.set("groupListnamePrefix." + entry.getKey(), ChatHelper.decolorise(entry.getValue()));
		config.set("newChannelID", newChannelID);
		config.set("customChannels", null);
		synchronized (customChannels)
		{
			for (final CustomChannel channel : customChannels.values())
				channel.simpleSave(config, "customChannels.");
		}
		config.set("consoleDisplayName", ChatHelper.decolorise(consoleDisplayName));
		config.set("broadcastChatFormat", CrazyChatsChatHelper.unmakeFormat(broadcastChatFormat));
		config.set("globalChatFormat", CrazyChatsChatHelper.unmakeFormat(globalChatFormat));
		config.set("worldChatFormat", CrazyChatsChatHelper.unmakeFormat(worldChatFormat));
		config.set("localChatEnabled", localChatEnabled);
		config.set("localChatFormat", CrazyChatsChatHelper.unmakeFormat(localChatFormat));
		config.set("localChatRange", localChatRange);
		config.set("privateChatFormat", CrazyChatsChatHelper.unmakeFormat(privateChatFormat));
		config.set("privateChatSpyingEnabled", privateChatSpyingEnabled);
		config.set("adminChatFormat", CrazyChatsChatHelper.unmakeFormat(adminChatFormat));
		config.set("ownChatNamePrefix", ChatHelper.decolorise(ownChatNamePrefix));
		config.set("defaultChannelKey", defaultChannelKey);
		config.set("maxSilenceTime", maxSilenceTime);
		config.set("cleanRepetitions", cleanRepetitions);
		config.set("cleanCaps", cleanCaps);
		config.set("clearChatLength", clearChatLength);
		super.saveConfiguration();
	}

	public Map<Player, String> getLastPrivateChatSenders()
	{
		return lastPrivateChatSenders;
	}

	public Player getLastPrivateChatSender(final Player player)
	{
		final String name = lastPrivateChatSenders.get(player);
		if (name == null)
			return null;
		else
			return Bukkit.getPlayerExact(name);
	}

	public CrazyChatsPlayerListener getPlayerListener()
	{
		return playerListener;
	}

	public Map<String, String> getGroupPrefixes()
	{
		return groupPrefixes;
	}

	public String getGroupPrefix(final Player player)
	{
		final String prefix = PermissionModule.getGroupPrefix(player);
		if (prefix != null)
			return ChatHelper.colorise(prefix);
		final Set<String> groups = PermissionModule.getGroups(player);
		if (groups == null)
		{
			for (final Entry<String, String> entry : groupPrefixes.entrySet())
				if (!entry.getKey().equals("nogroup"))
					if (PermissionModule.hasGroup(player, entry.getKey()))
						return entry.getValue();
		}
		else
			for (final String group : groups)
			{
				final String infix = groupPrefixes.get(group);
				if (infix != null)
					return infix;
			}
		return groupPrefixes.get("nogroup");
	}

	public Map<String, String> getGroupSuffixes()
	{
		return groupSuffixes;
	}

	public String getGroupSuffix(final Player player)
	{
		final String suffix = PermissionModule.getGroupPrefix(player);
		if (suffix != null)
			return ChatHelper.colorise(suffix);
		final Set<String> groups = PermissionModule.getGroups(player);
		if (groups == null)
		{
			for (final Entry<String, String> entry : groupSuffixes.entrySet())
				if (!entry.getKey().equals("nogroup"))
					if (PermissionModule.hasGroup(player, entry.getKey()))
						return entry.getValue();
		}
		else
			for (final String group : groups)
			{
				final String infix = groupSuffixes.get(group);
				if (infix != null)
					return infix;
			}
		return groupSuffixes.get("nogroup");
	}

	public Map<String, String> getGroupListnamePrefixes()
	{
		return groupListnamePrefixes;
	}

	public String getGroupListnamePrefix(final Player player)
	{
		final Set<String> groups = PermissionModule.getGroups(player);
		if (groups == null)
		{
			for (final Entry<String, String> entry : groupListnamePrefixes.entrySet())
				if (!entry.getKey().equals("nogroup"))
					if (PermissionModule.hasGroup(player, entry.getKey()))
						return entry.getValue();
		}
		else
			for (final String group : groups)
			{
				final String infix = groupListnamePrefixes.get(group);
				if (infix != null)
					return infix;
			}
		return groupListnamePrefixes.get("nogroup");
	}

	public int getNewChannelID()
	{
		return newChannelID++;
	}

	public Map<Integer, CustomChannel> getCustomChannels()
	{
		return customChannels;
	}

	public BroadcastChannel getBroadcastChannel()
	{
		return broadcastChannel;
	}

	public GlobalChannel getGlobalChannel()
	{
		return globalChannel;
	}

	public Map<String, WorldChannel> getWorldChannels()
	{
		return worldChannels;
	}

	public LocalChannel getLocalChannel()
	{
		return localChannel;
	}

	public AdminChannel getAdminChannel()
	{
		return adminChannel;
	}

	public Set<ControlledChannelInterface> getControlledChannels()
	{
		return controlledChannels;
	}

	public String getConsoleDisplayName()
	{
		return consoleDisplayName;
	}

	public String getBroadcastChatFormat()
	{
		return broadcastChatFormat;
	}

	public String getGlobalChatFormat()
	{
		return globalChatFormat;
	}

	public String getWorldChatFormat()
	{
		return worldChatFormat;
	}

	public boolean isLocalChatEnabled()
	{
		return localChatEnabled;
	}

	public String getLocalChatFormat()
	{
		return localChatFormat;
	}

	public double getLocalChatRange()
	{
		return localChatRange;
	}

	public String getPrivateChatFormat()
	{
		return privateChatFormat;
	}

	public boolean isPrivateChatSpyingEnabled()
	{
		return privateChatSpyingEnabled;
	}

	public String getAdminChatFormat()
	{
		return adminChatFormat;
	}

	public String getOwnChatNamePrefix()
	{
		return ownChatNamePrefix;
	}

	public String getDefaultChannelKey()
	{
		return defaultChannelKey;
	}

	public boolean isServerSilenced()
	{
		return serverSilence > getNow();
	}

	public void setServerSilenced(final Date until)
	{
		setServerSilenced(until.getTime());
	}

	public void setServerSilenced(final long until)
	{
		serverSilence = until;
	}

	private long getNow()
	{
		return System.currentTimeMillis();
	}

	public long getMaxSilenceTime()
	{
		return maxSilenceTime;
	}

	public boolean isCleaningRepetitionsEnabled()
	{
		return cleanRepetitions;
	}

	public boolean isCleaningCapsEnabled()
	{
		return cleanCaps;
	}

	public int getClearChatLength()
	{
		return clearChatLength;
	}
}
