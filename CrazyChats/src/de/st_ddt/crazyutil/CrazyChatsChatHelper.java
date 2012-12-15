package de.st_ddt.crazyutil;

import org.apache.commons.lang.StringUtils;

public class CrazyChatsChatHelper extends ChatHelperExtended
{

	protected CrazyChatsChatHelper()
	{
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
