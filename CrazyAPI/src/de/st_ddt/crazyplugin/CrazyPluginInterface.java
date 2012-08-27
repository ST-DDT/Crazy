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

	public static SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	public boolean isInstalled();

	public boolean isUpdated();

	public CrazyLogger getCrazyLogger();

	public CrazyLocale getLocale();

	@Override
	public boolean command(CommandSender sender, String commandLabel, String[] args) throws CrazyException;

	public boolean commandMain(CommandSender sender, String commandLabel, String[] args) throws CrazyException;

	public void sendLocaleMessage(String localepath, CommandSender target, Object... args);

	public void sendLocaleMessage(CrazyLocale locale, CommandSender target, Object... args);

	public void sendLocaleMessage(String localepath, CommandSender[] targets, Object... args);

	public void sendLocaleMessage(CrazyLocale locale, CommandSender[] targets, Object... args);

	public void sendLocaleMessage(String localepath, Collection<? extends CommandSender> targets, Object... args);

	public void sendLocaleMessage(CrazyLocale locale, Collection<? extends CommandSender> targets, Object... args);

	public <E> void sendListMessage(CommandSender target, String headLocale, int amount, int page, List<? extends E> datas, EntryDataGetter<E> getter);

	public <E> void sendListMessage(CommandSender target, String headLocale, String seperator, String entry, String emptyPage, int amount, int page, List<? extends E> datas, EntryDataGetter<E> getter);

	public <E> void sendListMessage(CommandSender target, CrazyLocale headLocale, CrazyLocale seperator, CrazyLocale entry, CrazyLocale emptyPage, int amount, int page, List<? extends E> datas, EntryDataGetter<E> getter);

	public void broadcastLocaleMessage(String localepath, Object... args);

	public void broadcastLocaleMessage(CrazyLocale locale, Object... args);

	public void broadcastLocaleMessage(boolean console, String permission, String localepath, Object... args);

	public void broadcastLocaleMessage(boolean console, String permission, CrazyLocale locale, Object... args);

	public void broadcastLocaleMessage(boolean console, String[] permissions, String localepath, Object... args);

	public void broadcastLocaleMessage(boolean console, String[] permissions, CrazyLocale locale, Object... args);
}
