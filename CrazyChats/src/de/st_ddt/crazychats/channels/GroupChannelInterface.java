package de.st_ddt.crazychats.channels;

import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyException;

public interface GroupChannelInterface extends ChannelInterface
{

	public void join(Player player) throws CrazyException;

	public void leave(Player player) throws CrazyException;

	public boolean kick(Player player);

	public boolean canBeDeleted();
}
