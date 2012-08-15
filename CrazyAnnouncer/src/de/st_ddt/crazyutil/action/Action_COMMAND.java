package de.st_ddt.crazyutil.action;

import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyutil.ChatHelper;

public class Action_COMMAND extends Action
{

	List<String> commands;

	public Action_COMMAND(final ConfigurationSection config)
	{
		super(config);
		commands = config.getStringList("commands");
	}

	@Override
	public void run()
	{
		run(Bukkit.getConsoleSender());
	}

	public void run(final CommandSender sender)
	{
		for (final String command : commands)
			Bukkit.getServer().dispatchCommand(sender, ChatHelper.putArgs(command, sender.getName(), CrazyPluginInterface.DateFormat.format(new Date())));
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		config.set(path + "commands", commands);
	}
}
