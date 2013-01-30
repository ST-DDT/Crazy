package de.st_ddt.crazylogin;

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

	public boolean isLoggedIn(Player player);

	public Set<S> getPlayerDatasPerIP(String IP);

	public boolean isAlwaysNeedPassword();

	public boolean isAutoLogoutEnabled();

	public boolean isInstantAutoLogoutEnabled();

	public int getAutoLogoutTime();

	public int getAutoKick();

	public int getAutoKickUnregistered();

	public boolean isBlockingGuestChatEnabled();

	public boolean isBlockingGuestJoinEnabled();

	public boolean isRemovingGuestDataEnabled();

	public List<String> getCommandWhiteList();

	public boolean isForceSaveLoginEnabled();

	public boolean isHidingInventoryEnabled();

	public boolean isHidingPlayerEnabled();

	public boolean isHidingChatEnabled();

	public Encryptor getEncryptor();

	public double getMoveRange();

	public String getUniqueIDKey();

	public void requestLogin(Player player);

	public void broadcastLocaleMessage(boolean console, String permission, boolean loggedInOnly, String localepath, Object... args);

	public void broadcastLocaleRootMessage(boolean console, String permission, boolean loggedInOnly, String localepath, Object... args);

	public void broadcastLocaleMessage(boolean console, String permission, boolean loggedInOnly, CrazyLocale locale, Object... args);

	public void broadcastLocaleMessage(boolean console, String[] permissions, boolean loggedInOnly, String localepath, Object... args);

	public void broadcastLocaleRootMessage(boolean console, String[] permissions, boolean loggedInOnly, String localepath, Object... args);

	public void broadcastLocaleMessage(boolean console, String[] permissions, boolean loggedInOnly, CrazyLocale locale, Object... args);

	public class LoginPluginProvider
	{

		private LoginPlugin<? extends LoginData> plugin;

		public LoginPlugin<? extends LoginData> getPlugin()
		{
			return plugin;
		}

		void setPlugin(final LoginPlugin<? extends LoginData> plugin)
		{
			this.plugin = plugin;
		}
	}
}
