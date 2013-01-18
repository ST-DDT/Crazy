package de.st_ddt.crazyplugin.events;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import org.bukkit.event.HandlerList;

import de.st_ddt.crazyplugin.CrazyLightPluginInterface;

public class CrazyPlayerAssociatesEvent extends CrazyEvent<CrazyLightPluginInterface>
{

	private static final HandlerList handlers = new HandlerList();
	protected final String name;
	protected final int recursionDepth;
	protected final SortedSet<String> associates = new TreeSet<String>();

	public CrazyPlayerAssociatesEvent(final String name)
	{
		super();
		this.name = name;
		this.recursionDepth = 0;
	}

	public CrazyPlayerAssociatesEvent(final String name, final int recursionDepth)
	{
		super();
		this.name = name;
		this.recursionDepth = recursionDepth;
	}

	@Deprecated
	public CrazyPlayerAssociatesEvent(final CrazyLightPluginInterface plugin, final String name)
	{
		super(plugin);
		this.name = name;
		this.recursionDepth = 0;
	}

	@Deprecated
	public CrazyPlayerAssociatesEvent(final CrazyLightPluginInterface plugin, final String name, final int recursionDepth)
	{
		super(plugin);
		this.name = name;
		this.recursionDepth = recursionDepth;
	}

	public String getSearchedName()
	{
		return name;
	}

	public void add(final String name)
	{
		if (associates.add(name))
			if (recursionDepth > 0)
			{
				final CrazyPlayerAssociatesEvent event = new CrazyPlayerAssociatesEvent(name, recursionDepth - 1);
				event.callEvent();
				addAll(event.getAssociates());
			}
	}

	public void addAll(final Collection<String> names)
	{
		if (recursionDepth > 0)
			for (final String name : names)
				add(name);
		else
			associates.addAll(names);
	}

	public SortedSet<String> getAssociates()
	{
		return associates;
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
