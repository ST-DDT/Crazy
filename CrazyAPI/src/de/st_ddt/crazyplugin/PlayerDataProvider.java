package de.st_ddt.crazyplugin;

import java.util.Set;
import java.util.TreeSet;

import org.bukkit.OfflinePlayer;

import de.st_ddt.crazyplugin.data.PlayerDataInterface;

public interface PlayerDataProvider
{

	public static final Set<PlayerDataProvider> PROVIDERS = new TreeSet<PlayerDataProvider>();

	public PlayerDataInterface getAvailablePlayerData(OfflinePlayer player);

	public PlayerDataInterface getAvailablePlayerData(String player);

	public String getChatHeader();
}
