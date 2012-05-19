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
	public EventTrigger(final ConfigurationSection config, final List<NamedRunnable> actionlist, final JavaPlugin plugin, final TriggerEventListener listener)
	{
		super(config, actionlist, plugin);
		this.listener = listener;
		this.events = new ArrayList<Class<? extends Event>>();
		for (final String clazz : config.getStringList("events"))
			try
			{
				events.add((Class<Event>) Class.forName(clazz));
			}
			catch (final ClassNotFoundException e)
			{
				System.out.println("Class " + clazz + " not found!");
			}
			catch (final ClassCastException e)
			{
				System.out.println("Class " + clazz + " is not an event!");
			}
	}

	public EventTrigger(final String name, final List<NamedRunnable> actionlist, final JavaPlugin plugin, final List<Class<? extends Event>> events, final TriggerEventListener listener)
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
