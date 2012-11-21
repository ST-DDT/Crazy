package de.st_ddt.crazychats.tasks;

import java.util.Iterator;
import java.util.Set;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.channels.GroupChannelInterface;

public class GroupChannelCleanupTask implements Runnable
{

	private final CrazyChats plugin;

	public GroupChannelCleanupTask(final CrazyChats plugin)
	{
		super();
		this.plugin = plugin;
	}

	@Override
	public void run()
	{
		final Set<GroupChannelInterface> channels = plugin.getGroupChannels();
		synchronized (channels)
		{
			final Iterator<GroupChannelInterface> it = channels.iterator();
			while (it.hasNext())
				if (it.next().canBeDeleted())
					it.remove();
		}
	}
}
