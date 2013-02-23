package de.st_ddt.crazyplugin.exceptions;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.source.Localized;

public class CrazyCommandParameterException extends CrazyCommandException
{

	private static final long serialVersionUID = 4734610798821983664L;
	private int number;
	private final String type;
	private final Collection<String> allowed;

	public CrazyCommandParameterException(final int number, final String type)
	{
		this(number, type, new String[0]);
	}

	public CrazyCommandParameterException(final int number, final String type, final String... allowed)
	{
		super();
		this.number = number;
		this.type = type;
		this.allowed = new ArrayList<String>(allowed.length);
		for (final String allow : allowed)
			this.allowed.add(allow);
	}

	public CrazyCommandParameterException(final int number, final String type, final Collection<String> allowed)
	{
		super();
		this.number = number;
		this.type = type;
		this.allowed = allowed;
	}

	@Override
	public String getLangPath()
	{
		return super.getLangPath() + ".PARAMETER";
	}

	@Override
	@Localized("CRAZYEXCEPTION.COMMAND.PARAMETER $Command$ $Number$ $Type$ $Allowed$")
	public void print(final CommandSender sender, final String header)
	{
		ChatHelper.sendMessage(sender, header, locale, command, number, type, ChatHelper.listingString("\\n", allowed));
	}

	@Override
	public void setCommand(final String commandLabel, final String[] args)
	{
		if (number <= args.length)
			args[number - 1] = ChatColor.RED + args[number - 1] + ChatColor.WHITE;
		super.setCommand(commandLabel, args);
	}

	@Override
	public void shiftCommandIndex(final int shift)
	{
		number += shift;
	}
}
