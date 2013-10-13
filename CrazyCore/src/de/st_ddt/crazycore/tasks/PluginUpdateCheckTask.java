package de.st_ddt.crazycore.tasks;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyutil.source.Localized;

public class PluginUpdateCheckTask implements Runnable
{

	private final CrazyCore plugin;
	private final CommandSender sender;
	private final boolean force;

	public PluginUpdateCheckTask()
	{
		this(null, null, false);
	}

	public PluginUpdateCheckTask(final CrazyCore plugin, final CommandSender sender)
	{
		this(plugin, sender, false);
	}

	public PluginUpdateCheckTask(final CrazyCore plugin, final CommandSender sender, final boolean force)
	{
		super();
		this.plugin = plugin;
		this.sender = sender;
		this.force = force;
	}

	@Override
	@Localized("CRAZYCORE.COMMAND.UPDATECHECK.FINISHED")
	public void run()
	{
		for (final CrazyPlugin plugin : CrazyPlugin.getCrazyPlugins())
			plugin.checkForUpdateWithMessage(force, null);
		if (plugin != null && sender != null)
			plugin.sendLocaleMessage("COMMAND.UPDATECHECK.FINISHED", sender);
	}
}
