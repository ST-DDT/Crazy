package de.st_ddt.crazyarena.utils;

import java.io.File;

import org.bukkit.entity.Player;

import de.st_ddt.crazyutil.PlayerSaver;

public class ArenaPlayerSaver extends PlayerSaver
{

	protected String displayName;
	protected String listName;

	public ArenaPlayerSaver(final Player player, final boolean clearAfterBackup)
	{
		super(player, clearAfterBackup);
	}

	public ArenaPlayerSaver(final Player player)
	{
		super(player);
	}

	public ArenaPlayerSaver(final String name, final File file)
	{
		super(name, file);
	}

	public ArenaPlayerSaver(final String name, final String path)
	{
		super(name, path);
	}

	public ArenaPlayerSaver(final String name)
	{
		super(name);
	}

	@Override
	public void backup(final Player player)
	{
		super.backup(player);
		displayName = player.getDisplayName();
		listName = player.getPlayerListName();
	}

	@Override
	public void restore(final Player player)
	{
		if (!backup)
			return;
		super.restore(player);
		player.setDisplayName(displayName);
		player.setPlayerListName(listName);
	}

	@Override
	public void clear(final Player player)
	{
		super.clear(player);
		player.setDisplayName(player.getName());
		player.setPlayerListName(null);
	}

	@Override
	public void reset()
	{
		super.reset();
		displayName = null;
		listName = null;
	}

	@Override
	public void load()
	{
		super.load();
		displayName = config.getString("displayName");
		listName = config.getString("listName");
	}

	@Override
	public void save()
	{
		super.save();
		config.set("displayName", displayName);
		config.set("listName", listName);
	}
}
