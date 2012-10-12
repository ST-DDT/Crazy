package de.st_ddt.crazyplugin.exceptions;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazyCommandCrazyErrorException extends CrazyCommandErrorException
{

	private static final long serialVersionUID = 1755697197006991043L;
	private final CrazyException cexception;

	public CrazyCommandCrazyErrorException(final CrazyException cexception)
	{
		super(cexception);
		this.cexception = cexception;
	}

	@Override
	public CrazyException getException()
	{
		return cexception;
	}

	@Override
	@Localized("CRAZYEXCEPTION.COMMAND.EXCEPTION $Command$ $ErrorMessage$")
	public void print(final CommandSender sender, final String header)
	{
		ChatHelper.sendMessage(sender, header, locale, command, cexception.getMessage());
		cexception.print(sender, header);
		if (printStackTrace)
			cexception.printStackTrace();
	}
}
