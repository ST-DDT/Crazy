package de.st_ddt.crazyplugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.commands.CrazyPlayerDataPluginCommandMainReload;
import de.st_ddt.crazyplugin.commands.CrazyPlayerDataPluginCommandPlayerTree;
import de.st_ddt.crazyplugin.comparator.PlayerDataComparator;
import de.st_ddt.crazyplugin.comparator.PlayerDataNameComparator;
import de.st_ddt.crazyplugin.data.PlayerDataFilter;
import de.st_ddt.crazyplugin.data.PlayerDataInterface;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.ListFormat;
import de.st_ddt.crazyutil.ListOptionsModder;
import de.st_ddt.crazyutil.databases.PlayerDataDatabase;
import de.st_ddt.crazyutil.locales.Localized;

public abstract class CrazyPlayerDataPlugin<T extends PlayerDataInterface, S extends T> extends CrazyPlugin implements CrazyPlayerDataPluginInterface<T, S>
{

	private static final LinkedHashMap<Class<? extends CrazyPlugin>, CrazyPlayerDataPlugin<? extends PlayerDataInterface, ? extends PlayerDataInterface>> playerDataPlugins = new LinkedHashMap<Class<? extends CrazyPlugin>, CrazyPlayerDataPlugin<? extends PlayerDataInterface, ? extends PlayerDataInterface>>();
	protected final Collection<PlayerDataFilter<T>> playerDataFilters = new ArrayList<PlayerDataFilter<T>>();
	protected final Map<String, PlayerDataComparator<T>> playerDataSorters = new HashMap<String, PlayerDataComparator<T>>();
	protected final CrazyPlayerDataPluginCommandPlayerTree<T> playerCommand = new CrazyPlayerDataPluginCommandPlayerTree<T>(this);
	protected PlayerDataDatabase<S> database;
	protected boolean saveDatabaseOnShutdown;

	public static Collection<CrazyPlayerDataPlugin<? extends PlayerDataInterface, ? extends PlayerDataInterface>> getCrazyPlayerDataPlugins()
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

	public CrazyPlayerDataPlugin()
	{
		super();
		registerSorters();
	}

	private void registerSorters()
	{
		playerDataSorters.put("name", new PlayerDataNameComparator<T>());
		playerDataSorters.put("default", getPlayerDataDefaultComparator());
	}

	@Override
	public PlayerDataDatabase<S> getCrazyDatabase()
	{
		return database;
	}

	@Override
	public CrazyPlayerDataPluginCommandPlayerTree<T> getPlayerCommand()
	{
		return playerCommand;
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
	public Object getPlayerDataLock()
	{
		if (database == null)
			return new Object();
		else
			return database.getDatabaseLock();
	}

	@Override
	public Collection<S> getPlayerData()
	{
		if (database == null)
			return new HashSet<S>();
		else
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
	public HashSet<T> getAvailablePlayerData(final boolean includeOnline, final boolean includeAllEntries)
	{
		final HashSet<T> result = new HashSet<T>();
		if (includeAllEntries)
			if (database != null)
				synchronized (database.getDatabaseLock())
				{
					result.addAll(database.getAllEntries());
				}
		if (includeOnline)
			for (final Player player : Bukkit.getOnlinePlayers())
				result.add(getAvailablePlayerData(player));
		result.remove(null);
		return result;
	}

	@Override
	public <E extends OfflinePlayer> HashSet<S> getPlayerData(final Collection<E> players)
	{
		final HashSet<S> datas = new HashSet<S>();
		for (final OfflinePlayer player : players)
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
		final HashSet<S> res = new HashSet<S>();
		for (final Player player : Bukkit.getOnlinePlayers())
			res.add(getPlayerData(player));
		res.remove(null);
		return res;
	}

	@Override
	public final HashSet<Player> getOnlinePlayersPerIP(final String IP)
	{
		final HashSet<Player> res = new HashSet<Player>();
		for (final Player player : Bukkit.getOnlinePlayers())
			if (player.getAddress().getAddress().getHostAddress().equals(IP))
				res.add(player);
		return res;
	}

	@Override
	public final HashSet<S> getOnlinePlayerDatasPerIP(final String IP)
	{
		final HashSet<S> res = new HashSet<S>();
		for (final Player player : getOnlinePlayersPerIP(IP))
			res.add(getPlayerData(player));
		res.remove(null);
		return res;
	}

	@Override
	@Localized("CRAZYPLUGIN.PLUGININFO.DATABASEENTRIES")
	public void show(final CommandSender target, final String chatHeader, final boolean showDetailed)
	{
		super.show(target, chatHeader, showDetailed);
		if (database != null)
			ChatHelper.sendMessage(target, chatHeader, getLocale().getLanguageEntry("PLUGININFO.DATABASEENTRIES"), database.getAllEntries().size());
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
		super.onEnable();
		mainCommand.addSubCommand(playerCommand, "player", "players");
		mainCommand.addSubCommand(new CrazyPlayerDataPluginCommandMainReload<T>(this), "reload");
	}

	@Override
	public void onDisable()
	{
		if (saveDatabaseOnShutdown)
			saveDatabase();
		saveConfiguration();
	}

	@Override
	public void load()
	{
		super.load();
		loadDatabase();
	}

	@Override
	@Localized({ "CRAZYPLUGIN.DATABASE.ACCESSWARN", "CRAZYPLUGIN.DATABASE.LOADED" })
	public void loadDatabase()
	{
	}

	@Override
	public void loadConfiguration()
	{
		saveDatabaseOnShutdown = getConfig().getBoolean("database.saveOnShutdown", true);
	}

	@Override
	public void save()
	{
		saveDatabase();
		super.save();
	}

	@Override
	public void saveDatabase()
	{
		final ConfigurationSection config = getConfig();
		if (database != null)
		{
			database.save(config, "database.");
			database.saveDatabase();
		}
	}

	@Override
	public void saveConfiguration()
	{
		final ConfigurationSection config = getConfig();
		if (database != null)
			database.save(config, "database.");
		config.set("database.saveOnShutdown", saveDatabaseOnShutdown);
		super.saveConfiguration();
	}

	@Override
	public final Collection<? extends PlayerDataFilter<T>> getPlayerDataFilters()
	{
		return playerDataFilters;
	}

	@Override
	public final Map<String, PlayerDataComparator<T>> getPlayerDataComparators()
	{
		return playerDataSorters;
	}

	@Override
	public PlayerDataComparator<T> getPlayerDataDefaultComparator()
	{
		return new PlayerDataNameComparator<T>();
	}

	@Override
	public ListFormat getPlayerDataListFormat()
	{
		return new ListFormat()
		{

			@Override
			@Localized({ "CRAZYPLUGIN.COMMAND.PLAYER.LIST.HEADER", "CRAZYPLUGIN.COMMAND.PLAYER.LIST.LISTFORMAT", "CRAZYPLUGIN.COMMAND.PLAYER.LIST.ENTRYFORMAT" })
			public String headFormat(final CommandSender sender)
			{
				return getLocale().getLanguageEntry("COMMAND.PLAYER.LIST.HEADER").getLanguageText(sender);
			}

			@Override
			public String listFormat(final CommandSender sender)
			{
				return getLocale().getLanguageEntry("COMMAND.PLAYER.LIST.LISTFORMAT").getLanguageText(sender);
			}

			@Override
			public String entryFormat(final CommandSender sender)
			{
				return getLocale().getLanguageEntry("COMMAND.PLAYER.LIST.ENTRYFORMAT").getLanguageText(sender);
			}
		};
	}

	@Override
	public ListOptionsModder<T> getPlayerDataListModder()
	{
		return null;
	}
}
