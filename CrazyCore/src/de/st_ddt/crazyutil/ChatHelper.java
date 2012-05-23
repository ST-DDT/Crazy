package de.st_ddt.crazyutil;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public class ChatHelper
{

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
