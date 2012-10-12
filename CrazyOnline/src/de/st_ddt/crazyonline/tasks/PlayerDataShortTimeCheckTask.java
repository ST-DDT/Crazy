package de.st_ddt.crazyonline.tasks;

import de.st_ddt.crazyonline.CrazyOnline;
import de.st_ddt.crazyonline.data.OnlinePlayerData;
import de.st_ddt.crazyplugin.events.CrazyPlayerRemoveEvent;

public class PlayerDataShortTimeCheckTask implements Runnable
{

	protected final CrazyOnline plugin;
	protected final String name;

	public PlayerDataShortTimeCheckTask(final CrazyOnline plugin, final String name)
	{
		super();
		this.plugin = plugin;
		this.name = name;
	}

	@Override
	public void run()
	{
		final OnlinePlayerData data = plugin.getPlayerData(name);
		if (data == null)
			return;
		if (data.isOnline())
			return;
		if (data.getTimeTotal() > 5)
			return;
		new CrazyPlayerRemoveEvent(plugin, name).checkAndCallAsyncEvent();
	}
}
