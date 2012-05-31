package de.st_ddt.crazylogin;

import java.util.HashMap;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import de.st_ddt.crazylogin.crypt.Encryptor;
import de.st_ddt.crazyplugin.CrazyPluginInterface;

public interface LoginPlugin extends CrazyPluginInterface
{

	public abstract int dropInactiveAccounts();

	public abstract boolean isLoggedIn(final Player player);

	public abstract boolean hasAccount(final OfflinePlayer player);

	public abstract boolean hasAccount(final String player);

	public abstract boolean isAlwaysNeedPassword();

	public abstract int getAutoLogoutTime();

	public abstract boolean isInstantAutoLogoutEnabled();

	public abstract int getAutoKick();

	public abstract int getAutoKickUnregistered();

	public abstract List<String> getCommandWhiteList();

	public abstract boolean isAutoKickCommandUsers();

	public abstract boolean isBlockingGuestCommandsEnabled();

	public abstract String getUniqueIDKey();

	public abstract Encryptor getEncryptor();

	public abstract HashMap<String, LoginData> getPlayerData();

	public abstract LoginData getPlayerData(final OfflinePlayer player);

	public abstract LoginData getPlayerData(final String name);

	public abstract boolean deletePlayerData(final String player);

	public abstract boolean isForceSingleSessionEnabled();

	public abstract int getMinNameLength();

	public abstract int getMaxNameLength();

	public abstract boolean checkNameLength(final String name);
}
