package de.st_ddt.crazyplugin.exceptions;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CrazyCommandParameterException extends CrazyCommandException
{

	private static final long serialVersionUID = 4734610798821983664L;
	private int number;
	private final String type;
	private final String[] allowed;

	public CrazyCommandParameterException(int number, String type)
	{
		this(number, type, new String[0]);
	}

	public CrazyCommandParameterException(int number, String type, String... allowed)
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
	public void print(CommandSender sender, String header)
	{
		super.print(sender, header);
		sender.sendMessage(header + locale.getLocaleMessage(sender, "ERROR", String.valueOf(number), type));
		for (String allow : allowed)
			sender.sendMessage(header + allow);
	}

	@Override
	public void setCommand(String commandLabel, String[] args)
	{
		if (number <= args.length)
			args[number - 1] = ChatColor.RED + args[number - 1] + ChatColor.WHITE;
		super.setCommand(commandLabel, args);
	}

	@Override
	public void shiftCommandIndex(int shift)
	{
		number += shift;
		super.shiftCommandIndex(shift);
	}
}
