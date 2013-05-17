package de.st_ddt.crazyplugin.commands;

import java.util.List;

import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyException;

public interface CrazyPlayerCommandExecutorInterface extends CrazyCommandExecutorInterface
{

	public void command(Player player, String[] args) throws CrazyException;

	public List<String> tab(Player player, String[] args);

	public boolean hasAccessPermission(Player player);

	public boolean isAccessible(Player player);
}
