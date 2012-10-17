package de.st_ddt.crazyannouncer;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyutil.NamedRunnable;
import de.st_ddt.crazyutil.action.Action;
import de.st_ddt.crazyutil.action.ActionList_ORDERED;
import de.st_ddt.crazyutil.action.Action_MESSAGE;
import de.st_ddt.crazyutil.trigger.EventTrigger;
import de.st_ddt.crazyutil.trigger.ScheduledRepeatedTrigger;
import de.st_ddt.crazyutil.trigger.Trigger;

public class CrazyAnnouncer extends CrazyPlugin
{

	protected static CrazyAnnouncer plugin;
	protected final Set<Trigger> triggers = new HashSet<Trigger>();
	protected final Set<NamedRunnable> actions = new HashSet<NamedRunnable>();
	private CrazyAnnouncerEventListener eventListener;

	public static CrazyAnnouncer getPlugin()
	{
		return plugin;
	}

	@Override
	public void onEnable()
	{
		plugin = this;
		registerHooks();
		super.onEnable();
	}

	public void registerHooks()
	{
		this.eventListener = new CrazyAnnouncerEventListener(this);
		final PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(eventListener, this);
	}

	@Override
	public void loadConfiguration()
	{
		// Cleanup
		// EDIT eventListener.clearEventRegistrations();
		actions.clear();
		triggers.clear();
		// Load
		EventTrigger.setTriggerEventListener(eventListener);
		ConfigurationSection config = getConfig().getConfigurationSection("actions");
		if (config != null)
		{
			for (final String name : config.getKeys(false))
				actions.add(Action.load(config.getConfigurationSection(name)));
		}
		else
		{
			actions.add(new Action_MESSAGE("example", "This is a default message", "Welcome to the minecraft server of &E" + getServer().getServerName()));
			final ActionList_ORDERED action = new ActionList_ORDERED("example123");
			actions.add(action);
			action.addAction(new Action_MESSAGE("a1", "Message1"));
			action.addAction(new Action_MESSAGE("a2", "Message2"));
			action.addAction(new Action_MESSAGE("a3", "Message3"));
		}
		config = getConfig().getConfigurationSection("triggers");
		if (config != null)
		{
			for (final String name : config.getKeys(false))
				triggers.add(Trigger.load(config.getConfigurationSection(name), actions, this));
			triggers.remove(null);
			for (final Trigger trigger : triggers)
				trigger.register();
		}
		else
		{
			final Set<Class<? extends Event>> events = new HashSet<Class<? extends Event>>();
			events.add(PlayerJoinEvent.class);
			triggers.add(new EventTrigger("exampleEvent", actions, plugin, events));
			triggers.add(new ScheduledRepeatedTrigger("exampleEvent2", actions, plugin, new Date(new Date().getTime() + 20000), 60000, -1));
		}
	}

	@Override
	public void saveConfiguration()
	{
		final ConfigurationSection config = getConfig();
		config.set("triggers", null);
		for (final Trigger trigger : triggers)
			trigger.save(config, "triggers." + trigger.getName() + ".");
		config.set("actions", null);
		for (final NamedRunnable action : actions)
			action.save(config, "actions." + action.getName() + ".");
		super.saveConfiguration();
	}

	public Set<Trigger> getTriggers()
	{
		return triggers;
	}

	public Set<NamedRunnable> getActions()
	{
		return actions;
	}

	public NamedRunnable getAction(final String actionname)
	{
		for (final NamedRunnable action : actions)
			if (action.getName().equals(actionname))
				return action;
		return null;
	}
}
