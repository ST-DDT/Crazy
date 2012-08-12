package de.st_ddt.crazyplugin.events;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyutil.ChatHelper;

public class CrazyPlayerRemoveEvent extends CrazyEvent<CrazyPluginInterface>
{

	private static final HandlerList handlers = new HandlerList();
	private final HashSet<JavaPlugin> deletions = new HashSet<JavaPlugin>();
	protected final String player;

	public CrazyPlayerRemoveEvent(final CrazyPluginInterface plugin, final OfflinePlayer player)
	{
		super(plugin);
		this.player = player.getName();
	}

	public CrazyPlayerRemoveEvent(final CrazyPluginInterface plugin, final String player)
	{
		super(plugin);
		this.player = player;
	}

	public String getPlayer()
	{
		return player;
	}

	public void markDeletion(final JavaPlugin plugin)
	{
		deletions.add(plugin);
	}

	public HashSet<JavaPlugin> getDeletions()
	{
		return deletions;
	}

	public int getDeletionsCount()
	{
		return deletions.size();
	}

	public String getDeletionsList()
	{
		final ArrayList<String> list = new ArrayList<String>();
		for (final JavaPlugin plugin : deletions)
			list.add(plugin.getName());
		return ChatHelper.listingString(list);
	}

	public void callEvent()
	{
		Bukkit.getPluginManager().callEvent(this);
	}

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}
}
