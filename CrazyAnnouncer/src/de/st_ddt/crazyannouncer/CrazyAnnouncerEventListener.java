package de.st_ddt.crazyannouncer;

import java.util.ArrayList;

import org.bukkit.event.EventHandler;

import org.bukkit.event.Listener;
import org.bukkit.event.Event;

public class CrazyAnnouncerEventListener implements Listener
{

	private final CrazyAnnouncer plugin;
	private final ArrayList<EventAnnouncement> announcements = new ArrayList<EventAnnouncement>();

	public CrazyAnnouncerEventListener(CrazyAnnouncer plugin)
	{
		super();
		this.plugin = plugin;
	}

	public final void addEventAnnouncement(EventAnnouncement announcement)
	{
		if (announcements.contains(announcement))
			announcements.add(announcement);
	}

	public final void removeEventAnnouncement(EventAnnouncement announcement)
	{
		announcements.remove(announcement);
	}

	@EventHandler
	public void Event(Event event)
	{
		for (EventAnnouncement announcement : announcements)
			for (Class<? extends Event> trigger : announcement.listTriggers())
				if (trigger.isAssignableFrom(event.getClass()))
				{
					announcement.run();
					return;
				}
	}

	public CrazyAnnouncer getPlugin()
	{
		return plugin;
	}

	public ArrayList<EventAnnouncement> getAnnouncements()
	{
		return announcements;
	}
}
