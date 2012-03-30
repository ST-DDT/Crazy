package de.st_ddt.crazyannouncer;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Event;

public class EventAnnouncement extends Announcement<Class<? extends Event>>
{

	public EventAnnouncement(String name, CrazyAnnouncer plugin)
	{
		super(name, plugin);
	}

	@Override
	public void addTrigger(String... strings)
	{
		// TODO Convert String to EventClass
	}

	@Override
	public List<String> listTriggerNames()
	{
		List<String> list = new ArrayList<String>();
		for (Class<? extends Event> trigger : triggers)
			list.add(trigger.toString().substring(6));
		return list;
	}
}
