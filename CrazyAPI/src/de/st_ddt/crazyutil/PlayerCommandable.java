package de.st_ddt.crazyutil;

import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyException;

public interface PlayerCommandable
{

	public boolean command(Player player, String commandLabel, String[] args) throws CrazyException;
}
