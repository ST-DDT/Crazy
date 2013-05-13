package de.st_ddt.crazylogin.exceptions;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.source.Localized;

public class PasswordRejectedLengthException extends PasswordRejectedException
{

	private static final long serialVersionUID = 8155158736271511951L;
	private final int currentLength;
	private final int minLength;

	public PasswordRejectedLengthException(final int currentLength, final int minLength)
	{
		super("This password is too short (unsafe). (Min: " + minLength + ", Current: " + currentLength + ")");
		this.currentLength = currentLength;
		this.minLength = minLength;
	}

	@Override
	public String getLangPath()
	{
		return "CRAZYLOGIN.EXCEPTION.REGISTER.PASSWORDREJECTED.LENGTH";
	}

	@Override
	@Localized("CRAZYLOGIN.EXCEPTION.REGISTER.PASSWORDREJECTED.LENGTH $CurrentLength$ $MinLength$")
	public void print(final CommandSender sender, final String header)
	{
		ChatHelper.sendMessage(sender, header, locale, currentLength, minLength);
	}
}
