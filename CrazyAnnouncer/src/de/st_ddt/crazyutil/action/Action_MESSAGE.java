package de.st_ddt.crazyutil.action;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.ChatHelper;

public class Action_MESSAGE extends Action
{

	private List<String> messages;

	public Action_MESSAGE(ConfigurationSection config)
	{
		super(config);
		messages = config.getStringList("messages");
	}

	@Override
	public void run()
	{
		for (String message : messages)
			Bukkit.broadcastMessage(ChatHelper.colorise(message));
	}
}
