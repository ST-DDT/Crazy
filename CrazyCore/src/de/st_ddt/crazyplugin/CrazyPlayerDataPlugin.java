package de.st_ddt.crazyplugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.data.PlayerDataInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.databases.PlayerDataDatabase;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public abstract class CrazyPlayerDataPlugin<T extends PlayerDataInterface, S extends T> extends CrazyPlugin implements CrazyPlayerDataPluginInterface<T, S>
{

	private static final HashMap<Class<? extends CrazyPlugin>, CrazyPlugin> playerDataPlugins = new HashMap<Class<? extends CrazyPlugin>, CrazyPlugin>();
	protected PlayerDataDatabase<S> database;
	protected boolean saveDatabaseOnShutdown;

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
		if (database == null)
			return false;
		return database.hasEntry(name);
	}

	@Override
	public boolean hasPlayerData(final OfflinePlayer player)
	{
		if (database == null)
			return false;
		return database.hasEntry(player);
	}

	@Override
	public S getPlayerData(final String name)
	{
		if (database == null)
			return null;
		return database.getEntry(name);
	}

	@Override
	public S getPlayerData(final OfflinePlayer player)
	{
		if (database == null)
			return null;
		return database.getEntry(player);
	}

	@Override
	public Collection<S> getPlayerData()
	{
		if (database == null)
			return new HashSet<S>();
		return database.getAllEntries();
	}

	@Override
	public T getAvailablePlayerData(final String name)
	{
		return getPlayerData(name);
	}

	@Override
	public T getAvailablePlayerData(final OfflinePlayer player)
	{
		return getPlayerData(player);
	}

	@Override
	public HashSet<T> getAvailablePlayerData(boolean includeOnline, boolean includeData)
	{
		HashSet<T> result = new HashSet<T>();
		if (includeData)
			if (database != null)
				result.addAll(database.getAllEntries());
		if (includeOnline)
			for (Player player : Bukkit.getOnlinePlayers())
				result.add(getAvailablePlayerData(player));
		result.remove(null);
		return result;
	}

	@Override
	public <E extends OfflinePlayer> HashSet<S> getPlayerData(Collection<E> players)
	{
		HashSet<S> datas = new HashSet<S>();
		for (OfflinePlayer player : players)
			datas.add(getPlayerData(player));
		return datas;
	}

	@Override
	public boolean deletePlayerData(final String name)
	{
		if (database == null)
			return false;
		return database.deleteEntry(name);
	}

	@Override
	public boolean deletePlayerData(final OfflinePlayer player)
	{
		if (database == null)
			return false;
		return database.deleteEntry(player);
	}

	@Override
	public final HashSet<S> getOnlinePlayerDatas()
	{
		HashSet<S> res = new HashSet<S>();
		for (Player player : Bukkit.getOnlinePlayers())
			res.add(getPlayerData(player));
		res.remove(null);
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
		res.remove(null);
		return res;
	}

	@Override
	public boolean commandMain(CommandSender sender, String commandLabel, String[] args) throws CrazyException
	{
		if (commandLabel.equalsIgnoreCase("player"))
		{
			if (args.length == 0)
			{
				commandPlayerInfo(sender, args);
				return true;
			}
			try
			{
				final String[] newArgs = ChatHelper.shiftArray(args, 1);
				if (!commandPlayer(sender, args[0].toLowerCase(), newArgs))
					commandPlayerInfo(sender, newArgs);
				return true;
			}
			catch (final CrazyCommandException e)
			{
				e.shiftCommandIndex();
				throw e;
			}
		}
		return false;
	}

	public boolean commandPlayer(CommandSender sender, String commandLabel, String[] args) throws CrazyException
	{
		if (commandLabel.equals("info"))
		{
			commandPlayerInfo(sender, args);
			return true;
		}
		else if (commandLabel.equals("delete") || commandLabel.equals("remove"))
		{
			commandPlayerDelete(sender, args);
			return true;
		}
		return false;
	}

	protected void commandPlayerInfo(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		switch (args.length)
		{
			case 0:
				commandPlayerInfo(sender, (String) null, true);
				return;
			case 1:
				commandPlayerInfo(sender, args[0], true);
				return;
			default:
				throw new CrazyCommandUsageException("/" + getName().toLowerCase() + " playerinfo [Player]");
		}
	}

	protected void commandPlayerInfo(final CommandSender sender, final String name, final boolean detailed) throws CrazyCommandException
	{
		OfflinePlayer target = null;
		if (name == null)
		{
			if (sender instanceof ConsoleCommandSender)
				throw new CrazyCommandUsageException("/" + getName().toLowerCase() + " player info <Player>");
			target = (Player) sender;
		}
		else if (name.equals(""))
		{
			if (sender instanceof ConsoleCommandSender)
				throw new CrazyCommandUsageException("/" + getName().toLowerCase() + " player info <Player>");
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
			if (!sender.hasPermission(getName().toLowerCase() + ".player.info.self"))
				throw new CrazyCommandPermissionException();
			else if (!sender.hasPermission(getName().toLowerCase() + ".player.info.other"))
				throw new CrazyCommandPermissionException();
		S data = getPlayerData(target);
		if (data == null)
			throw new CrazyCommandNoSuchException("PlayerData", target.getName());
		data.show(sender, getChatHeader(), detailed);
	}

	protected void commandPlayerDelete(CommandSender sender, String[] args) throws CrazyCommandException
	{
		if (args.length != 1)
			throw new CrazyCommandUsageException("/" + getName().toLowerCase() + " player delete <Player>");
		if (!sender.hasPermission(getName().toLowerCase() + ".player.delete"))
			throw new CrazyCommandPermissionException();
		if (!deletePlayerData(args[0]))
			throw new CrazyCommandNoSuchException("PlayerData", args[0]);
		sendLocaleMessage("COMMAND.PLAYERDATA.DELETED", sender, args[0]);
	}

	@Override
	public void show(CommandSender target, String chatHeader, boolean showDetailed)
	{
		final CrazyLocale locale = CrazyLocale.getLocaleHead().getSecureLanguageEntry("CRAZYPLUGIN.PLUGININFO");
		super.show(target, chatHeader, showDetailed);
		if (database != null)
			ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("DATABASEENTRIES"), database.getAllEntries().size());
	}

	@Override
	public void onLoad()
	{
		playerDataPlugins.put(this.getClass(), this);
		super.onLoad();
	}

	@Override
	public void onEnable()
	{
		saveDatabaseOnShutdown = getConfig().getBoolean("database.saveOnShutdown", true);
		super.onEnable();
	}

	@Override
	public void onDisable()
	{
		if (saveDatabaseOnShutdown)
			saveDatabase();
		saveConfiguration();
	}

	@Override
	public void save()
	{
		saveDatabase();
		saveConfiguration();
	}

	public void saveDatabase()
	{
		final ConfigurationSection config = getConfig();
		if (database != null)
		{
			config.set("database.saveType", database.getType().toString());
			database.saveDatabase();
		}
	}

	public void saveConfiguration()
	{
		final ConfigurationSection config = getConfig();
		config.set("database.saveOnShutdown", saveDatabaseOnShutdown);
		saveConfig();
	}
}
