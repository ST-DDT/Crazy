package de.st_ddt.crazyplugin.exceptions;

import org.bukkit.command.CommandSender;

public class CrazyCommandCircumstanceException extends CrazyCommandException
{

	private static final long serialVersionUID = 1847386590932930809L;
	private final String mustBe, current;

	public CrazyCommandCircumstanceException()
	{
		this(null, null);
	}

	public CrazyCommandCircumstanceException(final String mustBe)
	{
		this(mustBe, null);
	}

	public CrazyCommandCircumstanceException(final String mustBe, final String current)
	{
		super();
		this.mustBe = mustBe;
		this.current = current;
	}

	@Override
	public String getLangPath()
	{
		return super.getLangPath() + ".CIRCUMSTANCE";
	}

	@Override
	public void print(final CommandSender sender, final String header)
	{
		super.print(sender, header);
		if (mustBe != null)
		{
			sender.sendMessage(header + locale.getLocaleMessage(sender, "MUSTBE", mustBe));
			if (current != null)
				sender.sendMessage(header + locale.getLocaleMessage(sender, "NOW", current));
		}
		else
			sender.sendMessage(header + locale.getLocaleMessage(sender, "ERROR"));
	}
}
