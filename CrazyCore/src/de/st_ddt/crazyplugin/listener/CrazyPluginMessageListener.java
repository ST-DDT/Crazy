package de.st_ddt.crazyplugin.listener;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import de.st_ddt.crazyplugin.CrazyPlugin;

public abstract class CrazyPluginMessageListener implements PluginMessageListener
{

	protected final CrazyPlugin plugin;
	protected final static Charset charset = Charset.forName("UTF-8");

	public CrazyPluginMessageListener(final CrazyPlugin plugin)
	{
		super();
		this.plugin = plugin;
	}

	public final CrazyPlugin getPlugin()
	{
		return plugin;
	}

	@Override
	public final void onPluginMessageReceived(final String channel, final Player player, final byte[] bytes)
	{
		final String message = new String(bytes, charset);
		final String[] split = message.split(" ", 2);
		if (split.length == 0)
			return;
		final String header = split[0];
		String args;
		if (split.length == 1)
			args = "";
		else
			args = split[2];
		if (header.startsWith("Q_"))
		{
			if (channel.equals(plugin.getName()))
			{
				if (header.equalsIgnoreCase("Ping"))
				{
					sendPluginMessage(player, "A_PING " + plugin.getName());
					return;
				}
				else if (header.equalsIgnoreCase("Version"))
				{
					sendPluginMessage(player, "A_Version " + plugin.getDescription().getVersion());
					return;
				}
			}
			pluginMessageQuerryRecieved(channel, player, header.substring(2), args);
		}
		if (header.startsWith("A_"))
			pluginMessageAnswerRecieved(channel, player, header.substring(2), args);
		if (header.startsWith("R_"))
			pluginMessageRawRecieved(channel, player, header.substring(2), shiftArray(bytes, header.length() + 1));
	}

	protected void pluginMessageQuerryRecieved(final String channel, final Player player, final String header, final String args)
	{
	}

	protected void pluginMessageAnswerRecieved(final String channel, final Player player, final String header, final String args)
	{
	}

	protected void pluginMessageRawRecieved(final String channel, final Player player, final String header, final byte[] bytes)
	{
	}

	protected final void sendPluginMessage(final Player player, final String message)
	{
		player.sendPluginMessage(plugin, plugin.getName(), message.getBytes(charset));
	}

	protected final void sendPluginMessage(final String channel, final Player player, final String message)
	{
		player.sendPluginMessage(plugin, channel, message.getBytes(charset));
	}

	protected static byte[] shiftArray(final byte[] array, final int anz)
	{
		if (anz >= array.length)
			return Arrays.copyOf(array, 0);
		return Arrays.copyOfRange(array, anz, array.length - 1);
	}
}
