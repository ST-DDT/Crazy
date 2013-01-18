package de.st_ddt.crazyplugin.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import org.bukkit.event.HandlerList;

import de.st_ddt.crazyplugin.CrazyLightPluginInterface;

public class CrazyPlayerAssociatesEvent extends CrazyEvent
{

	private static final HandlerList handlers = new HandlerList();
	protected final String name;
	protected final int recursionDepth;
	protected final SortedSet<String> associates;

	public CrazyPlayerAssociatesEvent(final String name)
	{
		this(name, 0);
	}

	public CrazyPlayerAssociatesEvent(final String name, final int recursionDepth)
	{
		this(name, recursionDepth, new TreeSet<String>());
	}

	private CrazyPlayerAssociatesEvent(final String name, final int recursionDepth, final SortedSet<String> associates)
	{
		super();
		this.name = name;
		this.recursionDepth = recursionDepth;
		this.associates = associates;
		this.associates.add(name);
	}

	@Deprecated
	public CrazyPlayerAssociatesEvent(final CrazyLightPluginInterface plugin, final String name)
	{
		this(plugin, name, 0);
	}

	@Deprecated
	public CrazyPlayerAssociatesEvent(final CrazyLightPluginInterface plugin, final String name, final int recursionDepth)
	{
		super(plugin);
		this.name = name;
		this.recursionDepth = recursionDepth;
		this.associates = new TreeSet<String>();
		this.associates.add(name);
	}

	public String getSearchedName()
	{
		return name;
	}

	public void add(final String name)
	{
		if (associates.add(name))
			if (recursionDepth > 0)
				new CrazyPlayerAssociatesEvent(name, recursionDepth - 1, associates).callEvent();
	}

	public void addAll(final Collection<String> names)
	{
		if (recursionDepth > 0)
			for (final String name : new ArrayList<String>(names))
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
