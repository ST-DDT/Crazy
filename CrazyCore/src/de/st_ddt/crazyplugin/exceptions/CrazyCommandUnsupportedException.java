package de.st_ddt.crazyplugin.exceptions;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazyCommandUnsupportedException extends CrazyCommandException
{

	private static final long serialVersionUID = -7032605906057458341L;
	private final String type;
	private final String unsupportedCommandPart;

	public CrazyCommandUnsupportedException(final String type, final String... unsupportedCommands)
	{
		super();
		this.type = type;
		this.unsupportedCommandPart = ChatHelper.listingString(" ", unsupportedCommands);
	}

	public String getType()
	{
		return type;
	}

	public String getUnsupportedCommandPart()
	{
		return unsupportedCommandPart;
	}

	@Override
	public String getLangPath()
	{
		return super.getLangPath() + ".UNSUPPORTED";
	}

	@Override
	@Localized("CRAZYEXCEPTION.COMMAND.UNSUPPORTED $Command$ $Type$ $UnsupportedCommandPart$")
	public void print(final CommandSender sender, final String header)
	{
		ChatHelper.sendMessage(sender, header, locale, command, type, unsupportedCommandPart);
	}
}
