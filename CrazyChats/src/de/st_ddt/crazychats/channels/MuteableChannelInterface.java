package de.st_ddt.crazychats.channels;

import java.util.Set;

import org.bukkit.entity.Player;

public interface MuteableChannelInterface extends ChannelInterface
{

	public abstract Set<Player> getDeafTargets();

	public abstract void muteChannel(Player player);

	public abstract void unmuteChannel(Player player);
}
