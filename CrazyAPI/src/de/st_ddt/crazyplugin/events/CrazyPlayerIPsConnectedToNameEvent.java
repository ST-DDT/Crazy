package de.st_ddt.crazyplugin.events;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import org.bukkit.event.HandlerList;

public class CrazyPlayerIPsConnectedToNameEvent extends CrazyEvent
{

	private static final HandlerList handlers = new HandlerList();
	protected final String name;
	protected final SortedSet<String> ips = new TreeSet<String>();

	public CrazyPlayerIPsConnectedToNameEvent(final String name)
	{
		super();
		this.name = name;
	}

	public String getSearchedName()
	{
		return name;
	}

	public void add(final String ip)
	{
		ips.add(ip);
	}

	public void addAll(final Collection<String> ips)
	{
		this.ips.addAll(ips);
	}

	public SortedSet<String> getIPs()
	{
		return ips;
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
