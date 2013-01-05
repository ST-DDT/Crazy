package de.st_ddt.crazyutil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazychats.CrazyChats;

public class CrazyChatsChatHelper extends ChatHelperExtended
{

	protected final static DateFormat CHATTIMEFORMAT = new SimpleDateFormat("mm:ss");

	protected CrazyChatsChatHelper()
	{
	}

	public static String applyFormat(final CrazyChats plugin, final CommandSender sender, final String format)
	{
		if (sender instanceof Player)
			return applyFormat(plugin, (Player) sender, format);
		else
			return putArgs(2, format, "", "", "", CHATTIMEFORMAT.format(new Date()));
	}

	public static String applyFormat(final CrazyChats plugin, final Player player, final String format)
	{
		return putArgs(2, format, plugin.getGroupPrefix(player), plugin.getGroupSuffix(player), player.getWorld().getName(), CHATTIMEFORMAT.format(new Date()));
	}

	public static String makeFormat(final String rawFormat)
	{
		if (rawFormat == null)
			return null;
		return ChatHelper.colorise(ChatHelper.putArgs(rawFormat, "%1$s", "%2$s"));
	}

	public static String unmakeFormat(final String format)
	{
		if (format == null)
			return null;
		return ChatHelper.decolorise(StringUtils.replace(StringUtils.replace(format, "%2$s", "$1$"), "%1$s", "$0$"));
	}

	public static String cleanRepetitions(final String message)
	{
		return cleanRepetitions(message, 5);
	}

	public static String cleanRepetitions(final String message, final int limit)
	{
		final StringBuilder newMessage = new StringBuilder(message.length());
		int count = 0;
		char last = 0;
		for (final char cur : message.toCharArray())
			if (Character.toLowerCase(cur) == last)
				if (++count >= limit)
					continue;
				else
					newMessage.append(cur);
			else
			{
				last = Character.toLowerCase(cur);
				count = 0;
				newMessage.append(cur);
			}
		return newMessage.toString();
	}

	public static String cleanCaps(final String message)
	{
		return cleanCaps(message, 5);
	}

	public static String cleanCaps(final String message, final int limit)
	{
		final char[] b = message.toCharArray();
		int count = 0;
		for (int i = 0; i < b.length; i++)
			if (b[i] >= 65 && b[i] <= 90)
				if (++count > limit)
					b[i] = Character.toLowerCase(b[i]);
				else
					continue;
			else if (b[i] >= 97 && b[i] <= 122)
				count = Math.max(count - 1, 0);
		return new String(b);
	}
}
