package de.st_ddt.crazyplugin.data;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyutil.Showable;

public interface ParameterData extends Showable
{

	public String getParameter(CommandSender sender, int index);

	public int getParameterCount();
}
