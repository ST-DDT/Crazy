package de.st_ddt.crazycore.tasks;

import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyutil.locales.Localized;

public class PluginUpdateCheckTask implements Runnable
{

	final boolean force;

	public PluginUpdateCheckTask()
	{
		super();
		this.force = false;
	}

	public PluginUpdateCheckTask(final boolean force)
	{
		super();
		this.force = force;
	}

	@Override
	@Localized("CRAZYPLUGIN.PLUGININFO.UPDATE $NewVersion$")
	public void run()
	{
		for (final CrazyPlugin plugin : CrazyPlugin.getCrazyPlugins())
			if (plugin.checkForUpdate(force))
				plugin.broadcastLocaleMessage(true, "crazycore.updatecheck", "PLUGININFO.UPDATE", plugin.getUpdateVersion());
	}
}
