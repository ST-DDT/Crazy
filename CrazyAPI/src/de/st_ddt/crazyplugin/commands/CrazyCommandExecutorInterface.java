package de.st_ddt.crazyplugin.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import de.st_ddt.crazyplugin.exceptions.CrazyException;

public interface CrazyCommandExecutorInterface extends TabExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args);

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args);

	public void command(CommandSender sender, String[] args) throws CrazyException;

	public List<String> tab(CommandSender sender, String[] args);

	public boolean hasAccessPermission(CommandSender sender);
}
