package de.st_ddt.crazyplugin;

import java.nio.charset.Charset;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public abstract class CrazyPluginMessageListener implements PluginMessageListener
{

	protected final CrazyPlugin plugin;
	protected final Charset charset = Charset.forName("UTF-8");

	public CrazyPluginMessageListener(CrazyPlugin plugin)
	{
		super();
		this.plugin = plugin;
	}

	public CrazyPlugin getPlugin()
	{
		return plugin;
	}

	@Override
	public final void onPluginMessageReceived(final String channel, final Player player, final byte[] bytes)
	{
		final String message = new String(bytes, charset);
		if (channel.equals(plugin.getName()))
		{
			if (message.equals("Q_PING"))
				sendPluginMessage(player, "A_PING " + plugin.getName());
			if (message.equals("Q_Version"))
				sendPluginMessage(player, "A_Version " + plugin.getDescription().getVersion());
		}
		pluginMessageRecieved(channel, player, message);
	}

	public abstract void pluginMessageRecieved(final String channel, final Player player, String message);

	protected final void sendPluginMessage(final Player player, final String message)
	{
		player.sendPluginMessage(plugin, plugin.getName(), message.getBytes(charset));
	}

	protected final void sendPluginMessage(final String channel, final Player player, final String message)
	{
		player.sendPluginMessage(plugin, channel, message.getBytes(charset));
	}
}
