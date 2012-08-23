package de.st_ddt.crazyutil;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.data.ParameterData;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public class ChatHelper
{

	private static boolean showChatHeaders;

	private ChatHelper()
	{
	}

	public static boolean isShowingChatHeadersEnabled()
	{
		return showChatHeaders;
	}

	public static void setShowChatHeaders(final boolean showChatHeaders)
	{
		ChatHelper.showChatHeaders = showChatHeaders;
	}

	/**
	 * Replace Colorcodes texts with the real colors
	 * 
	 * @param string
	 *            The string the colors should apply to
	 * @return The colorated String
	 */
	public static String colorise(final String string)
	{
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	public static void sendMessage(final CommandSender target, final Object message, final Object... args)
	{
		sendFinalMessage(target, putArgsExtended(target, message, args));
	}

	public static void sendMessage(final CommandSender target, final String chatHeader, final Object message, final Object... args)
	{
		sendFinalMessage(target, chatHeader, putArgsExtended(target, message, args));
	}

	public static void sendMessage(final CommandSender[] targets, final Object message, final Object... args)
	{
		for (final CommandSender target : targets)
			sendMessage(target, message, args);
	}

	public static void sendMessage(final CommandSender[] targets, final String chatHeader, final Object message, final Object... args)
	{
		for (final CommandSender target : targets)
			sendMessage(target, chatHeader, message, args);
	}

	public static void sendMessage(final Collection<? extends CommandSender> targets, final Object message, final Object... args)
	{
		for (final CommandSender target : targets)
			sendMessage(target, message, args);
	}

	public static void sendMessage(final Collection<? extends CommandSender> targets, final String chatHeader, final Object message, final Object... args)
	{
		for (final CommandSender target : targets)
			sendMessage(target, chatHeader, message, args);
	}

	public static void sendFinalMessage(final CommandSender target, final String message)
	{
		for (final String part : StringUtils.splitByWholeSeparator(message, "\\n"))
			target.sendMessage(part);
	}

	public static void sendFinalMessage(final CommandSender target, final String chatHeader, final String message)
	{
		for (final String part : StringUtils.splitByWholeSeparator(message, "\\n"))
			if (showChatHeaders || !(target instanceof Player))
				target.sendMessage(chatHeader + part);
			else
				target.sendMessage(part);
	}

	public static <E> void sendListMessage(final CommandSender target, final CrazyPluginInterface plugin, final String headLocale, final String seperator, final String entry, final String emptyPage, final int amount, final int page, final List<? extends E> datas, final EntryDataGetter<E> getter)
	{
		sendListMessage(target, plugin.getChatHeader(), plugin.getLocale().getLanguageEntry(headLocale), seperator == null ? null : plugin.getLocale().getLanguageEntry(seperator), entry == null ? null : plugin.getLocale().getLanguageEntry(entry), emptyPage == null ? null : plugin.getLocale().getLanguageEntry(emptyPage), amount, page, datas, getter);
	}

	public static <E> void sendListMessage(final CommandSender target, final String chatHeader, final String headLocale, final String seperator, final String entry, final String emptyPage, final int amount, final int page, final List<? extends E> datas, final EntryDataGetter<E> getter)
	{
		sendListMessage(target, chatHeader, CrazyLocale.getLocaleHead().getLanguageEntry(headLocale), seperator == null ? null : CrazyLocale.getLocaleHead().getLanguageEntry(seperator), entry == null ? null : CrazyLocale.getLocaleHead().getLanguageEntry(entry), emptyPage == null ? null : CrazyLocale.getLocaleHead().getLanguageEntry(emptyPage), amount, page, datas, getter);
	}

	public static <E> void sendListMessage(final CommandSender target, String chatHeader, final CrazyLocale headLocale, CrazyLocale seperator, CrazyLocale entry, CrazyLocale emptyPage, int amount, int page, final List<? extends E> datas, final EntryDataGetter<E> getter)
	{
		if (chatHeader == null)
			chatHeader = "";
		final int lastIndex = datas.size();
		if (page == Integer.MIN_VALUE)
			amount = lastIndex;
		else if (amount == 0)
			amount = 10;
		else if (amount < 0)
			amount = lastIndex;
		page = Math.max(1, page);
		if (seperator == null)
			seperator = CrazyLocale.getLocaleHead().getLanguageEntry("CRAZYPLUGIN.LIST.SEPERATOR");
		if (emptyPage == null)
			emptyPage = CrazyLocale.getLocaleHead().getLanguageEntry("CRAZYPLUGIN.LIST.EMPTYPAGE");
		if (entry == null)
			entry = CrazyLocale.getLocaleHead().getLanguageEntry("CRAZYPLUGIN.LIST.ENTRY");
		CrazyPage.storePagedData(target, chatHeader, headLocale, seperator, entry, emptyPage, amount, page, datas, getter).show(target);
	}

	public static String putArgs(final String message, final Object... args)
	{
		String res = message;
		final int length = args.length;
		for (int i = 0; i < length; i++)
			res = res.replaceAll("\\$" + i + "\\$", args[i].toString());
		return res;
	}

	public static String putArgsPara(final String message, final ParameterData data)
	{
		String res = message;
		final int length = data.getParameterCount();
		for (int i = 0; i < length; i++)
			res = res.replaceAll("\\$" + i + "\\$", data.getParameter(i));
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

	public static <S> S[] cutArray(final S[] array, final int anz)
	{
		if (anz < 0)
			return Arrays.copyOf(array, 0);
		else
			return Arrays.copyOfRange(array, 0, anz);
	}

	public static <S> S[] shiftArray(final S[] array, final int anz)
	{
		if (anz >= array.length)
			return Arrays.copyOf(array, 0);
		else
			return Arrays.copyOfRange(array, anz, array.length);
	}

	public static <S> String listingString(final S[] strings)
	{
		return listingString(", ", strings);
	}

	public static <S> String listingString(final String seperator, final S... strings)
	{
		final int length = strings.length;
		if (length == 0)
			return "";
		String res = strings[0].toString();
		for (int i = 1; i < length; i++)
		{
			res = res + seperator + strings[i].toString();
		}
		return res;
	}

	public static <S> String listingString(final Collection<S> strings)
	{
		return listingString(", ", strings);
	}

	public static <S> String listingString(final String seperator, final Collection<S> strings)
	{
		if (strings.size() == 0)
			return "";
		final Iterator<S> it = strings.iterator();
		final StringBuilder res = new StringBuilder(it.next().toString());
		while (it.hasNext())
		{
			res.append(seperator);
			res.append(it.next().toString());
		}
		return res.toString();
	}

	public static String dateToString(final Date date)
	{
		return CrazyPluginInterface.DateFormat.format(date);
	}

	public static String[] toList(final ParameterData data)
	{
		final int count = data.getParameterCount();
		final String[] res = new String[count];
		for (int i = 0; i < count; i++)
			res[i] = data.getParameter(i);
		return res;
	}

	/**
	 * Converts a time in readable information, splited up in units.
	 * 
	 * @param time
	 *            Time in seconds
	 * @param shift
	 *            Required amount of time for highest shown unit
	 * @param target
	 *            The CommandSender who shall recieve this message.
	 * @param units
	 *            How much time units shall be shown. Example: show Weeks and Days but skip Hours
	 * @param showWeeks
	 *            Use Weeks as a unit or not
	 * @return A String representing the time shown in its units. Example: 2 Days 3 Hours 2 Seconds
	 */
	public static String timeConverter(long time, float shift, final CommandSender target, int units, final boolean showWeeks)
	{
		final StringBuilder res = new StringBuilder();
		if (time > shift * 31536000)
		{
			final long unit = time / 31536000;
			shift = 1;
			units--;
			time %= 31536000;
			res.append(" " + unit + " " + CrazyLocale.getUnitText("TIME.YEARS", target));
			if (units == 0)
				return res.substring(1);
		}
		if (time > shift * 2592000)
		{
			final long unit = time / 2592000;
			shift = 1;
			units--;
			time %= 2592000;
			res.append(" " + unit + " " + CrazyLocale.getUnitText("TIME.MONTHS", target));
			if (units == 0)
				return res.substring(1);
		}
		if (showWeeks && time > shift * 604800)
		{
			final long unit = time / 604800;
			shift = 1;
			units--;
			time %= 604800;
			res.append(" " + unit + " " + CrazyLocale.getUnitText("TIME.WEEKS", target));
			if (units == 0)
				return res.substring(1);
		}
		if (time > shift * 86400)
		{
			final long unit = time / 86400;
			shift = 1;
			units--;
			time %= 86400;
			res.append(" " + unit + " " + CrazyLocale.getUnitText("TIME.DAYS", target));
			if (units == 0)
				return res.substring(1);
		}
		if (time > shift * 3600)
		{
			final long unit = time / 3600;
			shift = 1;
			units--;
			time %= 3600;
			res.append(" " + unit + " " + CrazyLocale.getUnitText("TIME.HOURS", target));
			if (units == 0)
				return res.substring(1);
		}
		if (time > shift * 60)
		{
			final long unit = time / 60;
			shift = 1;
			units--;
			time %= 60;
			res.append(" " + unit + " " + CrazyLocale.getUnitText("TIME.MINUTES", target));
			if (units == 0)
				return res.substring(1);
		}
		if (time > 0 || res.length() == 0)
			res.append(" " + time + " " + CrazyLocale.getUnitText("TIME.SECONDS", target));
		return res.substring(1);
	}
}
