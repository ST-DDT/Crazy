package de.st_ddt.crazyutil;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.bukkit.ChatColor;

import de.st_ddt.crazyplugin.CrazyPlugin;

public class ChatHelper
{

	public static String colorise(String string)
	{
		return string.replaceAll("\\&0", ChatColor.BLACK.toString()).replaceAll("\\&1", ChatColor.DARK_BLUE.toString()).replaceAll("\\&2", ChatColor.DARK_GREEN.toString()).replaceAll("\\&3", ChatColor.DARK_AQUA.toString()).replaceAll("\\&4", ChatColor.DARK_RED.toString()).replaceAll("\\&5", ChatColor.DARK_PURPLE.toString()).replaceAll("\\&6", ChatColor.GOLD.toString()).replaceAll("\\&7", ChatColor.GRAY.toString()).replaceAll("\\&8", ChatColor.DARK_GRAY.toString()).replaceAll("\\&9", ChatColor.BLUE.toString()).replaceAll("\\&A", ChatColor.GREEN.toString()).replaceAll("\\&B", ChatColor.AQUA.toString()).replaceAll("\\&C", ChatColor.RED.toString()).replaceAll("\\&D", ChatColor.LIGHT_PURPLE.toString()).replaceAll("\\&E", ChatColor.YELLOW.toString()).replaceAll("\\&F", ChatColor.WHITE.toString()).replaceAll("\\&G", ChatColor.MAGIC.toString());
	}

	public static String putArgs(String text, Object... args)
	{
		String res = text;
		int length = args.length;
		for (int i = 0; i < length; i++)
			res = res.replaceAll("\\$" + i + "\\$", args[i].toString());
		return res;
	}

	public static String[] shiftArray(String[] array, int anz)
	{
		if (anz >= array.length)
			return new String[0];
		String[] res = new String[array.length - anz];
		for (int i = 0; i < array.length - anz; i++)
			res[i] = array[i + anz];
		return res;
	}

	public static String listToString(String[] strings)
	{
		return listToString(strings, ", ");
	}

	public static String listToString(String[] strings, String seperator)
	{
		int length = strings.length;
		if (length == 0)
			return "";
		String res = strings[0];
		for (int i = 1; i < length; i++)
			res = res + seperator + strings[i];
		return res;
	}

	public static String listToString(ArrayList<String> strings)
	{
		return listToString(strings, ", ");
	}

	public static String listToString(ArrayList<String> strings, String seperator)
	{
		if (strings.size() == 0)
			return "";
		Iterator<String> list = strings.iterator();
		String res = list.next();
		while (list.hasNext())
			res = res + seperator + list.next();
		return res;
	}

	public static String dateToString(Date date)
	{
		return CrazyPlugin.DateFormat.format(date);
	}
}
