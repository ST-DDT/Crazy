package de.st_ddt.crazycore;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import de.st_ddt.crazyutil.locales.CrazyLocale;

public class CrazyCoreLanguageListener implements PluginMessageListener
{

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message)
	{
		String text = String.valueOf(message);
		if (channel.equalsIgnoreCase("crazylanguage"))
			if (text.length() == 5)
				CrazyLocale.setUserLanguage(player, text);
	}
}
