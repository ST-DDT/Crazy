package de.st_ddt.crazyplugin.exceptions;

import org.bukkit.command.CommandSender;

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
	public void print(final CommandSender sender, final String header)
	{
		super.print(sender, header);
		if (exception != null)
		{
			sender.sendMessage(header + exception.getMessage());
			if (printStackTrace)
				exception.printStackTrace();
		}
	}
}
