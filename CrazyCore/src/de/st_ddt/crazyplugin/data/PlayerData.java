package de.st_ddt.crazyplugin.data;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.CrazyLightPluginInterface;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.locales.Localized;

public abstract class PlayerData<S extends PlayerData<S>> implements PlayerDataInterface, Comparable<S>
{

	protected final String name;

	public PlayerData(final String name)
	{
		super();
		this.name = name;
	}

	protected abstract String getChatHeader();

	@Override
	public void show(final CommandSender target)
	{
		show(target, getChatHeader(), true);
	}

	@Override
	@Localized({ "CRAZYPLUGIN.PLAYERINFO.HEAD", "CRAZYPLUGIN.PLAYERINFO.USERNAME", "CRAZYPLUGIN.PLAYERINFO.DISPLAYNAME", "CRAZYPLUGIN.PLAYERINFO.IPADDRESS", "CRAZYPLUGIN.PLAYERINFO.CONNECTION", "CRAZYPLUGIN.PLAYERINFO.URL", "CRAZYPLUGIN.PLAYERINFO.OP", "CRAZYPLUGIN.PLAYERINFO.WHITELISTED", "CRAZYPLUGIN.PLAYERINFO.BANNED", "CRAZYPLUGIN.PLAYERINFO.SEPARATOR" })
	public void show(final CommandSender target, final String chatHeader, final boolean showDetailed)
	{
		final CrazyLocale locale = CrazyLocale.getLocaleHead().getSecureLanguageEntry("CRAZYPLUGIN.PLAYERINFO");
		final Player player = getPlayer();
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("HEAD"), CrazyLightPluginInterface.DATETIMEFORMAT.format(new Date()));
		if (player == null)
			ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("USERNAME"), getName());
		else
		{
			ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("USERNAME"), player.getName());
			ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("DISPLAYNAME"), player.getDisplayName());
			ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("IPADDRESS"), player.getAddress().getAddress().getHostAddress());
			ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("CONNECTION"), player.getAddress().getHostName());
			ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("URL"), player.getAddress().getAddress().getHostAddress());
		}
		final OfflinePlayer plr = getOfflinePlayer();
		if (plr != null)
		{
			ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("OP"), plr.isOp() ? "True" : "False");
			if (Bukkit.hasWhitelist())
				ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("WHITELISTED"), plr.isWhitelisted() ? "True" : "False");
			if (plr.isBanned())
				ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("BANNED"), "True");
		}
		if (showDetailed)
		{
			ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("SEPARATOR"));
			showDetailed(target, chatHeader);
		}
	}

	@Override
	public String getShortInfo()
	{
		return toString();
	}

	@Override
	public Player getPlayer()
	{
		return Bukkit.getPlayerExact(getName());
	}

	@Override
	public OfflinePlayer getOfflinePlayer()
	{
		return Bukkit.getOfflinePlayer(getName());
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public boolean isOnline()
	{
		final Player player = getPlayer();
		if (player == null)
			return false;
		return player.isOnline();
	}

	@Override
	public String toString()
	{
		return "PlayerData " + getName();
	}

	@Override
	public int compareTo(final S o)
	{
		return name.compareToIgnoreCase(o.getName());
	}

	@Override
	public int hashCode()
	{
		return name.toLowerCase().hashCode();
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj instanceof PlayerData<?>)
			return equals((PlayerData<?>) obj);
		return false;
	}

	public boolean equals(final PlayerData<?> obj)
	{
		return name.equalsIgnoreCase(obj.name);
	}
}
