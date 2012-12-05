package de.st_ddt.crazyplugin;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.commands.CrazyCommandTreeExecutorInterface;
import de.st_ddt.crazyplugin.comparator.PlayerDataComparator;
import de.st_ddt.crazyplugin.data.PlayerDataFilterInterface;
import de.st_ddt.crazyplugin.data.PlayerDataInterface;
import de.st_ddt.crazyutil.ListFormat;
import de.st_ddt.crazyutil.ListOptionsModder;
import de.st_ddt.crazyutil.databases.PlayerDataDatabase;

public interface CrazyPlayerDataPluginInterface<T extends PlayerDataInterface, S extends T> extends CrazyPluginInterface, PlayerDataProvider
{

	public void loadDatabase();

	public void saveDatabase();

	public PlayerDataDatabase<S> getCrazyDatabase();

	public CrazyCommandTreeExecutorInterface<CrazyPlayerDataPluginInterface<T, ? extends T>> getPlayerCommand();

	public boolean hasPlayerData(String name);

	public boolean hasPlayerData(OfflinePlayer player);

	public S getPlayerData(String name);

	public S getPlayerData(OfflinePlayer player);

	public Object getPlayerDataLock();

	public Collection<S> getPlayerData();

	@Override
	public T getAvailablePlayerData(final String name);

	@Override
	public T getAvailablePlayerData(final OfflinePlayer player);

	public Collection<T> getAvailablePlayerData(boolean includeOnline, boolean includeAllEntries);

	public <E extends OfflinePlayer> Set<S> getPlayerData(Collection<E> players);

	public boolean deletePlayerData(String name);

	public boolean deletePlayerData(OfflinePlayer player);

	public Set<S> getOnlinePlayerDatas();

	public Set<Player> getOnlinePlayersPerIP(String IP);

	public Set<S> getOnlinePlayerDatasPerIP(String IP);

	public Collection<? extends PlayerDataFilterInterface<T>> getPlayerDataFilters();

	public Map<String, PlayerDataComparator<T>> getPlayerDataComparators();

	public PlayerDataComparator<T> getPlayerDataDefaultComparator();

	public ListFormat getPlayerDataListFormat();

	public ListOptionsModder<T> getPlayerDataListModder();
}
