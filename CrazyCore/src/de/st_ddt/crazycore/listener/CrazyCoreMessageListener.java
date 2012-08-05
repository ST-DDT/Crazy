package de.st_ddt.crazycore.listener;

import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.listener.CrazyPluginMessageListener;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public class CrazyCoreMessageListener extends CrazyPluginMessageListener
{

	public CrazyCoreMessageListener(final CrazyPlugin plugin)
	{
		super(plugin);
	}

	@Override
	protected void pluginMessageQuerryRecieved(final String channel, final Player player, final String header, final String args)
	{
		if (channel.equals(plugin.getName()))
		{
			if (header.equalsIgnoreCase("Language"))
				sendPluginMessage(player, "A_Language " + ChatHelper.listingString(CrazyLocale.getActiveLanguages()));
			else if (header.equalsIgnoreCase("LanguageName"))
				sendPluginMessage(player, "A_LanguageName " + args + " " + CrazyLocale.getLanguageName(args));
		}
	}

	@Override
	protected void pluginMessageAnswerRecieved(final String channel, final Player player, final String header, final String args)
	{
		if (channel.equals(plugin.getName()))
		{
			if (header.equalsIgnoreCase("Language"))
				CrazyLocale.setUserLanguage(player, args);
		}
	}

	@Override
	protected void pluginMessageRawRecieved(final String channel, final Player player, final String header, final byte[] bytes)
	{
	}
}
