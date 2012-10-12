package de.st_ddt.crazyplugin.exceptions;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.Localized;

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
	@Localized("CRAZYEXCEPTION.COMMAND.PERMISSION $Command$")
	public void print(final CommandSender sender, final String header)
	{
		ChatHelper.sendMessage(sender, header, locale, command);
	}
}
