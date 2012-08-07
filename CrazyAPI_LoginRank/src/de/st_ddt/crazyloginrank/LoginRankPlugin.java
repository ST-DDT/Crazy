package de.st_ddt.crazyloginrank;

import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import de.st_ddt.crazyloginrank.data.LoginRankData;
import de.st_ddt.crazyplugin.CrazyPlayerDataPluginInterface;

public interface LoginRankPlugin<S extends LoginRankData<S>> extends CrazyPlayerDataPluginInterface<S>
{

	public Player getLowestRank();

	public List<LoginRankData<?>> getRankList(boolean includeOnline, boolean includeData);

	public LoginRankData<?> getAvailablePlayerData(String name);

	public LoginRankData<?> getAvailablePlayerData(OfflinePlayer player);

	public LoginRankData<?> getPermissionBasedPlayerData(String name);

	public LoginRankData<?> getPermissionBasedPlayerData(OfflinePlayer player);

	public LoginRankData<?> getDefaultPlayerData(String name);

	public LoginRankData<?> getDefaultPlayerData(OfflinePlayer player);
}
