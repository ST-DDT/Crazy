package de.st_ddt.crazyutil.action;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.ChatHelper;

public class Action_MESSAGE extends Action
{

	private final List<String> messages;

	public Action_MESSAGE(final ConfigurationSection config)
	{
		super(config);
		messages = config.getStringList("messages");
	}

	public Action_MESSAGE(final String name, final String... messages)
	{
		super(name);
		this.messages = new ArrayList<String>();
		for (final String message : messages)
			this.messages.add(message);
	}

	@Override
	public void run()
	{
		for (final String message : messages)
			Bukkit.broadcastMessage(ChatHelper.colorise(message));
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		config.set(path + "messages", messages);
	}
}
