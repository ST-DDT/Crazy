package de.st_ddt.crazycore.data;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.data.PlayerData;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public class PseudoPlayerData extends PlayerData<PseudoPlayerData>
{

	public PseudoPlayerData(final String name)
	{
		super(name);
	}

	@Override
	public String getParameter(final int index)
	{
		switch (index)
		{
			case 0:
				return name;
			default:
				return "";
		}
	}

	@Override
	public int getParameterCount()
	{
		return 1;
	}

	public CrazyCore getPlugin()
	{
		return CrazyCore.getPlugin();
	}

	@Override
	protected String getChatHeader()
	{
		return getPlugin().getChatHeader();
	}

	@Override
	public void show(final CommandSender target)
	{
		show(target, getChatHeader(), false);
	}

	@Override
	public void showDetailed(final CommandSender target, final String chatHeader)
	{
		final CrazyLocale locale = CrazyLocale.getLocaleHead().getSecureLanguageEntry("CRAZYCORE.PLAYERINFO");
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("LANGUAGE"), CrazyLocale.getUserLanguageName(name, true));
	}
}
