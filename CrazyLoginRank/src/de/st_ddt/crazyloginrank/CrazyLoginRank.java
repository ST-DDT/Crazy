package de.st_ddt.crazyloginrank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	protected final ArrayList<PermissionRanks> permissionRanks = new ArrayList<PermissionRanks>();
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
		final ConfigurationSection ranks = config.getConfigurationSection("ranks");
		if (ranks != null)
			for (final String key : ranks.getKeys(false))
				permissionRanks.add(new PermissionRanks(key, ranks.getInt(key, 0)));
		if (isInstalled)
		{
			permissionRanks.add(new PermissionRanks("guest", 0));
			permissionRanks.add(new PermissionRanks("user", 5));
			permissionRanks.add(new PermissionRanks("friends", 10));
			permissionRanks.add(new PermissionRanks("rank1", 30));
			permissionRanks.add(new PermissionRanks("rank2", 40));
			permissionRanks.add(new PermissionRanks("moderator", 50));
			permissionRanks.add(new PermissionRanks("rank3", 75));
			permissionRanks.add(new PermissionRanks("admin", 100));
			permissionRanks.add(new PermissionRanks("owner", 200));
		}
		Collections.sort(permissionRanks);
	}

	@Override
	public void save()
	{
		final ConfigurationSection config = getConfig();
		config.set("defaultRank", defaultRank);
		config.set("defaultOPRank", defaultOPRank);
		for (final PermissionRanks entry : permissionRanks)
			config.set("ranks." + entry.getName(), entry.getRank());
		super.save();
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
		for (final PermissionRanks rank : permissionRanks)
			if (plr.hasPermission("crazylogin.rank." + rank.getName()))
				return new LoginRankUnregisteredPlayerData(player, rank.getRank());
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
