package de.st_ddt.crazyutil.databases;

import org.bukkit.OfflinePlayer;

import de.st_ddt.crazyplugin.data.PlayerDataInterface;

public interface PlayerDataDatabase<S extends PlayerDataInterface> extends Database<S>
{

	public S getEntry(OfflinePlayer player);

	public boolean hasEntry(OfflinePlayer player);

	public boolean deleteEntry(OfflinePlayer player);
}
