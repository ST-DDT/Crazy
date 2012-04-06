package de.st_ddt.crazyutil.trigger;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazyutil.NamedRunnable;

public class EventTrigger extends Trigger
{

	TriggerEventListener listener;
	List<Class<? extends Event>> events;

	@SuppressWarnings("unchecked")
	public EventTrigger(ConfigurationSection config, List<NamedRunnable> actionlist, JavaPlugin plugin, TriggerEventListener listener)
	{
		super(config, actionlist, plugin);
		this.listener = listener;
		this.events = new ArrayList<Class<? extends Event>>();
		for (String clazz : config.getStringList("events"))
			try
			{
				events.add((Class<Event>) Class.forName(clazz));
			}
			catch (ClassNotFoundException e)
			{
				System.out.println("Class " + clazz + " not found!");
			}
			catch (ClassCastException e)
			{
				System.out.println("Class " + clazz + " is not an event!");
			}
	}

	public EventTrigger(String name, List<NamedRunnable> actionlist, JavaPlugin plugin, List<Class<? extends Event>> events, TriggerEventListener listener)
	{
		super(name, actionlist, plugin);
		this.listener = listener;
		this.events = events;
	}

	@Override
	public boolean needToBeSaved()
	{
		return true;
	}

	@Override
	public void register()
	{
		listener.addTrigger(this);
	}

	@Override
	public void unregister()
	{
		listener.removeTrigger(this);
	}

	public List<Class<? extends Event>> getEventList()
	{
		return events;
	}
}
