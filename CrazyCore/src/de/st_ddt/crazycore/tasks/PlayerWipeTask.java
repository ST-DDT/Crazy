package de.st_ddt.crazycore.tasks;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyutil.ChatHelper;

public class PlayerWipeTask implements Runnable
{

	protected final String name;
	protected final OfflinePlayer player;
	protected final List<File> files = new LinkedList<File>();
	private int run = 0;

	public PlayerWipeTask(final String name, final Collection<File> files)
	{
		super();
		this.name = name;
		this.player = Bukkit.getOfflinePlayer(name);
		for (final File file : files)
			addFile(file);
	}

	public PlayerWipeTask(final String name, final Collection<File> files, final boolean worlds)
	{
		this(name, files);
		if (worlds)
			for (final World world : Bukkit.getWorlds())
				addFile(new File(world.getName() + File.separator + "players" + File.separator + name + ".dat"));
	}

	public PlayerWipeTask(final String name, final boolean foo, final Collection<String> paths)
	{
		super();
		this.name = name;
		this.player = Bukkit.getOfflinePlayer(name);
		for (final String path : paths)
			addFile(new File(ChatHelper.putArgs(path, name)));
	}

	public PlayerWipeTask(final String name, final boolean foo, final Collection<String> paths, final boolean worlds)
	{
		this(name, foo, paths);
		if (worlds)
			for (final World world : Bukkit.getWorlds())
				addFile(new File(world.getName() + File.separator + "players" + File.separator + name + ".dat"));
	}

	public void addFile(final File file)
	{
		try
		{
			files.add(file.getCanonicalFile());
		}
		catch (final IOException e)
		{
			files.add(file);
		}
	}

	@SuppressWarnings("deprecation")
	public void execute()
	{
		if (!fileCheck())
			Bukkit.getScheduler().scheduleAsyncDelayedTask(CrazyCore.getPlugin(), this, 20);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run()
	{
		run++;
		if (!fileCheck())
			Bukkit.getScheduler().scheduleAsyncDelayedTask(CrazyCore.getPlugin(), this, 20 * 10 * run * run);
	}

	public synchronized boolean fileCheck()
	{
		if (player != null)
			if (player.isOnline())
				return files.size() == 0;
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
