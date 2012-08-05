package de.st_ddt.crazyplugin;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.Commandable;
import de.st_ddt.crazyutil.CrazyLogger;
import de.st_ddt.crazyutil.EntryDataGetter;
import de.st_ddt.crazyutil.Named;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public interface CrazyPluginInterface extends Named, CrazyLightPluginInterface, Commandable
{

	public final static SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	public boolean isUpdated();

	public CrazyLogger getCrazyLogger();

	public CrazyLocale getLocale();

	@Override
	public boolean command(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyException;

	public boolean commandMain(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyException;

	public void sendLocaleMessage(final String localepath, final CommandSender target, final Object... args);

	public void sendLocaleRootMessage(final String localepath, final CommandSender target, final Object... args);

	public void sendLocaleMessage(final CrazyLocale locale, final CommandSender target, final Object... args);

	public void sendLocaleMessage(final String localepath, final CommandSender[] targets, final Object... args);

	public void sendLocaleRootMessage(final String localepath, final CommandSender[] targets, final Object... args);

	public void sendLocaleMessage(final CrazyLocale locale, final CommandSender[] targets, final Object... args);

	public void sendLocaleMessage(final String localepath, final Collection<CommandSender> targets, final Object... args);

	public void sendLocaleRootMessage(final String localepath, final Collection<CommandSender> targets, final Object... args);

	public void sendLocaleMessage(final CrazyLocale locale, final Collection<CommandSender> targets, final Object... args);

	public <E> void sendListMessage(final CommandSender target, String headLocale, int amount, int page, List<? extends E> datas, EntryDataGetter<E> getter);

	public <E> void sendListMessage(final CommandSender target, String headLocale, String seperator, String entry, String emptyPage, int amount, int page, List<? extends E> datas, EntryDataGetter<E> getter);

	public <E> void sendListRootMessage(final CommandSender target, String headLocale, String seperator, String entry, String emptyPage, int amount, int page, List<? extends E> datas, EntryDataGetter<E> getter);

	public <E> void sendListMessage(final CommandSender target, CrazyLocale headLocale, CrazyLocale seperator, CrazyLocale entry, CrazyLocale emptyPage, int amount, int page, List<? extends E> datas, EntryDataGetter<E> getter);

	public void broadcastLocaleMessage(final String localepath, final Object... args);

	public void broadcastLocaleRootMessage(final String localepath, final Object... args);

	public void broadcastLocaleMessage(final CrazyLocale locale, final Object... args);

	public void broadcastLocaleMessage(final boolean console, final String permission, final String localepath, final Object... args);

	public void broadcastLocaleRootMessage(final boolean console, final String permission, final String localepath, final Object... args);

	public void broadcastLocaleMessage(final boolean console, final String permission, final CrazyLocale locale, final Object... args);
}
