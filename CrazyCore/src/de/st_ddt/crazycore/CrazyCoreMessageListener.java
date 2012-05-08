package de.st_ddt.crazycore;

import org.bukkit.entity.Player;
import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.CrazyPluginMessageListener;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public class CrazyCoreMessageListener extends CrazyPluginMessageListener
{

	public CrazyCoreMessageListener(CrazyPlugin plugin)
	{
		super(plugin);
	}

	@Override
	public void pluginMessageRecieved(String channel, Player player, String message)
	{
		if (message.startsWith("Q_Lang "))
		{
			message = message.substring(7);
			CrazyLocale.setUserLanguage(player, message);
		}
	}
}
