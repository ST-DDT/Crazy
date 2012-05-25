package de.st_ddt.crazyplugin.exceptions;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CrazyCommandPermissionException extends CrazyCommandException
{

	private static final long serialVersionUID = -6529132768225981196L;

	public CrazyCommandPermissionException()
	{
		super();
	}

	@Override
	public String getLangPath()
	{
		return super.getLangPath() + ".PERMISSION";
	}

	@Override
	public void print(final CommandSender sender, final String header)
	{
		super.print(sender, header);
		sender.sendMessage(header + ChatColor.RED + locale.getLocaleMessage(sender, "ERROR"));
	}
}
