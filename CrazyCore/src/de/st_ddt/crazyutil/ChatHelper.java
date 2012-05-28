package de.st_ddt.crazyutil;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public class ChatHelper
{

	private ChatHelper()
	{
	}

	public static String colorise(final String string)
	{
		return string.replaceAll("\\&0", ChatColor.BLACK.toString()).replaceAll("\\&1", ChatColor.DARK_BLUE.toString()).replaceAll("\\&2", ChatColor.DARK_GREEN.toString()).replaceAll("\\&3", ChatColor.DARK_AQUA.toString()).replaceAll("\\&4", ChatColor.DARK_RED.toString()).replaceAll("\\&5", ChatColor.DARK_PURPLE.toString()).replaceAll("\\&6", ChatColor.GOLD.toString()).replaceAll("\\&7", ChatColor.GRAY.toString()).replaceAll("\\&8", ChatColor.DARK_GRAY.toString()).replaceAll("\\&9", ChatColor.BLUE.toString()).replaceAll("\\&A", ChatColor.GREEN.toString()).replaceAll("\\&B", ChatColor.AQUA.toString()).replaceAll("\\&C", ChatColor.RED.toString()).replaceAll("\\&D", ChatColor.LIGHT_PURPLE.toString()).replaceAll("\\&E", ChatColor.YELLOW.toString()).replaceAll("\\&F", ChatColor.WHITE.toString()).replaceAll("\\&G", ChatColor.MAGIC.toString());
	}

	public static void sendMessage(final CommandSender target, final Object message, final Object... args)
	{
		target.sendMessage(putArgsExtended(target, message, args));
	}

	public static void sendMessage(final CommandSender target, final String chatHeader, final Object message, final Object... args)
	{
		target.sendMessage(chatHeader + putArgsExtended(target, message, args));
	}

	public static void sendMessage(final CommandSender[] targets, final Object message, final Object... args)
	{
		for (final CommandSender target : targets)
			target.sendMessage(putArgsExtended(target, message, args));
	}

	public static void sendMessage(final CommandSender[] targets, final String chatHeader, final Object message, final Object... args)
	{
		for (final CommandSender target : targets)
			target.sendMessage(chatHeader + putArgsExtended(target, message, args));
	}

	public static void sendMessage(final Collection<CommandSender> targets, final Object message, final Object... args)
	{
		for (final CommandSender target : targets)
			target.sendMessage(putArgsExtended(target, message, args));
	}

	public static void sendMessage(final Collection<CommandSender> targets, final String chatHeader, final Object message, final Object... args)
	{
		for (final CommandSender target : targets)
			target.sendMessage(chatHeader + putArgsExtended(target, message, args));
	}

	public static <E> void sendListMessage(final CommandSender target, CrazyPlugin plugin, String headLocale, String seperator, String entry, String emptyPage, int page, List<? extends E> datas, EntryDataGetter<E> getter)
	{
		sendListMessage(target, plugin.getChatHeader(), plugin.getLocale().getLanguageEntry(headLocale), seperator == null ? null : plugin.getLocale().getLanguageEntry(seperator), entry == null ? null : plugin.getLocale().getLanguageEntry(entry), emptyPage == null ? null : plugin.getLocale().getLanguageEntry(emptyPage), page, datas, getter);
	}

	public static <E> void sendListMessage(final CommandSender target, String chatHeader, String headLocale, String seperator, String entry, String emptyPage, int page, List<? extends E> datas, EntryDataGetter<E> getter)
	{
		sendListMessage(target, chatHeader, CrazyLocale.getLocaleHead().getLanguageEntry(headLocale), seperator == null ? null : CrazyLocale.getLocaleHead().getLanguageEntry(seperator), entry == null ? null : CrazyLocale.getLocaleHead().getLanguageEntry(entry), emptyPage == null ? null : CrazyLocale.getLocaleHead().getLanguageEntry(emptyPage), page, datas, getter);
	}

	public static <E> void sendListMessage(final CommandSender target, String chatHeader, CrazyLocale headLocale, CrazyLocale seperator, CrazyLocale entry, CrazyLocale emptyPage, int page, List<? extends E> datas, EntryDataGetter<E> getter)
	{
		if (chatHeader == null)
			chatHeader = "";
		sendMessage(target, headLocale, page, (datas.size() + 9) / 10);
		if (seperator == null)
			seperator = CrazyLocale.getLocaleHead().getLanguageEntry("CRAZYPLUGIN.LIST.SEPERATOR");
		sendMessage(target, seperator);
		int lastIndex = datas.size();
		if (lastIndex + 9 < page * 10)
		{
			if (emptyPage == null)
				emptyPage = CrazyLocale.getLocaleHead().getLanguageEntry("CRAZYPLUGIN.LIST.EMPTYPAGE");
			sendMessage(target, emptyPage, page);
			return;
		}
		if (entry == null)
			entry = CrazyLocale.getLocaleHead().getLanguageEntry("CRAZYPLUGIN.LIST.ENTRY");
		lastIndex = Math.min(lastIndex, page * 10);
		for (int i = page * 10 - 10; i < lastIndex; i++)
			sendMessage(target, entry, i + 1, getter.getEntryData(datas.get(i)));
	}

	public static String putArgs(final String message, final Object... args)
	{
		String res = message;
		final int length = args.length;
		for (int i = 0; i < length; i++)
			res = res.replaceAll("\\$" + i + "\\$", args[i].toString());
		return res;
	}

	public static String putArgsExtended(final CommandSender target, final Object message, final Object... args)
	{
		String res = message.toString();
		if (message instanceof CrazyLocale)
			res = ((CrazyLocale) message).getLanguageText(target);
		final int length = args.length;
		for (int i = 0; i < length; i++)
			res = res.replaceAll("\\$" + i + "\\$", (args[i] instanceof CrazyLocale ? ((CrazyLocale) args[i]).getLanguageText(target) : args[i].toString()));
		return res;
	}

	public static String[] shiftArray(final String[] array, final int anz)
	{
		if (anz >= array.length)
			return new String[0];
		final String[] res = new String[array.length - anz];
		for (int i = 0; i < array.length - anz; i++)
			res[i] = array[i + anz];
		return res;
	}

	public static String listingString(final String[] strings)
	{
		return listingString(", ", strings);
	}

	public static String listingString(final String seperator, final String... strings)
	{
		final int length = strings.length;
		if (length == 0)
			return "";
		String res = strings[0];
		for (int i = 1; i < length; i++)
			res = res + seperator + strings[i];
		return res;
	}

	public static String listingString(final Collection<String> strings)
	{
		return listingString(", ", strings);
	}

	public static String listingString(final String seperator, final Collection<String> strings)
	{
		if (strings.size() == 0)
			return "";
		final Iterator<String> list = strings.iterator();
		String res = list.next();
		while (list.hasNext())
			res = res + seperator + list.next();
		return res;
	}

	public static String dateToString(final Date date)
	{
		return CrazyPlugin.DateFormat.format(date);
	}
}
