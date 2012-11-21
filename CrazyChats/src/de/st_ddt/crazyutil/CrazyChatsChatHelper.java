package de.st_ddt.crazyutil;

import org.apache.commons.lang.StringUtils;

public class CrazyChatsChatHelper extends ChatHelper
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
}
