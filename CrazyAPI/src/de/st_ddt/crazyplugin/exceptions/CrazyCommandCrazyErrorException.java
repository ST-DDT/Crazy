package de.st_ddt.crazyplugin.exceptions;

import org.bukkit.command.CommandSender;

public class CrazyCommandCrazyErrorException extends CrazyCommandErrorException
{

	private static final long serialVersionUID = 1755697197006991043L;
	private final CrazyException cexception;

	public CrazyCommandCrazyErrorException(final CrazyException cexception)
	{
		super(null);
		this.cexception = cexception;
	}

	@Override
	public Exception getException()
	{
		return cexception;
	}

	@Override
	public void print(final CommandSender sender, final String header)
	{
		super.print(sender, header);
		cexception.print(sender, header);
	}
}
