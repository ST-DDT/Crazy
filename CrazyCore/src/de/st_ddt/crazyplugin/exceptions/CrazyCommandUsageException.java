package de.st_ddt.crazyplugin.exceptions;

import org.bukkit.command.CommandSender;

public class CrazyCommandUsageException extends CrazyCommandException
{

	private static final long serialVersionUID = 1755697197006991043L;
	private final String[] validUsage;

	public CrazyCommandUsageException(String... validUsage)
	{
		super();
		this.validUsage = validUsage;
	}

	@Override
	public String getLangPath()
	{
		return super.getLangPath() + ".USAGE";
	}

	@Override
	public void print(CommandSender sender, String header)
	{
		super.print(sender, header);
		sender.sendMessage(header + locale.getLocaleMessage(sender, "ERROR"));
		for (String usage : validUsage)
			sender.sendMessage(header + usage);
	}
}
