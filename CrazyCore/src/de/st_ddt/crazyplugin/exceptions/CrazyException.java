package de.st_ddt.crazyplugin.exceptions;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyutil.locales.CrazyLocale;

public class CrazyException extends Exception
{

	private static final long serialVersionUID = -1585496766103859503L;
	private boolean printStackTrace;
	protected CrazyLocale locale;

	public CrazyException()
	{
		super();
		this.locale = CrazyLocale.getLocaleHead().getLanguageEntry(getLangPath());
	}

	public void setPrintStackTrace(boolean printStackTrace)
	{
		this.printStackTrace = printStackTrace;
	}

	public String getLangPath()
	{
		return "CRAZYEXCEPTION";
	}

	public void print(CommandSender sender, String header)
	{
		sender.sendMessage(header + locale.getLocaleMessage(sender, "Head"));
		if (printStackTrace)
			printStackTrace();
	}

	public final void print(CommandSender sender)
	{
		print(sender, "");
	}
}
