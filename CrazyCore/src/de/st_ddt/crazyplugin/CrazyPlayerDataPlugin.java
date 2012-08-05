package de.st_ddt.crazyplugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.data.PlayerDataInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyutil.databases.PlayerDataDatabase;

public abstract class CrazyPlayerDataPlugin<S extends PlayerDataInterface<S>> extends CrazyPlugin implements CrazyPlayerDataPluginInterface<S>
{

	private static final HashMap<Class<? extends CrazyPlugin>, CrazyPlugin> playerDataPlugins = new HashMap<Class<? extends CrazyPlugin>, CrazyPlugin>();
	protected PlayerDataDatabase<S> database;

	public static Collection<CrazyPlugin> getCrazyPlayerDataPlugins()
	{
		return playerDataPlugins.values();
	}

	public final static CrazyPlugin getPlayerDataPlugin(final Class<? extends CrazyPlugin> plugin)
	{
		return playerDataPlugins.get(plugin);
	}

	public final static CrazyPlugin getPlayerDataPlugin(final String name)
	{
		for (final CrazyPlugin plugin : playerDataPlugins.values())
			if (plugin.getName().equalsIgnoreCase(name))
				return plugin;
		return null;
	}

	@Override
	public PlayerDataDatabase<S> getCrazyDatabase()
	{
		return database;
	}

	@Override
	public boolean hasPlayerData(final String name)
	{
		return database.hasEntry(name);
	}

	@Override
	public boolean hasPlayerData(final OfflinePlayer player)
	{
		return database.hasEntry(player);
	}

	@Override
	public S getPlayerData(final String name)
	{
		return database.getEntry(name);
	}

	@Override
	public S getPlayerData(final OfflinePlayer player)
	{
		return database.getEntry(player);
	}

	@Override
	public Collection<S> getPlayerData()
	{
		return database.getAllEntries();
	}

	@Override
	public boolean deletePlayerData(final String name)
	{
		return database.deleteEntry(name);
	}

	@Override
	public boolean deletePlayerData(final OfflinePlayer player)
	{
		return database.deleteEntry(player);
	}

	@Override
	public final HashSet<S> getOnlinePlayerDatas()
	{
		HashSet<S> res = new HashSet<S>();
		for (Player player : Bukkit.getOnlinePlayers())
			res.add(getPlayerData(player));
		return res;
	}

	@Override
	public final HashSet<Player> getOnlinePlayersPerIP(String IP)
	{
		HashSet<Player> res = new HashSet<Player>();
		for (final Player player : Bukkit.getOnlinePlayers())
			if (player.getAddress().getAddress().getHostAddress().equals(IP))
				res.add(player);
		return res;
	}

	@Override
	public final HashSet<S> getOnlinePlayerDatasPerIP(String IP)
	{
		HashSet<S> res = new HashSet<S>();
		for (Player player : getOnlinePlayersPerIP(IP))
			res.add(getPlayerData(player));
		return res;
	}

	protected void commandPlayerInfo(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		switch (args.length)
		{
			case 0:
				commandPlayerInfo(sender, (String) null);
				return;
			case 1:
				commandPlayerInfo(sender, args[0]);
				return;
			default:
				throw new CrazyCommandUsageException("/" + getName().toLowerCase() + " playerinfo <Player>");
		}
	}

	protected void commandPlayerInfo(final CommandSender sender, final String name) throws CrazyCommandException
	{
		OfflinePlayer target = null;
		if (name == null)
		{
			if (sender instanceof ConsoleCommandSender)
				throw new CrazyCommandUsageException("/" + getName().toLowerCase() + " playerinfo <Player>");
			target = (Player) sender;
		}
		else if (name.equals(""))
		{
			if (sender instanceof ConsoleCommandSender)
				throw new CrazyCommandUsageException("/" + getName().toLowerCase() + " playerinfo <Player>");
			target = (Player) sender;
		}
		else
		{
			target = Bukkit.getPlayer(name);
			if (target == null)
				target = Bukkit.getOfflinePlayer(name);
			if (target == null)
				throw new CrazyCommandNoSuchException("Player", name);
		}
		if (sender == target)
			if (!sender.hasPermission(getName().toLowerCase() + ".playerinfo.self"))
				throw new CrazyCommandPermissionException();
			else if (!sender.hasPermission(getName().toLowerCase() + ".playerinfo.other"))
				throw new CrazyCommandPermissionException();
		S data = getPlayerData(target);
		if (data == null)
			throw new CrazyCommandNoSuchException("PlayerData", target.getName());
		data.show(sender);
	}
}
