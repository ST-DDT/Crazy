package de.st_ddt.crazylogin;

import java.util.Date;
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

	public abstract HashMap<String, ? extends LoginData> getPlayerData();

	public abstract LoginData getPlayerData(final OfflinePlayer player);

	public abstract LoginData getPlayerData(final String name);

	public abstract boolean deletePlayerData(final String player);

	public abstract List<Player> getOnlinesPerIP(final String ip);

	public abstract List<? extends LoginData> getRegistrationsPerIP(final String ip);

	public abstract boolean isAlwaysNeedPassword();

	public abstract boolean isAutoLogoutEnabled();

	public abstract boolean isInstantAutoLogoutEnabled();

	public abstract int getAutoLogoutTime();

	public abstract int getAutoKick();

	public abstract long getAutoTempBan();

	public abstract int getAutoKickUnregistered();

	public abstract int getAutoKickLoginFailer();

	public abstract long getAutoTempBanLoginFailer();

	public abstract boolean isAutoKickCommandUsers();

	public abstract boolean isBlockingGuestCommandsEnabled();

	public abstract boolean isResettingGuestLocationsEnabled();

	public abstract boolean isTempBanned(final String IP);

	public abstract Date getTempBanned(final String IP);

	public abstract String getTempBannedString(final String IP);

	public abstract List<String> getCommandWhiteList();

	public abstract boolean isForceSingleSessionEnabled();

	public abstract boolean isForceSingleSessionSameIPBypassEnabled();

	public abstract boolean isForceSaveLoginEnabled();

	public abstract Encryptor getEncryptor();

	public abstract int getAutoDelete();

	public abstract int getMaxOnlinesPerIP();

	public abstract int getMaxRegistrationsPerIP();

	public abstract double getMoveRange();

	public abstract int getMinNameLength();

	public abstract int getMaxNameLength();

	public abstract boolean checkNameLength(final String name);

	public abstract String getUniqueIDKey();
}
