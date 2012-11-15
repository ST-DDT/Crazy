package de.st_ddt.crazylogin;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;

import de.st_ddt.crazylogin.crypt.Encryptor;
import de.st_ddt.crazylogin.data.LoginData;
import de.st_ddt.crazyplugin.CrazyPlayerDataPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public interface LoginPlugin<S extends LoginData> extends CrazyPlayerDataPluginInterface<LoginData, S>
{

	static final LoginPluginProvider LOGINPLUGINPROVIDER = new LoginPluginProvider();

	public void playerLogin(Player player, final String password) throws CrazyException;

	public void playerLogout(Player player) throws CrazyException;

	public void playerPassword(Player player, String password) throws CrazyException;

	public int dropInactiveAccounts();

	public boolean isLoggedIn(Player player);

	public Set<S> getPlayerDatasPerIP(String IP);

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

	public boolean isBlockingGuestChatEnabled();

	public boolean isBlockingGuestJoinEnabled();

	public boolean isRemovingGuestDataEnabled();

	public boolean isTempBanned(String IP);

	public Date getTempBanned(String IP);

	public String getTempBannedString(String IP);

	public List<String> getCommandWhiteList();

	public boolean isForceSingleSessionEnabled();

	public boolean isForceSingleSessionSameIPBypassEnabled();

	public boolean isForceSaveLoginEnabled();

	public boolean isHidingInventoryEnabled();

	public boolean isHidingPlayerEnabled();

	public boolean isHidingJoinQuitMessagesEnabled();

	public Encryptor getEncryptor();

	public int getAutoDelete();

	public int getMaxOnlinesPerIP();

	public int getMaxRegistrationsPerIP();

	public double getMoveRange();

	public String getNameFilter();

	public boolean checkNameChars(String name);

	public boolean isBlockingDifferentNameCasesEnabled();

	public boolean checkNameCase(String name);

	public int getMinNameLength();

	public int getMaxNameLength();

	public boolean checkNameLength(String name);

	public String getUniqueIDKey();

	public void requestLogin(Player player);

	public void broadcastLocaleMessage(boolean console, String permission, boolean loggedInOnly, String localepath, Object... args);

	public void broadcastLocaleRootMessage(boolean console, String permission, boolean loggedInOnly, String localepath, Object... args);

	public void broadcastLocaleMessage(boolean console, String permission, boolean loggedInOnly, CrazyLocale locale, Object... args);

	public void broadcastLocaleMessage(boolean console, String[] permissions, boolean loggedInOnly, String localepath, Object... args);

	public void broadcastLocaleRootMessage(boolean console, String[] permissions, boolean loggedInOnly, String localepath, Object... args);

	public void broadcastLocaleMessage(boolean console, String[] permissions, boolean loggedInOnly, CrazyLocale locale, Object... args);
}
