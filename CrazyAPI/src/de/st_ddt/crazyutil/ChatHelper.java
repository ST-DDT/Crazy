package de.st_ddt.crazyutil;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.data.ParameterData;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public class ChatHelper
{

	private static boolean showChatHeaders;

	protected ChatHelper()
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

	/**
	 * Replace real colors texts with the Colorcodes
	 * 
	 * @param string
	 *            The string the colors should apply to
	 * @return The decolorated String
	 */
	public static String decolorise(final String string)
	{
		final char[] b = string.toCharArray();
		for (int i = 0; i < b.length - 1; i++)
			if (b[i] == ChatColor.COLOR_CHAR && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1)
			{
				b[i] = '&';
				b[i + 1] = Character.toUpperCase(b[i + 1]);
			}
		return new String(b);
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
		if (message.equals(""))
			return;
		for (final String part : StringUtils.splitByWholeSeparator(message, "\\n"))
			target.sendMessage(part);
	}

	public static void sendFinalMessage(final CommandSender target, final String chatHeader, final String message)
	{
		if (message.equals(""))
			return;
		if (!showChatHeaders && target instanceof Player)
			sendFinalMessage(target, message);
		else
			for (final String part : StringUtils.splitByWholeSeparator(message, "\\n"))
				target.sendMessage(chatHeader + part);
	}

	public static String putArgs(final String message, final Object... args)
	{
		return putArgs(message, 0, args);
	}

	public static String putArgs(final String message, final int start, final Object... args)
	{
		String res = message;
		final int length = args.length;
		for (int i = 0; i < length; i++)
			res = StringUtils.replace(res, "$" + (i + start) + "$", args[i].toString());
		return res;
	}

	public static String putArgsPara(final CommandSender sender, final String message, final ParameterData data)
	{
		String res = message;
		final int length = data.getParameterCount();
		for (int i = 0; i < length; i++)
			res = StringUtils.replace(res, "$" + i + "$", data.getParameter(sender, i));
		return res;
	}

	public static String putArgsExtended(final CommandSender target, final Object message, final Object... args)
	{
		return putArgsExtended(target, message, 0, args);
	}

	public static String putArgsExtended(final CommandSender target, final Object message, final int start, final Object... args)
	{
		String res = message.toString();
		if (message instanceof CrazyLocale)
			res = ((CrazyLocale) message).getLanguageText(target);
		final int length = args.length;
		for (int i = 0; i < length; i++)
			res = StringUtils.replace(res, "$" + (i + start) + "$", (args[i] instanceof CrazyLocale ? ((CrazyLocale) args[i]).getLanguageText(target) : args[i].toString()));
		return res;
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
		final StringBuilder res = new StringBuilder(strings[0].toString());
		for (int i = 1; i < length; i++)
		{
			res.append(seperator);
			res.append(strings[i].toString());
		}
		return res.toString();
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

	public static String[] toList(final CommandSender sender, final ParameterData data)
	{
		final int count = data.getParameterCount();
		final String[] res = new String[count];
		for (int i = 0; i < count; i++)
			res[i] = data.getParameter(sender, i);
		return res;
	}

	public static String getMinecraftVersion()
	{
		return Bukkit.getBukkitVersion().split("-", 2)[0];
	}

	public static String getExtendedMinecraftVersion()
	{
		return Bukkit.getBukkitVersion().replace("-R", ".").split("-", 2)[0];
	}
}
