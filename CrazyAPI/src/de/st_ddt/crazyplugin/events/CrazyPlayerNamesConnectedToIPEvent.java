package de.st_ddt.crazyplugin.events;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import org.bukkit.event.HandlerList;

import de.st_ddt.crazyplugin.CrazyLightPluginInterface;

public class CrazyPlayerNamesConnectedToIPEvent extends CrazyEvent<CrazyLightPluginInterface>
{

	private static final HandlerList handlers = new HandlerList();
	protected final SortedSet<String> names = new TreeSet<String>();
	protected final String ip;

	public CrazyPlayerNamesConnectedToIPEvent(final String ip)
	{
		super();
		this.ip = ip;
	}

	@Deprecated
	public CrazyPlayerNamesConnectedToIPEvent(final CrazyLightPluginInterface plugin, final String ip)
	{
		super(plugin);
		this.ip = ip;
	}

	public String getSearchedIP()
	{
		return ip;
	}

	public void add(final String name)
	{
		names.add(name);
	}

	public void addAll(final String... names)
	{
		for (final String name : names)
			add(name);
	}

	public void addAll(final Collection<String> names)
	{
		this.names.addAll(names);
	}

	public SortedSet<String> getNames()
	{
		return names;
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
