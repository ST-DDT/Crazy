package de.st_ddt.crazyplugin.data;

import org.bukkit.command.CommandSender;

public interface Showable
{

	public void show(CommandSender target);

	public void show(CommandSender target, String... args);

	public String getShortInfo(String... args);
}
