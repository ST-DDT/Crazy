package de.st_ddt.crazyutil;

import org.bukkit.entity.Player;

public interface ChatFormatParameters
{

	public String getParameterPrefix();

	public Object[] getParameters(Player player);
	
	public int getParameterCount();
}
