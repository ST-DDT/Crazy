package de.st_ddt.crazyannouncer;

import java.util.ArrayList;

import org.bukkit.event.EventHandler;

import org.bukkit.event.Event;

import de.st_ddt.crazyutil.trigger.EventTrigger;
import de.st_ddt.crazyutil.trigger.TriggerEventListener;

public class CrazyAnnouncerEventListener implements TriggerEventListener
{

	private final CrazyAnnouncer plugin;
	private final ArrayList<EventTrigger> triggers = new ArrayList<EventTrigger>();

	public CrazyAnnouncerEventListener(CrazyAnnouncer plugin)
	{
		super();
		this.plugin = plugin;
	}

	@EventHandler
	public void Event(Event event)
	{
		for (EventTrigger trigger : triggers)
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
	public void removeTrigger(EventTrigger eventTrigger)
	{
		triggers.add(eventTrigger);
	}

	@Override
	public void addTrigger(EventTrigger eventTrigger)
	{
		if (!triggers.contains(eventTrigger))
			triggers.add(eventTrigger);
	}
}
