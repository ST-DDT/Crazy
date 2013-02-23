package de.st_ddt.crazyplugin.exceptions;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.Showable;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.source.Localized;

public class CrazyException extends Exception implements Showable
{

	private static final long serialVersionUID = -1585496766103859503L;
	protected boolean printStackTrace = false;
	protected CrazyLocale locale;

	public CrazyException()
	{
		super();
		this.locale = CrazyLocale.getLocaleHead().getLanguageEntry(getLangPath());
	}

	public void setPrintStackTrace(final boolean printStackTrace)
	{
		this.printStackTrace = printStackTrace;
	}

	public String getLangPath()
	{
		return "CRAZYEXCEPTION";
	}

	@Localized("CRAZYEXCEPTION")
	public void print(final CommandSender sender, final String header)
	{
		ChatHelper.sendMessage(sender, header, locale);
		if (printStackTrace)
			printStackTrace();
	}

	public final void print(final CommandSender sender)
	{
		print(sender, "");
	}

	@Override
	public void show(final CommandSender target)
	{
		print(target);
	}

	@Override
	public void show(final CommandSender target, final String chatHeader, final boolean showDetailed)
	{
		final boolean temp = printStackTrace;
		printStackTrace = true;
		print(target, chatHeader);
		printStackTrace = temp;
	}

	@Override
	public String getShortInfo()
	{
		return locale.getDefaultLanguageText();
	}
}
