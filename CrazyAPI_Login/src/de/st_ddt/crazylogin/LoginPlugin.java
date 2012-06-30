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

	public int dropInactiveAccounts();

	public boolean isLoggedIn(Player player);

	public boolean hasAccount(OfflinePlayer player);

	public boolean hasAccount(String name);

	public HashMap<String, ? extends LoginData> getPlayerData();

	public LoginData getPlayerData(OfflinePlayer player);

	public LoginData getPlayerData(String name);

	public void updateAccount(String name);

	public boolean deletePlayerData(String name);

	public List<Player> getOnlinesPerIP(String ip);

	public List<? extends LoginData> getRegistrationsPerIP(String ip);

	public boolean isAlwaysNeedPassword();

	public boolean isAutoLogoutEnabled();

	public boolean isInstantAutoLogoutEnabled();

	public int getAutoLogoutTime();

	public int getAutoKick();

	public long getAutoTempBan();

	public int getAutoKickUnregistered();

	public int getAutoKickLoginFailer();

	public long getAutoTempBanLoginFailer();

	public boolean isAutoKickCommandUsers();

	public boolean isBlockingGuestCommandsEnabled();

	public boolean isResettingGuestLocationsEnabled();

	public boolean isTempBanned(String IP);

	public Date getTempBanned(String IP);

	public String getTempBannedString(String IP);

	public List<String> getCommandWhiteList();

	public boolean isForceSingleSessionEnabled();

	public boolean isForceSingleSessionSameIPBypassEnabled();

	public boolean isForceSaveLoginEnabled();

	public Encryptor getEncryptor();

	public int getAutoDelete();

	public int getMaxOnlinesPerIP();

	public int getMaxRegistrationsPerIP();

	public double getMoveRange();

	public int getMinNameLength();

	public int getMaxNameLength();

	public boolean checkNameLength(String name);

	public String getUniqueIDKey();
}
