package de.st_ddt.crazyplugin.events;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;

import de.st_ddt.crazyutil.Named;

public class CrazyPlayerAssociatesEvent extends CrazyEvent
{

	private static final HandlerList handlers = new HandlerList();
	protected final String name;
	protected int recursionDepth;
	protected final SortedSet<String> associates = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	protected final Queue<String> queue = new LinkedList<String>();

	public CrazyPlayerAssociatesEvent(final String name)
	{
		this(name, 0);
	}

	public CrazyPlayerAssociatesEvent(final String name, final int recursionDepth)
	{
		super();
		this.name = name;
		this.recursionDepth = recursionDepth;
		this.associates.add(name);
	}

	public String getSearchedName()
	{
		return name;
	}

	private void add(final String name)
	{
		if (associates.add(name))
			queue.add(name);
	}

	public void addNames(final String... names)
	{
		if (recursionDepth > 0)
			for (final String name : names)
				add(name);
		else
			for (final String name : names)
				associates.add(name);
	}

	public void addPlayers(final OfflinePlayer... names)
	{
		if (recursionDepth > 0)
			for (final OfflinePlayer name : names)
				add(name.getName());
		else
			for (final OfflinePlayer name : names)
				associates.add(name.getName());
	}

	public void addNamed(final Named... names)
	{
		if (recursionDepth > 0)
			for (final Named name : names)
				add(name.getName());
		else
			for (final Named name : names)
				associates.add(name.getName());
	}

	public void addNames(final Collection<String> names)
	{
		if (recursionDepth > 0)
			for (final String name : names)
				add(name);
		else
			for (final String name : names)
				associates.add(name);
	}

	public void addPlayers(final Collection<? extends OfflinePlayer> names)
	{
		if (recursionDepth > 0)
			for (final OfflinePlayer name : names)
				add(name.getName());
		else
			for (final OfflinePlayer name : names)
				associates.add(name.getName());
	}

	public void addNamed(final Collection<? extends Named> names)
	{
		if (recursionDepth > 0)
			for (final Named name : names)
				add(name.getName());
		else
			for (final Named name : names)
				associates.add(name.getName());
	}

	public SortedSet<String> getAssociates()
	{
		return associates;
	}

	@Override
	public void callEvent()
	{
		super.callEvent();
		if (!queue.isEmpty())
			queue.add(null);
		while (!queue.isEmpty() && recursionDepth > 0)
		{
			final String name = queue.poll();
			if (name == null)
			{
				recursionDepth--;
				if (!queue.isEmpty())
					queue.add(null);
			}
			else
			{
				final CrazyPlayerAssociatesEvent event = new CrazyPlayerAssociatesEvent(name);
				event.callEvent();
				addNames(event.getAssociates());
			}
		}
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
