package de.st_ddt.crazyutil;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.exceptions.CrazyException;

public interface Commandable extends PlayerCommandable
{

	public boolean command(CommandSender sender, String commandLabel, String[] args) throws CrazyException;
}
