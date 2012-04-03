package de.st_ddt.crazyannouncer;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyutil.action.Action;
import de.st_ddt.crazyutil.action.Action_MESSAGE;
import de.st_ddt.crazyutil.action.NamedRunnable;
import de.st_ddt.crazyutil.trigger.EventTrigger;
import de.st_ddt.crazyutil.trigger.Trigger;

public class CrazyAnnouncer extends CrazyPlugin
{

	protected static CrazyAnnouncer plugin;
	protected final List<Trigger> triggers = new ArrayList<Trigger>();
	protected final List<NamedRunnable> actions = new ArrayList<NamedRunnable>();
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
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(eventListener, this);
	}

	@Override
	public void load()
	{
		super.load();
		ConfigurationSection config = getConfig().getConfigurationSection("actions");
		if (config != null)
		{
			for (String name : config.getKeys(false))
				actions.add(Action.load(config.getConfigurationSection(name)));
		}
		else
		{
			actions.add(new Action_MESSAGE("example", "This is a default message", "Welcome to the minecraft server of" + getServer().getServerName()));
		}
		config = getConfig().getConfigurationSection("triggers");
		if (config != null)
		{
			for (String name : config.getKeys(false))
				triggers.add(Trigger.load(config.getConfigurationSection(name), actions, this));
		}
		{
			List<Class<? extends Event>> events=new ArrayList<Class<? extends Event>>();
			events.add(PlayerJoinEvent.class);
			triggers.add(new EventTrigger("exampleEvent", actions, plugin, events, eventListener));
		}
	}

	@Override
	public void save()
	{
		// EDIT Auto-generated method stub
		super.save();
	}
}
