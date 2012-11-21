package de.st_ddt.crazychats;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazychats.channels.AbstractChannel;
import de.st_ddt.crazychats.channels.BroadcastChannel;
import de.st_ddt.crazychats.channels.GlobalChannel;
import de.st_ddt.crazychats.channels.GroupChannelInterface;
import de.st_ddt.crazychats.channels.LocalChannel;
import de.st_ddt.crazychats.channels.WorldChannel;
import de.st_ddt.crazychats.commands.CrazyChatsCommandPlayerDisplayName;
import de.st_ddt.crazychats.commands.CrazyChatsCommandPlayerHeadName;
import de.st_ddt.crazychats.commands.CrazyChatsCommandPlayerListName;
import de.st_ddt.crazychats.commands.CrazyChatsCommandPlayerMute;
import de.st_ddt.crazychats.commands.CrazyChatsCommandPlayerSilence;
import de.st_ddt.crazychats.commands.CrazyChatsCommandPlayerUnmute;
import de.st_ddt.crazychats.commands.CrazyChatsPlayerCommandChatAdd;
import de.st_ddt.crazychats.commands.CrazyChatsPlayerCommandChatChannel;
import de.st_ddt.crazychats.commands.CrazyChatsPlayerCommandChatRemove;
import de.st_ddt.crazychats.commands.CrazyChatsPlayerCommandChatTo;
import de.st_ddt.crazychats.commands.CrazyChatsPlayerCommandMuteAll;
import de.st_ddt.crazychats.commands.CrazyChatsPlayerCommandMuteChannel;
import de.st_ddt.crazychats.commands.CrazyChatsPlayerCommandMutePlayer;
import de.st_ddt.crazychats.commands.CrazyChatsPlayerCommandTell;
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
import de.st_ddt.crazychats.tasks.GroupChannelCleanupTask;
import de.st_ddt.crazyplugin.CrazyPlayerDataPlugin;
import de.st_ddt.crazyplugin.commands.CrazyPluginCommandMainMode;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.CrazyChatsChatHelper;
import de.st_ddt.crazyutil.VersionComparator;
import de.st_ddt.crazyutil.databases.DatabaseType;
import de.st_ddt.crazyutil.locales.Localized;

public final class CrazyChats extends CrazyPlayerDataPlugin<ChatPlayerData, ChatPlayerData>
{

	private static CrazyChats plugin;
	private final BroadcastChannel broadcastChannel = new BroadcastChannel();
	private final GlobalChannel globalChannel = new GlobalChannel();
	private final Map<String, WorldChannel> worldChannels = Collections.synchronizedMap(new HashMap<String, WorldChannel>());
	private final LocalChannel localChannel = new LocalChannel(this);
	private final Set<GroupChannelInterface> groupChannels = Collections.synchronizedSet(new HashSet<GroupChannelInterface>());
	private final CrazyPluginCommandMainMode modeCommand = new CrazyPluginCommandMainMode(this);
	private CrazyChatsPlayerListener playerListener;
	private String broadcastChatFormat = "[All]%1$s: %2$s";
	private String globalChatFormat = "[Global]%1$s: %2$s";
	private String worldChatFormat = "[World]%1$s: %2$s";
	private boolean localChatEnabled = true;
	private String localChatFormat = "[Local]%1$s: %2$s";
	private double localChatRange = 50;
	private String privateChatFormat = "[Private]%1$s: %2$s";
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
		modeCommand.addMode(modeCommand.new Mode<String>("broadcastChatFormat", String.class)
		{

			@Override
			public String getValue()
			{
				return String.format(broadcastChatFormat, "Channel", "Message") + " (" + CrazyChatsChatHelper.unmakeFormat(broadcastChatFormat) + ")";
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
				broadcastChatFormat = newValue;
				saveConfiguration();
			}
		});
		modeCommand.addMode(modeCommand.new Mode<String>("globalChatFormat", String.class)
		{

			@Override
			public String getValue()
			{
				return String.format(globalChatFormat, "Channel", "Message") + " (" + CrazyChatsChatHelper.unmakeFormat(globalChatFormat) + ")";
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
				globalChatFormat = newValue;
				saveConfiguration();
			}
		});
		modeCommand.addMode(modeCommand.new Mode<String>("worldChatFormat", String.class)
		{

			@Override
			public String getValue()
			{
				return String.format(worldChatFormat, "Channel", "Message") + " (" + CrazyChatsChatHelper.unmakeFormat(worldChatFormat) + ")";
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
				worldChatFormat = newValue;
				saveConfiguration();
			}
		});
		modeCommand.addMode(modeCommand.new BooleanTrueMode("localChatEnabled")
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

			@Override
			public List<String> tab(String... args)
			{
				System.out.println("TESR");
				return super.tab(args);
			}
		});
		modeCommand.addMode(modeCommand.new Mode<String>("localChatFormat", String.class)
		{

			@Override
			public String getValue()
			{
				return String.format(localChatFormat, "Channel", "Message") + " (" + CrazyChatsChatHelper.unmakeFormat(localChatFormat) + ")";
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
				localChatFormat = newValue;
				saveConfiguration();
			}
		});
		modeCommand.addMode(modeCommand.new DoubleMode("localChatRange")
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				plugin.sendLocaleMessage("MODE.CHANGE", sender, name, getValue() + " blocks");
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
		modeCommand.addMode(modeCommand.new Mode<String>("privateChatFormat", String.class)
		{

			@Override
			public String getValue()
			{
				return String.format(privateChatFormat, "Channel", "Message") + " (" + CrazyChatsChatHelper.unmakeFormat(privateChatFormat) + ")";
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
				privateChatFormat = newValue;
				saveConfiguration();
			}
		});
	}

	private void registerCommands()
	{
		getCommand("tell").setExecutor(new CrazyChatsPlayerCommandTell(this));
		getCommand("chatto").setExecutor(new CrazyChatsPlayerCommandChatTo(this));
		getCommand("chatadd").setExecutor(new CrazyChatsPlayerCommandChatAdd(this));
		getCommand("chatremove").setExecutor(new CrazyChatsPlayerCommandChatRemove(this));
		getCommand("chatchannel").setExecutor(new CrazyChatsPlayerCommandChatChannel(this));
		getCommand("muteplayer").setExecutor(new CrazyChatsPlayerCommandMutePlayer(this));
		getCommand("unmuteplayer").setExecutor(new CrazyChatsPlayerCommandUnmutePlayer(this));
		getCommand("mutechannel").setExecutor(new CrazyChatsPlayerCommandMuteChannel(this));
		getCommand("unmutechannel").setExecutor(new CrazyChatsPlayerCommandUnmuteChannel(this));
		getCommand("muteall").setExecutor(new CrazyChatsPlayerCommandMuteAll(this));
		mainCommand.addSubCommand(modeCommand, "mode");
		playerCommand.addSubCommand(new CrazyChatsCommandPlayerDisplayName(this), "displayname", "dispname", "dname");
		if (tagAPIenabled)
			playerCommand.addSubCommand(new CrazyChatsCommandPlayerHeadName(this), "headname", "hname");
		playerCommand.addSubCommand(new CrazyChatsCommandPlayerListName(this), "listname", "lname");
		playerCommand.addSubCommand(new CrazyChatsCommandPlayerSilence(this), "silence", "globalmute");
		playerCommand.addSubCommand(new CrazyChatsCommandPlayerMute(this), "mute");
		playerCommand.addSubCommand(new CrazyChatsCommandPlayerUnmute(this), "unmute");
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
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new GroupChannelCleanupTask(this), 18000, 36000);
		super.onEnable();
		registerCommands();
		// Online Players
		for (final Player player : Bukkit.getOnlinePlayers())
			playerListener.PlayerJoinComplete(player);
	}

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
		broadcastChatFormat = CrazyChatsChatHelper.makeFormat(config.getString("broadcastChatFormat", "&L&C[All] &F%1$s&F: &E%2$s"));
		globalChatFormat = CrazyChatsChatHelper.makeFormat(config.getString("globalChatFormat", "&6[Global] &F%1$s&F: &F%2$s"));
		worldChatFormat = CrazyChatsChatHelper.makeFormat(config.getString("worldChatFormat", "&A[World] &F%1$s&F: &F%2$s"));
		localChatEnabled = config.getBoolean("localChatEnabled", true);
		localChatFormat = CrazyChatsChatHelper.makeFormat(config.getString("localChatFormat", "&2[Local] &F%1$s&F: &F%2$s"));
		localChatRange = config.getDouble("localChatRange", 50);
		privateChatFormat = CrazyChatsChatHelper.makeFormat(config.getString("privateChatFormat", "&7[Private] &F%1$s&F: &F%2$s"));
		// Logger
		logger.createLogChannels(config, "Chat");
	}

	@Override
	public void saveConfiguration()
	{
		final ConfigurationSection config = getConfig();
		config.set("broadcastChatFormat", CrazyChatsChatHelper.unmakeFormat(broadcastChatFormat));
		config.set("globalChatFormat", CrazyChatsChatHelper.unmakeFormat(globalChatFormat));
		config.set("worldChatFormat", CrazyChatsChatHelper.unmakeFormat(worldChatFormat));
		config.set("localChatEnabled", localChatEnabled);
		config.set("localChatFormat", CrazyChatsChatHelper.unmakeFormat(localChatFormat));
		config.set("localChatRange", localChatRange);
		config.set("privateChatFormat", CrazyChatsChatHelper.unmakeFormat(privateChatFormat));
		super.saveConfiguration();
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

	public Set<GroupChannelInterface> getGroupChannels()
	{
		return groupChannels;
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
}
