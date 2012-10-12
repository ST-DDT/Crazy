package de.st_ddt.crazyplugin.exceptions;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazyCommandExecutorException extends CrazyCommandCircumstanceException
{

	private static final long serialVersionUID = 4950505774618267378L;
	private final boolean forConsole;

	public CrazyCommandExecutorException(final boolean forConsole)
	{
		super();
		this.forConsole = forConsole;
	}

	@Override
	@Localized({ "CRAZYEXCEPTION.COMMAND.CIRCUMSTANCE.CONSOLE $Command$", "CRAZYEXCEPTION.COMMAND.CIRCUMSTANCE.PLAYER $Command$" })
	public void print(final CommandSender sender, final String header)
	{
		ChatHelper.sendMessage(sender, header, locale.getLanguageEntry(forConsole ? "CONSOLE" : "PLAYER"), command);
	}
}
