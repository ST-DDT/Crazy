package de.st_ddt.crazyplugin.exceptions;

import org.bukkit.command.CommandSender;

public class CrazyCommandExceedingLimitsException extends CrazyCommandException
{

	private static final long serialVersionUID = 5977462914820064182L;
	protected final String message;
	protected final boolean lowerLimit;
	protected final Integer limit;

	public CrazyCommandExceedingLimitsException(String message)
	{
		super();
		this.message = message;
		lowerLimit = false;
		limit = null;
	}

	public CrazyCommandExceedingLimitsException(String message, Integer limit)
	{
		super();
		this.message = message;
		lowerLimit = false;
		this.limit = limit;
	}

	public CrazyCommandExceedingLimitsException(String message, boolean lowerLimit)
	{
		super();
		this.message = message;
		this.lowerLimit = lowerLimit;
		limit = null;
	}

	public CrazyCommandExceedingLimitsException(String message, boolean lowerLimit, Integer limit)
	{
		super();
		this.message = message;
		this.lowerLimit = lowerLimit;
		this.limit = limit;
	}

	@Override
	public String getLangPath()
	{
		return super.getLangPath() + ".EXCEEDINGLIMITS";
	}

	@Override
	public void print(CommandSender sender, String header)
	{
		super.print(sender, header);
		sender.sendMessage(header + locale.getLocaleMessage(sender, lowerLimit ? "LOWER" : "UPPER", message));
		if (limit != null)
			sender.sendMessage(header + locale.getLocaleMessage(sender, "LIMIT", limit));
	}
}
