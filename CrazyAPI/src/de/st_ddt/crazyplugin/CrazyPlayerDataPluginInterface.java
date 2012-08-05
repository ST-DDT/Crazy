package de.st_ddt.crazyplugin;

import java.util.Collection;
import java.util.Set;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.data.PlayerDataInterface;
import de.st_ddt.crazyutil.databases.PlayerDataDatabase;

public interface CrazyPlayerDataPluginInterface<S extends PlayerDataInterface<S>> extends CrazyPluginInterface
{

	public PlayerDataDatabase<S> getCrazyDatabase();

	public boolean hasPlayerData(String name);

	public boolean hasPlayerData(OfflinePlayer player);

	public S getPlayerData(String name);

	public S getPlayerData(OfflinePlayer player);

	public Collection<S> getPlayerData();

	public boolean deletePlayerData(String name);

	public boolean deletePlayerData(OfflinePlayer player);

	public Set<S> getOnlinePlayerDatas();

	public Set<Player> getOnlinePlayersPerIP(String IP);

	public Set<S> getOnlinePlayerDatasPerIP(String IP);
}
