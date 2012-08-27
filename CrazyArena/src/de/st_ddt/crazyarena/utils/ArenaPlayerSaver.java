package de.st_ddt.crazyarena.utils;

import java.io.File;

import org.bukkit.entity.Player;

import de.st_ddt.crazyutil.PlayerSaver;

public class ArenaPlayerSaver extends PlayerSaver
{

	protected String displayName;
	protected String listName;

	public ArenaPlayerSaver(Player player, boolean clearAfterBackup)
	{
		super(player, clearAfterBackup);
	}

	public ArenaPlayerSaver(Player player)
	{
		super(player);
	}

	public ArenaPlayerSaver(String name, File file)
	{
		super(name, file);
	}

	public ArenaPlayerSaver(String name, String path)
	{
		super(name, path);
	}

	public ArenaPlayerSaver(String name)
	{
		super(name);
	}

	@Override
	public void backup(Player player)
	{
		super.backup(player);
		displayName = player.getDisplayName();
		listName = player.getPlayerListName();
	}

	@Override
	public void restore(Player player)
	{
		if (!backup)
			return;
		super.restore(player);
		player.setDisplayName(displayName);
		player.setPlayerListName(listName);
	}

	@Override
	public void clear(Player player)
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
