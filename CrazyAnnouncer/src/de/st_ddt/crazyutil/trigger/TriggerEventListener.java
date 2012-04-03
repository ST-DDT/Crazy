package de.st_ddt.crazyutil.trigger;

import org.bukkit.event.Listener;

public interface TriggerEventListener extends Listener
{

	public void removeTrigger(EventTrigger eventTrigger);

	public void addTrigger(EventTrigger eventTrigger);
}
