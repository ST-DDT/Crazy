package de.st_ddt.crazyplugin;

import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.commands.CrazyCommandTreeExecutorInterface;
import de.st_ddt.crazyutil.ListFormat;
import de.st_ddt.crazyutil.Logger;
import de.st_ddt.crazyutil.locales.CrazyLocale;

/**
 * This object represents a CrazyPlugin.
 */
public interface CrazyPluginInterface extends CrazyLightPluginInterface
{

	/**
	 * @return True if this plugin has been (re-)installed.
	 */
	public boolean isInstalled();

	/**
	 * @return True if this plugin has been installed or updated.
	 */
	public boolean isUpdated();

	public Logger getCrazyLogger();

	public CrazyLocale getLocale();

	public void load();

	public void loadConfiguration();

	public void save();

	public void saveConfiguration();

	public CrazyCommandTreeExecutorInterface getMainCommand();

	public void sendLocaleMessage(String localepath, CommandSender target, Object... args);

	public void sendLocaleMessage(CrazyLocale locale, CommandSender target, Object... args);

	public void sendLocaleMessage(String localepath, CommandSender[] targets, Object... args);

	public void sendLocaleMessage(CrazyLocale locale, CommandSender[] targets, Object... args);

	public void sendLocaleMessage(String localepath, Collection<? extends CommandSender> targets, Object... args);

	public void sendLocaleMessage(CrazyLocale locale, Collection<? extends CommandSender> targets, Object... args);

	public void sendLocaleList(CommandSender target, String formatPath, int amount, int page, List<?> datas);

	public void sendLocaleList(final CommandSender target, ListFormat format, int amount, int page, List<?> datas);

	public void sendLocaleList(final CommandSender target, String headFormatPath, String listFormatPath, String entryFormatPath, int amount, int page, List<?> datas);

	public void sendLocaleList(final CommandSender target, CrazyLocale headFormat, CrazyLocale listFormat, CrazyLocale entryFormat, int amount, int page, List<?> datas);

	public void broadcastLocaleMessage(String localepath, Object... args);

	public void broadcastLocaleMessage(CrazyLocale locale, Object... args);

	public void broadcastLocaleMessage(boolean console, String permission, String localepath, Object... args);

	public void broadcastLocaleMessage(boolean console, String permission, CrazyLocale locale, Object... args);

	public void broadcastLocaleMessage(boolean console, String[] permissions, String localepath, Object... args);

	public void broadcastLocaleMessage(boolean console, String[] permissions, CrazyLocale locale, Object... args);

	/**
	 * Check for updates.
	 * 
	 * @param force
	 *            Force checking for updates, if false this method does nothing if executed for the second time.
	 * @return true, if an update is available, false otherwise .
	 */
	public boolean checkForUpdate(boolean force);

	/**
	 * @return The latest version number available,<br>
	 *         0 - if {@link #checkForUpdate(boolean force)} hasn't executed yet.<br>
	 *         null - if {@link #checkForUpdate(boolean force)} caused an error.
	 */
	public String getUpdateVersion();
}
