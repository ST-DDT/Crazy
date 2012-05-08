package de.st_ddt.crazycore;

import java.nio.charset.Charset;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import de.st_ddt.crazyutil.locales.CrazyLocale;

public class CrazyCoreMessageListener implements PluginMessageListener
{

	protected final Charset charset = Charset.forName("UTF-8");

	@Override
	public void onPluginMessageReceived(final String channel, final Player player, final byte[] bytes)
	{
		if (!channel.equals("CrazyCore"))
			return;
		String message = new String(bytes, charset);
		if (message.startsWith("Q_Lang "))
		{
			message = message.substring(7);
			CrazyLocale.setUserLanguage(player, message);
		}
	}
}
