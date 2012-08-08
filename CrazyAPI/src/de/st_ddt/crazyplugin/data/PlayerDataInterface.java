package de.st_ddt.crazyplugin.data;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import de.st_ddt.crazyutil.databases.DatabaseEntry;

public interface PlayerDataInterface extends DatabaseEntry, ParameterData
{

	public Player getPlayer();

	public OfflinePlayer getOfflinePlayer();

	@Override
	public String getName();

	public boolean isOnline();
}
