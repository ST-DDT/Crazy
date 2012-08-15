package de.st_ddt.crazyannouncer;

import java.util.ArrayList;

import org.bukkit.event.Event;

import de.st_ddt.crazyutil.trigger.EventTrigger;
import de.st_ddt.crazyutil.trigger.TriggerEventListener;

public class CrazyAnnouncerEventListener implements TriggerEventListener
{

	private final CrazyAnnouncer plugin;
	private final ArrayList<EventTrigger> triggers = new ArrayList<EventTrigger>();

	public CrazyAnnouncerEventListener(final CrazyAnnouncer plugin)
	{
		super();
		this.plugin = plugin;
	}

	public void Event(final Event event)
	{
		for (final EventTrigger trigger : triggers)
			if (trigger.getEventList().contains(event.getClass()))
			{
				trigger.run();
				return;
			}
	}

	public CrazyAnnouncer getPlugin()
	{
		return plugin;
	}

	@Override
	public void addTrigger(final EventTrigger eventTrigger)
	{
		if (!triggers.contains(eventTrigger))
			triggers.add(eventTrigger);
	}

	@Override
	public void removeTrigger(final EventTrigger eventTrigger)
	{
		triggers.add(eventTrigger);
	}
}
