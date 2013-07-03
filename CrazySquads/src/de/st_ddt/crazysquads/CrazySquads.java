package de.st_ddt.crazysquads;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.PlayerDataProvider;
import de.st_ddt.crazyplugin.commands.CrazyCommandTreeExecutor;
import de.st_ddt.crazyplugin.data.PlayerDataInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazysquads.commands.CrazySquadsCommandMainCommands;
import de.st_ddt.crazysquads.commands.CrazySquadsPlayerCommandSquadCreate;
import de.st_ddt.crazysquads.commands.CrazySquadsPlayerCommandSquadJoin;
import de.st_ddt.crazysquads.commands.CrazySquadsSquadPlayerCommandSquadCommand;
import de.st_ddt.crazysquads.commands.CrazySquadsSquadPlayerCommandSquadDelete;
import de.st_ddt.crazysquads.commands.CrazySquadsSquadPlayerCommandSquadInvite;
import de.st_ddt.crazysquads.commands.CrazySquadsSquadPlayerCommandSquadKickMember;
import de.st_ddt.crazysquads.commands.CrazySquadsSquadPlayerCommandSquadLeave;
import de.st_ddt.crazysquads.commands.CrazySquadsSquadPlayerCommandSquadList;
import de.st_ddt.crazysquads.commands.CrazySquadsSquadPlayerCommandSquadMode;
import de.st_ddt.crazysquads.data.PseudoPlayerData;
import de.st_ddt.crazysquads.data.ShareRules;
import de.st_ddt.crazysquads.data.Squad;
import de.st_ddt.crazysquads.listener.CrazySquadsCrazyChatsListener;
import de.st_ddt.crazysquads.listener.CrazySquadsPlayerListener;
import de.st_ddt.crazysquads.listener.CrazySquadsTagAPIListener;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.CrazyChatsChatHelper;
import de.st_ddt.crazyutil.modes.DoubleMode;
import de.st_ddt.crazyutil.modes.IntegerMode;
import de.st_ddt.crazyutil.modes.LongMode;
import de.st_ddt.crazyutil.modes.Mode;
import de.st_ddt.crazyutil.source.Localized;

public final class CrazySquads extends CrazyPlugin implements PlayerDataProvider
{

	private static CrazySquads plugin;
	private final Map<Player, Squad> squads = new HashMap<Player, Squad>();
	private final Map<Player, Squad> invites = new HashMap<Player, Squad>();
	private final List<String> commandWhiteList = new ArrayList<String>();
	private CrazySquadsPlayerListener playerListener;
	private int maxSquadSize = 1;
	private double maxShareRange;
	private ShareRules defaultLootRules;
	private ShareRules defaultXPRules;
	private long squadAutoRejoinTime;
	private boolean crazyChatsEnabled;
	private String squadChatFormat = "[Squad]%1$s: %2$s";
	private String squadLeaderChatFormat = "[Squad]*%1$s*: %2$s";
	private boolean tagAPIEnabled;
	private String squadHeadNamePrefix = ChatColor.YELLOW.toString();

	public static CrazySquads getPlugin()
	{
		return plugin;
	}

	public CrazySquads()
	{
		super();
		registerModes();
	}

	@Localized("CRAZYSQUADS.MODE.CHANGE $Name$ $Value$")
	private void registerModes()
	{
		modeCommand.addMode(new IntegerMode(this, "maxSquadSize")
		{

			@Override
			public void setValue(final Integer newValue) throws CrazyException
			{
				maxSquadSize = newValue;
				saveConfiguration();
			}

			@Override
			public Integer getValue()
			{
				return maxSquadSize;
			}
		});
		modeCommand.addMode(new DoubleMode(this, "maxShareRange")
		{

			@Override
			public void setValue(final Double newValue) throws CrazyException
			{
				maxShareRange = newValue;
				saveConfiguration();
			}

			@Override
			public Double getValue()
			{
				return maxShareRange;
			}
		});
		modeCommand.addMode(new Mode<ShareRules>(this, "defaultLootRules", ShareRules.class)
		{

			@Override
			public ShareRules getValue()
			{
				return defaultLootRules;
			}

			@Override
			public void setValue(final CommandSender sender, final String... args) throws CrazyException
			{
				final String lootType = args[0].toUpperCase();
				try
				{
					setValue(ShareRules.valueOf(lootType));
					showValue(sender);
				}
				catch (final Exception e)
				{
					throw new CrazyCommandNoSuchException("LootRules", lootType, "LOOT_SHARE, LOOT_SHARESILENT, LOOT_PRIVATE, LOOT_PRIVATESILENT");
				}
			}

			@Override
			public void setValue(final ShareRules newValue) throws CrazyException
			{
				defaultLootRules = newValue;
				saveConfiguration();
			}

			@Override
			public List<String> tab(final String... args)
			{
				if (args.length != 1)
					return null;
				final List<String> res = new LinkedList<String>();
				final String name = args[0].toUpperCase();
				for (final ShareRules rule : ShareRules.values())
					if (rule.toString().startsWith(name))
						res.add(rule.toString());
				return res;
			}
		});
		modeCommand.addMode(new Mode<ShareRules>(this, "defaultXPRules", ShareRules.class)
		{

			@Override
			public ShareRules getValue()
			{
				return defaultXPRules;
			}

			@Override
			public void setValue(final CommandSender sender, final String... args) throws CrazyException
			{
				final String xpType = args[0].toUpperCase();
				try
				{
					setValue(ShareRules.valueOf(xpType));
					showValue(sender);
				}
				catch (final Exception e)
				{
					throw new CrazyCommandNoSuchException("LootRules", xpType, "XP_SHARE, XP_SHARESILENT, XP_PRIVATE, XP_PRIVATESILENT");
				}
			}

			@Override
			public void setValue(final ShareRules newValue) throws CrazyException
			{
				defaultXPRules = newValue;
				saveConfiguration();
			}

			@Override
			public List<String> tab(final String... args)
			{
				if (args.length != 1)
					return null;
				final List<String> res = new LinkedList<String>();
				final String name = args[0].toUpperCase();
				for (final ShareRules rule : ShareRules.values())
					if (rule.toString().startsWith(name))
						res.add(rule.toString());
				return res;
			}
		});
		modeCommand.addMode(new LongMode(this, "squadAutoRejoinTime")
		{

			@Override
			public void setValue(final Long newValue) throws CrazyException
			{
				squadAutoRejoinTime = Math.max(newValue, 0);
				saveConfiguration();
			}

			@Override
			public Long getValue()
			{
				return squadAutoRejoinTime;
			}
		});
	}

	@Localized({ "CRAZYSQUADS.FORMAT.CHANGE $FormatName$ $Value$", "CRAZYSQUADS.FORMAT.EXAMPLE $Example$" })
	private void registerModesCrazyChats()
	{
		SquadChatFormatSupport.registerChatFormats(this, modeCommand);
	}

	private void registerModesTagAPI()
	{
		modeCommand.addMode(new Mode<String>(this, "squadHeadNamePrefix", String.class)
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				sendLocaleMessage("FORMAT.CHANGE", sender, name, getValue(), squadHeadNamePrefix + "Playername");
			}

			@Override
			public String getValue()
			{
				return ChatHelper.decolorise(squadHeadNamePrefix);
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
				squadHeadNamePrefix = newValue;
				saveConfiguration();
			}
		});
	}

	private void registerCommands()
	{
		final CrazyCommandTreeExecutor<CrazySquads> squadCommand = new CrazyCommandTreeExecutor<CrazySquads>(this);
		getCommand("squad").setExecutor(squadCommand);
		squadCommand.addSubCommand(new CrazySquadsPlayerCommandSquadCreate(this), "c", "create");
		squadCommand.addSubCommand(new CrazySquadsSquadPlayerCommandSquadInvite(this), "i", "invite");
		squadCommand.addSubCommand(new CrazySquadsPlayerCommandSquadJoin(this), "j", "join");
		squadCommand.addSubCommand(new CrazySquadsSquadPlayerCommandSquadList(this), "l", "list");
		squadCommand.addSubCommand(new CrazySquadsSquadPlayerCommandSquadKickMember(this), "k", "kick");
		squadCommand.addSubCommand(new CrazySquadsSquadPlayerCommandSquadLeave(this), "q", "quit", "leave");
		squadCommand.addSubCommand(new CrazySquadsSquadPlayerCommandSquadDelete(this), "del", "delete");
		squadCommand.addSubCommand(new CrazySquadsSquadPlayerCommandSquadMode(this), "o", "option", "cfg", "config");
		squadCommand.addSubCommand(new CrazySquadsSquadPlayerCommandSquadCommand(this), "cmd", "command");
		mainCommand.addSubCommand(new CrazySquadsCommandMainCommands(this), "commands");
	}

	private void registerHooks()
	{
		this.playerListener = new CrazySquadsPlayerListener(this);
		final PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(playerListener, this);
		crazyChatsEnabled = Bukkit.getPluginManager().isPluginEnabled("CrazyChats");
		if (crazyChatsEnabled)
			pm.registerEvents(new CrazySquadsCrazyChatsListener(this), this);
		tagAPIEnabled = Bukkit.getPluginManager().isPluginEnabled("TagAPI");
		if (tagAPIEnabled)
			pm.registerEvents(new CrazySquadsTagAPIListener(this), this);
	}

	@Override
	public void onLoad()
	{
		plugin = this;
		PlayerDataProvider.PROVIDERS.add(this);
		super.onLoad();
	}

	@Override
	public void onEnable()
	{
		registerHooks();
		if (crazyChatsEnabled)
			registerModesCrazyChats();
		if (tagAPIEnabled)
			registerModesTagAPI();
		super.onEnable();
		registerCommands();
	}

	@Override
	public void loadConfiguration()
	{
		super.loadConfiguration();
		final ConfigurationSection config = getConfig();
		maxSquadSize = Math.max(1, config.getInt("maxSquadSize", 5));
		maxShareRange = config.getDouble("maxShareRange", 50);
		final String lootType = config.getString("defaultLootRules", ShareRules.SHARE.name()).toUpperCase();
		try
		{
			defaultLootRules = ShareRules.valueOf(lootType);
		}
		catch (final Exception e)
		{
			consoleLog(ChatColor.RED + "NO SUCH LOOTRULE " + lootType);
			defaultLootRules = ShareRules.SHARE;
		}
		final String xpType = config.getString("defaultXPRules", ShareRules.SHARESILENT.name()).toUpperCase();
		try
		{
			defaultXPRules = ShareRules.valueOf(xpType);
		}
		catch (final Exception e)
		{
			consoleLog(ChatColor.RED + "NO SUCH XPRULE " + xpType);
			defaultXPRules = ShareRules.SHARESILENT;
		}
		squadAutoRejoinTime = Math.max(config.getLong("squadAutoRejoinTime", 60), 0);
		commandWhiteList.clear();
		final List<String> commandList = config.getStringList("commandWhitelist");
		if (commandList != null)
			commandWhiteList.addAll(commandList);
		if (crazyChatsEnabled)
		{
			squadChatFormat = CrazyChatsChatHelper.makeFormat(config.getString("squadChatFormat", "&3[Squad] &F%1$s&F: &B%2$s"));
			squadLeaderChatFormat = CrazyChatsChatHelper.makeFormat(config.getString("squadLeaderChatFormat", "&3[Squad] &C*%1$s&C*&F: &B%2$s"));
		}
		if (tagAPIEnabled)
			squadHeadNamePrefix = ChatHelper.colorise(config.getString("squadHeadNamePrefix", ChatColor.DARK_BLUE.toString()));
	}

	@Override
	public void saveConfiguration()
	{
		final ConfigurationSection config = getConfig();
		config.set("maxSquadSize", maxSquadSize);
		config.set("maxShareRange", maxShareRange);
		config.set("defaultLootRules", defaultLootRules.toString());
		config.set("defaultXPRules", defaultXPRules.toString());
		config.set("squadAutoRejoinTime", squadAutoRejoinTime);
		config.set("commandWhitelist", commandWhiteList);
		if (crazyChatsEnabled)
		{
			config.set("squadChatFormat", CrazyChatsChatHelper.unmakeFormat(squadChatFormat));
			config.set("squadLeaderChatFormat", CrazyChatsChatHelper.unmakeFormat(squadLeaderChatFormat));
		}
		if (tagAPIEnabled)
			config.set("squadHeadNamePrefix", ChatHelper.decolorise(squadHeadNamePrefix));
		super.saveConfiguration();
	}

	@Override
	public PlayerDataInterface getAvailablePlayerData(final OfflinePlayer player)
	{
		return getAvailablePlayerData(player.getPlayer());
	}

	@Override
	public PlayerDataInterface getAvailablePlayerData(final String player)
	{
		return getAvailablePlayerData(Bukkit.getPlayerExact(player));
	}

	public PlayerDataInterface getAvailablePlayerData(final Player player)
	{
		if (player == null)
			return null;
		final Squad squad = squads.get(player);
		if (squad == null)
			return null;
		else
			return new PseudoPlayerData(player.getName(), squad);
	}

	public Map<Player, Squad> getSquads()
	{
		return squads;
	}

	public Map<Player, Squad> getInvites()
	{
		return invites;
	}

	public int getMaxSquadSize()
	{
		return maxSquadSize;
	}

	public double getMaxShareRange()
	{
		return maxShareRange;
	}

	public ShareRules getDefaultLootRules()
	{
		return defaultLootRules;
	}

	public ShareRules getDefaultXPRules()
	{
		return defaultXPRules;
	}

	public long getSquadAutoRejoinTime()
	{
		return squadAutoRejoinTime;
	}

	public List<String> getCommandWhiteList()
	{
		return commandWhiteList;
	}

	public String getSquadChatFormat()
	{
		return squadChatFormat;
	}

	void setSquadChatFormat(final String squadChatFormat)
	{
		this.squadChatFormat = squadChatFormat;
	}

	public String getSquadLeaderChatFormat()
	{
		return squadLeaderChatFormat;
	}

	void setSquadHeadNamePrefix(final String squadHeadNamePrefix)
	{
		this.squadHeadNamePrefix = squadHeadNamePrefix;
	}

	public String getSquadHeadNamePrefix()
	{
		return squadHeadNamePrefix;
	}
}
