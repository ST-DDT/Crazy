package de.st_ddt.crazylogin.exceptions;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.source.Localized;

public class PasswordRejectedException extends CrazyException
{

	private static final long serialVersionUID = -471960969682688153L;
	protected String reason;

	public PasswordRejectedException(final String reason)
	{
		super();
		this.reason = reason;
	}

	@Override
	public String getLangPath()
	{
		return "CRAZYLOGIN.EXCEPTION.REGISTER.PASSWORDREJECTED";
	}

	@Override
	@Localized("CRAZYLOGIN.EXCEPTION.REGISTER.PASSWORDREJECTED $Reason$")
	public void print(final CommandSender sender, final String header)
	{
		ChatHelper.sendMessage(sender, header, locale, reason);
	}
}
