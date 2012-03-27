package de.st_ddt.crazyplugin.exceptions;

import org.bukkit.command.CommandSender;

public class CrazyCommandExecutorException extends CrazyCommandCircumstanceException
{

	private static final long serialVersionUID = 4950505774618267378L;
	private boolean forConsole;

	public CrazyCommandExecutorException(boolean forConsole)
	{
		super();
		this.forConsole = forConsole;
	}

	@Override
	public void print(CommandSender sender, String header)
	{
		if (forConsole)
			sender.sendMessage(header + locale.getLocaleMessage(sender, "CONSOLE"));
		else
			sender.sendMessage(header + locale.getLocaleMessage(sender, "PLAYER"));
	}
}
