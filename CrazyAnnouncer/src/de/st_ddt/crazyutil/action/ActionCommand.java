package de.st_ddt.crazyutil.action;

import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyutil.ChatHelper;

public class ActionCommand extends Action
{

	List<String> commands;

	public ActionCommand(ConfigurationSection config)
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
}
