package de.st_ddt.crazysquads;

import java.util.ArrayList;
import java.util.HashMap;
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
import de.st_ddt.crazyplugin.commands.CrazyPluginCommandMainMode;
import de.st_ddt.crazyplugin.data.PlayerDataInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazysquads.commands.CrazySquadsPlayerCommandSquadCreate;
import de.st_ddt.crazysquads.commands.CrazySquadsPlayerCommandSquadJoin;
import de.st_ddt.crazysquads.commands.CrazySquadsSquadPlayerCommandSquadDelete;
import de.st_ddt.crazysquads.commands.CrazySquadsSquadPlayerCommandSquadInvite;
import de.st_ddt.crazysquads.commands.CrazySquadsSquadPlayerCommandSquadKickMember;
import de.st_ddt.crazysquads.commands.CrazySquadsSquadPlayerCommandSquadLeave;
import de.st_ddt.crazysquads.commands.CrazySquadsSquadPlayerCommandSquadList;
import de.st_ddt.crazysquads.commands.CrazySquadsSquadPlayerCommandSquadMode;
import de.st_ddt.crazysquads.data.PseudoPlayerData;
import de.st_ddt.crazysquads.data.Squad;
import de.st_ddt.crazysquads.listener.CrazySquadsCrazyChatsListener;
import de.st_ddt.crazysquads.listener.CrazySquadsPlayerListener;
import de.st_ddt.crazysquads.listener.CrazySquadsTagAPIListener;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.CrazyChatsChatHelper;
import de.st_ddt.crazyutil.locales.Localized;

public final class CrazySquads extends CrazyPlugin implements PlayerDataProvider
{

	private static CrazySquads plugin;
	private final Map<Player, Squad> squads = new HashMap<Player, Squad>();
	private final Map<Player, Squad> invites = new HashMap<Player, Squad>();
	private final CrazyPluginCommandMainMode modeCommand = new CrazyPluginCommandMainMode(this);
	private CrazySquadsPlayerListener playerListener;
	private String squadChatFormat = "[Squad]%1$s: %2$s";
	private String squadHeadNamePrefix = ChatColor.YELLOW.toString();
	private int maxSquadSize = 1;
	private double maxShareRange;
	private boolean crazyChatsEnabled;
	private boolean tagAPIEnabled;

	public static CrazySquads getPlugin()
	{
		return plugin;
	}

	public CrazySquads()
	{
		super();
		registerModes();
	}

	@Localized({ "CRAZYSQUADS.MODE.CHANGE $Name$ $Value$", "CRAZYSQUADS.FORMAT.CHANGE $FormatName$ $Value$", "CRAZYSQUADS.FORMAT.EXAMPLE $Example$" })
	private void registerModes()
	{
		modeCommand.addMode(modeCommand.new Mode<String>("squadChatFormat", String.class)
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				final String raw = getValue();
				plugin.sendLocaleMessage("FORMAT.CHANGE", sender, name, raw);
				plugin.sendLocaleMessage("FORMAT.EXAMPLE", sender, ChatHelper.putArgs(ChatHelper.colorise(raw), "Sender", "Message", "GroupPrefix", "GroupSuffix", "World"));
			}

			@Override
			public String getValue()
			{
				return CrazyChatsChatHelper.unmakeFormat(squadChatFormat);
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
				squadChatFormat = newValue;
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
		});
		modeCommand.addMode(modeCommand.new Mode<String>("squadHeadNamePrefix", String.class)
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				plugin.sendLocaleMessage("FORMAT.CHANGE", sender, name, getValue(), squadHeadNamePrefix + "Playername");
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
		modeCommand.addMode(modeCommand.new IntegerMode("maxSquadSize")
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
		modeCommand.addMode(modeCommand.new DoubleMode("maxShareRange")
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
	}

	private void registerCommands()
	{
		mainCommand.addSubCommand(modeCommand, "mode");
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
	}

	private void registerHooks()
	{
		this.playerListener = new CrazySquadsPlayerListener(this);
		final PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(playerListener, this);
		crazyChatsEnabled = Bukkit.getPluginManager().getPlugin("CrazyChats") != null;
		if (crazyChatsEnabled)
			pm.registerEvents(new CrazySquadsCrazyChatsListener(this), this);
		tagAPIEnabled = Bukkit.getPluginManager().getPlugin("TagAPI") != null;
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
		super.onEnable();
		registerCommands();
	}

	@Override
	public void loadConfiguration()
	{
		super.loadConfiguration();
		final ConfigurationSection config = getConfig();
		squadChatFormat = CrazyChatsChatHelper.makeFormat(config.getString("squadChatFormat", "&3[Squad] &F%1$s&F: &B%2$s"));
		squadHeadNamePrefix = ChatHelper.colorise(config.getString("squadHeadNamePrefix", ChatColor.DARK_BLUE.toString()));
		maxSquadSize = Math.max(1, config.getInt("maxSquadSize", 5));
		maxShareRange = config.getDouble("maxShareRange", 50);
	}

	@Override
	public void saveConfiguration()
	{
		final ConfigurationSection config = getConfig();
		config.set("squadChatFormat", CrazyChatsChatHelper.unmakeFormat(squadChatFormat));
		config.set("squadHeadNamePrefix", ChatHelper.decolorise(squadHeadNamePrefix));
		config.set("maxSquadSize", maxSquadSize);
		config.set("maxShareRange", maxShareRange);
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

	public String getSquadChatFormat()
	{
		return squadChatFormat;
	}

	public String getSquadHeadNamePrefix()
	{
		return squadHeadNamePrefix;
	}

	public int getMaxSquadSize()
	{
		return maxSquadSize;
	}

	public double getMaxShareRange()
	{
		return maxShareRange;
	}
}
