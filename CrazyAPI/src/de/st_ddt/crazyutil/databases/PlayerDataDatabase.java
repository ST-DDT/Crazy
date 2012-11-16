package de.st_ddt.crazyutil.databases;

import org.bukkit.OfflinePlayer;

import de.st_ddt.crazyplugin.data.PlayerDataInterface;

public interface PlayerDataDatabase<S extends PlayerDataInterface> extends Database<S>
{

	public S getEntry(OfflinePlayer player);

	public boolean hasEntry(OfflinePlayer player);

	public boolean deleteEntry(OfflinePlayer player);

	/**
	 * Loads the data belonging to key from data source, if this a none static database. This will overwrite existing data objects already stored in the cache. You have to update all references to data belonging to this key. (Otherwise you risk data inconsistency)
	 * 
	 * @param player
	 *            The player belonging to the data which should be loaded.
	 * @return The current data, eigther loaded or just the current one.
	 */
	public S updateEntry(OfflinePlayer player);
}
