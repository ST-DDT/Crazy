package de.st_ddt.crazyutil.trigger;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazyutil.NamedRunnable;

public class EventTrigger extends Trigger
{

	protected static TriggerEventListener listener;
	Set<Class<? extends Event>> events;

	public static TriggerEventListener getTriggerEventListener()
	{
		return listener;
	}

	public static void setTriggerEventListener(TriggerEventListener listener)
	{
		EventTrigger.listener = listener;
	}

	@SuppressWarnings("unchecked")
	public EventTrigger(final ConfigurationSection config, final Set<NamedRunnable> actionlist, final JavaPlugin plugin)
	{
		super(config, actionlist, plugin);
		this.events = new HashSet<Class<? extends Event>>();
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

	public EventTrigger(final String name, final Set<NamedRunnable> actionlist, final JavaPlugin plugin, final Set<Class<? extends Event>> events)
	{
		super(name, actionlist, plugin);
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

	public Set<Class<? extends Event>> getEventList()
	{
		return events;
	}
}
