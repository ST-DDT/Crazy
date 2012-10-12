package de.st_ddt.crazyutil;

import org.bukkit.command.CommandSender;

public interface Showable
{

	public void show(CommandSender target);

	public void show(CommandSender target, String chatHeader, boolean showDetailed);

	public String getShortInfo();
}
