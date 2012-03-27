package de.st_ddt.crazyplugin.exceptions;

import org.bukkit.command.CommandSender;

public class CrazyCommandAlreadyExistsException extends CrazyCommandException
{

	private static final long serialVersionUID = -7687691927850799497L;
	private final String creation;
	private final String type;

	public CrazyCommandAlreadyExistsException(String type, String creation)
	{
		super();
		this.creation = creation;
		this.type = type;
	}

	@Override
	public String getLangPath()
	{
		return super.getLangPath() + ".ALREADYEXISTS";
	}

	@Override
	public void print(CommandSender sender, String header)
	{
		super.print(sender, header);
		sender.sendMessage(header + locale.getLocaleMessage(sender, "ERROR", type, creation));
	}
}
