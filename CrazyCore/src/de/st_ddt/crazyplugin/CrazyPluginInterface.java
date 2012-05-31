package de.st_ddt.crazyplugin;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyutil.EntryDataGetter;
import de.st_ddt.crazyutil.Named;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public interface CrazyPluginInterface extends Named, CrazyLightPluginInterface
{

	public final static SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	public abstract boolean isUpdated();

	public abstract void sendLocaleMessage(final String localepath, final CommandSender target, final Object... args);

	public abstract void sendLocaleRootMessage(final String localepath, final CommandSender target, final Object... args);

	public abstract void sendLocaleMessage(final CrazyLocale locale, final CommandSender target, final Object... args);

	public abstract void sendLocaleMessage(final String localepath, final CommandSender[] targets, final Object... args);

	public abstract void sendLocaleRootMessage(final String localepath, final CommandSender[] targets, final Object... args);

	public abstract void sendLocaleMessage(final CrazyLocale locale, final CommandSender[] targets, final Object... args);

	public abstract void sendLocaleMessage(final String localepath, final Collection<CommandSender> targets, final Object... args);

	public abstract void sendLocaleRootMessage(final String localepath, final Collection<CommandSender> targets, final Object... args);

	public abstract void sendLocaleMessage(final CrazyLocale locale, final Collection<CommandSender> targets, final Object... args);

	public abstract <E> void sendListMessage(final CommandSender target, String headLocale, int page, List<? extends E> datas, EntryDataGetter<E> getter);

	public abstract <E> void sendListMessage(final CommandSender target, String headLocale, String seperator, String entry, String emptyPage, int page, List<? extends E> datas, EntryDataGetter<E> getter);

	public abstract <E> void sendListRootMessage(final CommandSender target, String headLocale, String seperator, String entry, String emptyPage, int page, List<? extends E> datas, EntryDataGetter<E> getter);

	public abstract <E> void sendListMessage(final CommandSender target, CrazyLocale headLocale, CrazyLocale seperator, CrazyLocale entry, CrazyLocale emptyPage, int page, List<? extends E> datas, EntryDataGetter<E> getter);

	public abstract void broadcastLocaleMessage(final String localepath, final Object... args);

	public abstract void broadcastLocaleRootMessage(final String localepath, final Object... args);

	public abstract void broadcastLocaleMessage(final CrazyLocale locale, final Object... args);

	public abstract void broadcastLocaleMessage(final boolean console, final String permission, final String localepath, final Object... args);

	public abstract void broadcastLocaleRootMessage(final boolean console, final String permission, final String localepath, final Object... args);

	public abstract void broadcastLocaleMessage(final boolean console, final String permission, final CrazyLocale locale, final Object... args);
}
