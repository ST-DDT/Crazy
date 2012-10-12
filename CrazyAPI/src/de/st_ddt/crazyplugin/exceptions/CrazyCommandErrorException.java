package de.st_ddt.crazyplugin.exceptions;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazyCommandErrorException extends CrazyCommandException
{

	private static final long serialVersionUID = 1755697197006991043L;
	private final Exception exception;

	public CrazyCommandErrorException(final Exception exception)
	{
		super();
		this.exception = exception;
	}

	public Exception getException()
	{
		return exception;
	}

	@Override
	public String getLangPath()
	{
		return super.getLangPath() + ".EXCEPTION";
	}

	@Override
	@Localized("CRAZYEXCEPTION.COMMAND.EXCEPTION $Command$ $ErrorMessage$")
	public void print(final CommandSender sender, final String header)
	{
		ChatHelper.sendMessage(sender, header, locale, command, exception.getMessage());
		if (printStackTrace)
			exception.printStackTrace();
	}
}
