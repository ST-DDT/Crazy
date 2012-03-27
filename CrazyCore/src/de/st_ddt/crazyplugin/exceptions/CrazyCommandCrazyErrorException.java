package de.st_ddt.crazyplugin.exceptions;

import org.bukkit.command.CommandSender;

public class CrazyCommandCrazyErrorException extends CrazyCommandErrorException
{

	private static final long serialVersionUID = 1755697197006991043L;
	private final CrazyException exception;

	public CrazyCommandCrazyErrorException(CrazyException exception)
	{
		super(null);
		this.exception = exception;
	}

	@Override
	public Exception getException()
	{
		return exception;
	}

	@Override
	public void print(CommandSender sender, String header)
	{
		super.print(sender, header);
		exception.print(sender, header);
	}
}
