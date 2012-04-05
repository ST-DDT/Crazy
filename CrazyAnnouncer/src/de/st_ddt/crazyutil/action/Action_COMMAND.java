package de.st_ddt.crazyutil.action;

import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyutil.ChatHelper;

public class Action_COMMAND extends Action
{

	List<String> commands;

	public Action_COMMAND(ConfigurationSection config)
	{
		super(config);
		commands = config.getStringList("commands");
	}

	@Override
	public void run()
	{
		run(Bukkit.getConsoleSender());
	}

	public void run(CommandSender sender)
	{
		for (String command : commands)
			Bukkit.getServer().dispatchCommand(sender, ChatHelper.putArgs(command, sender.getName(), CrazyCore.DateFormat.format(new Date())));
	}

	@Override
	public void save(ConfigurationSection config, String path)
	{
		super.save(config, path);
		config.set(path + "commands", commands);
	}
}
