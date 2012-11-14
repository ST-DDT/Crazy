package de.st_ddt.crazyutil.modules.login;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import de.st_ddt.crazyutil.Named;

public interface LoginSystem extends Named
{

	public boolean hasAccount(OfflinePlayer player);

	public boolean isLoggedIn(Player player);
}
