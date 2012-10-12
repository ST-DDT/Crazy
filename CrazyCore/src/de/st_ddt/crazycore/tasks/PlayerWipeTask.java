package de.st_ddt.crazycore.tasks;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import de.st_ddt.crazycore.CrazyCore;

public class PlayerWipeTask implements Runnable
{

	protected final String name;
	protected final OfflinePlayer player;
	protected final LinkedList<File> files = new LinkedList<File>();
	private int run = 0;

	public PlayerWipeTask(final String name)
	{
		super();
		this.name = name;
		this.player = Bukkit.getOfflinePlayer(name);
		for (final World world : Bukkit.getWorlds())
		{
			final File tempFile = new File(world.getName() + File.separator + "players" + File.separator + name + ".dat");
			try
			{
				files.add(tempFile.getCanonicalFile());
			}
			catch (final IOException e)
			{
				files.add(tempFile);
			}
		}
		final Iterator<File> it = files.iterator();
		while (it.hasNext())
			if (!it.next().exists())
				it.remove();
	}

	public void execute()
	{
		if (!fileCheck())
			Bukkit.getScheduler().scheduleAsyncDelayedTask(CrazyCore.getPlugin(), this, 20);
	}

	@Override
	public void run()
	{
		if (player != null)
			if (player.isOnline())
				return;
		run++;
		if (!fileCheck())
			Bukkit.getScheduler().scheduleAsyncDelayedTask(CrazyCore.getPlugin(), this, 20 * 10 * run * run);
	}

	public synchronized boolean fileCheck()
	{
		final Iterator<File> it = files.iterator();
		while (it.hasNext())
		{
			final File file = it.next();
			if (file.exists())
				try
				{
					if (file.delete())
						it.remove();
				}
				catch (final SecurityException e)
				{}
			else
				it.remove();
		}
		return files.size() == 0;
	}
}
