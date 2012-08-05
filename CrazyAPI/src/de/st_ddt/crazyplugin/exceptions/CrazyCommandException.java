package de.st_ddt.crazyplugin.exceptions;

import org.bukkit.command.CommandSender;

public class CrazyCommandException extends CrazyException
{

	private static final long serialVersionUID = 6006689682481259280L;
	private String command = null;

	@Override
	public String getLangPath()
	{
		return super.getLangPath() + ".COMMAND";
	}

	@Override
	public void print(final CommandSender sender, final String header)
	{
		sender.sendMessage(header + locale.getLocaleMessage(sender, "Head"));
		if (command != null)
			sender.sendMessage(header + command);
	}

	public void setCommand(final String commandLabel)
	{
		command = commandLabel;
	}

	public void setCommand(final String commandLabel, final String[] args)
	{
		String command = "/" + commandLabel;
		for (final String arg : args)
			command += " " + arg;
		this.command = command;
	}

	public final void shiftCommandIndex()
	{
		shiftCommandIndex(1);
	}

	public void shiftCommandIndex(final int shift)
	{
	}
}
