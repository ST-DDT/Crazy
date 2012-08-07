package de.st_ddt.crazyloginrank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazyloginrank.data.LoginRankData;
import de.st_ddt.crazyloginrank.data.LoginRankPlayerData;
import de.st_ddt.crazyloginrank.data.LoginRankUnregisteredPlayerData;
import de.st_ddt.crazyloginrank.listener.CrazyLoginRankCrazyListener;
import de.st_ddt.crazyloginrank.listener.CrazyLoginRankPlayerListener;
import de.st_ddt.crazyplugin.CrazyPlayerDataPlugin;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class CrazyLoginRank extends CrazyPlayerDataPlugin<LoginRankPlayerData> implements LoginRankPlugin<LoginRankPlayerData>
{

	private static CrazyLoginRank plugin;
	protected final HashMap<String, Integer> permissionRanks = new HashMap<String, Integer>();
	protected int defaultRank;
	protected int defaultOPRank;

	public static CrazyLoginRank getPlugin()
	{
		return plugin;
	}

	private CrazyLoginRankPlayerListener playerListener;
	private CrazyLoginRankCrazyListener crazylistener;

	@Override
	protected String getShortPluginName()
	{
		return "clr";
	}

	@Override
	public void onEnable()
	{
		plugin = this;
		registerHooks();
		super.onEnable();
	}

	public void registerHooks()
	{
		this.playerListener = new CrazyLoginRankPlayerListener(this);
		this.crazylistener = new CrazyLoginRankCrazyListener(this);
		final PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(playerListener, this);
		pm.registerEvents(crazylistener, this);
	}

	@Override
	public void load()
	{
		final ConfigurationSection config = getConfig();
		defaultRank = config.getInt("defaultRank", 0);
		defaultOPRank = config.getInt("defaultOPRank", 100);
		ConfigurationSection ranks = config.getConfigurationSection("ranks");
		if (ranks != null)
			for (String key : ranks.getKeys(false))
				permissionRanks.put(key, config.getInt(key, 0));
		if (isInstalled)
		{
			permissionRanks.put("guest", 0);
			permissionRanks.put("user", 5);
			permissionRanks.put("friends", 30);
			permissionRanks.put("rank1", 40);
			permissionRanks.put("rank2", 45);
			permissionRanks.put("moderator", 50);
			permissionRanks.put("rank3", 75);
			permissionRanks.put("admin", 100);
			permissionRanks.put("owner", 200);
		}
	}

	@Override
	public void save()
	{
		final ConfigurationSection config = getConfig();
		config.set("defaultRank", defaultRank);
		config.set("defaultOPRank", defaultOPRank);
		for (Entry<String, Integer> entry : permissionRanks.entrySet())
			config.set("ranks." + entry.getKey(), entry.getValue());
	}

	@Override
	public boolean command(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyException
	{
		return false;
	}

	@Override
	public Player getLowestRank()
	{
		final List<LoginRankData<?>> list = getRankList(true, false);
		if (list.size() == 0)
			return null;
		return list.get(list.size() - 1).getPlayer();
	}

	@Override
	public List<LoginRankData<?>> getRankList(final boolean includeOnline, final boolean includeData)
	{
		final List<LoginRankData<?>> list = new ArrayList<LoginRankData<?>>();
		if (includeData)
			list.addAll(database.getAllEntries());
		if (includeOnline)
			for (final Player player : Bukkit.getOnlinePlayers())
				list.add(getAvailablePlayerData(player));
		while (list.remove(null))
			;
		Collections.sort(list);
		return list;
	}

	@Override
	public LoginRankData<?> getAvailablePlayerData(final String name)
	{
		LoginRankData<?> res = null;
		if (database != null)
			res = database.getEntry(name);
		if (res != null)
			return res;
		res = getPermissionBasedPlayerData(name);
		if (res != null)
			return res;
		return getDefaultPlayerData(name);
	}

	@Override
	public LoginRankData<?> getAvailablePlayerData(final OfflinePlayer player)
	{
		LoginRankData<?> res = null;
		if (database != null)
			res = database.getEntry(player);
		if (res != null)
			return res;
		res = getPermissionBasedPlayerData(player);
		if (res != null)
			return res;
		return getDefaultPlayerData(player);
	}

	@Override
	public LoginRankData<?> getPermissionBasedPlayerData(final String name)
	{
		return getPermissionBasedPlayerData(Bukkit.getPlayerExact(name));
	}

	@Override
	public LoginRankData<?> getPermissionBasedPlayerData(final OfflinePlayer player)
	{
		if (player == null)
			return null;
		final Player plr = player.getPlayer();
		if (plr == null)
			return null;
		for (final Entry<String, Integer> rank : permissionRanks.entrySet())
			if (plr.hasPermission("crazylogin.rank." + rank.getKey()))
				return new LoginRankUnregisteredPlayerData(player, rank.getValue());
		return null;
	}

	@Override
	public LoginRankData<?> getDefaultPlayerData(final String name)
	{
		return getDefaultPlayerData(Bukkit.getOfflinePlayer(name));
	}

	@Override
	public LoginRankData<?> getDefaultPlayerData(final OfflinePlayer player)
	{
		if (player == null)
			return null;
		if (player.isOp())
			return new LoginRankUnregisteredPlayerData(player, defaultOPRank);
		else
			return new LoginRankUnregisteredPlayerData(player, defaultRank);
	}
}
