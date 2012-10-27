package de.st_ddt.crazyplugin.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.exceptions.CrazyException;

public interface CrazyCommandExecutorInterface extends CommandExecutor
{

	public void command(CommandSender sender, String[] args) throws CrazyException;

	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args);
}
