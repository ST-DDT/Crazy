package de.st_ddt.crazyplugin.exceptions;

import org.bukkit.command.CommandSender;

public class CrazyCommandNoSuchException extends CrazyCommandException
{

	private static final long serialVersionUID = -7687691927850799497L;
	private final String searched;
	private final String type;

	public CrazyCommandNoSuchException(final String type, final String searched)
	{
		super();
		this.searched = searched;
		this.type = type;
	}

	@Override
	public String getLangPath()
	{
		return super.getLangPath() + ".NOSUCH";
	}

	@Override
	public void print(final CommandSender sender, final String header)
	{
		super.print(sender, header);
		sender.sendMessage(header + locale.getLocaleMessage(sender, "ERROR", type, searched));
	}
}
