package de.st_ddt.crazycore.tasks;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import de.st_ddt.crazycore.CrazyCore;

public class PlayerWipeTask implements Runnable
{

	protected final String name;
	protected final OfflinePlayer player;
	protected final File file;
	private int run = 0;

	public PlayerWipeTask(final String name)
	{
		super();
		this.name = name;
		this.player = Bukkit.getOfflinePlayer(name);
		File tempFile = new File(Bukkit.getWorlds().get(0).getName() + File.separator + "players" + File.separator + name + ".dat");
		try
		{
			tempFile = tempFile.getCanonicalFile();
		}
		catch (IOException e)
		{
			tempFile = new File(Bukkit.getWorlds().get(0).getName() + File.separator + "players" + File.separator + name + ".dat");
		}
		file = tempFile;
	}

	public void execute()
	{
		if (!file.exists())
			return;
		try
		{
			file.delete();
		}
		catch (final SecurityException e)
		{}
		Bukkit.getScheduler().scheduleAsyncDelayedTask(CrazyCore.getPlugin(), this, 20);
	}

	@Override
	public void run()
	{
		if (!file.exists())
			return;
		if (player != null)
			if (player.isOnline())
				return;
		run++;
		boolean done = false;
		try
		{
			done = file.delete();
		}
		catch (final SecurityException e)
		{}
		finally
		{
			if (!done)
				Bukkit.getScheduler().scheduleAsyncDelayedTask(CrazyCore.getPlugin(), this, 20 * 10 * run * run);
		}
	}
}
