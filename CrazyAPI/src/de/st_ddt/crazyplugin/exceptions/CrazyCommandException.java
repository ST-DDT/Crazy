package de.st_ddt.crazyplugin.exceptions;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.source.Localized;

public class CrazyCommandException extends CrazyException
{

	private static final long serialVersionUID = 6006689682481259280L;
	protected String command = "";

	@Override
	public String getLangPath()
	{
		return super.getLangPath() + ".COMMAND";
	}

	@Override
	@Localized("CRAZYEXCEPTION.COMMAND $Command$")
	public void print(final CommandSender sender, final String header)
	{
		if (command == null)
			ChatHelper.sendMessage(sender, header, locale, "");
		else
			ChatHelper.sendMessage(sender, header, locale, command);
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

	public void addCommandPrefix(final String... prefixes)
	{
		shiftCommandIndex(prefixes.length);
	}

	public final void shiftCommandIndex()
	{
		shiftCommandIndex(1);
	}

	public void shiftCommandIndex(final int shift)
	{
	}
}
