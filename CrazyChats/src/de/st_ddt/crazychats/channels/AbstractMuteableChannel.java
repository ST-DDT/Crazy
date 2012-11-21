package de.st_ddt.crazychats.channels;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

abstract class AbstractMuteableChannel extends AbstractChannel implements MuteableChannelInterface
{

	protected final Set<Player> deafPlayers = Collections.synchronizedSet(new HashSet<Player>());

	public AbstractMuteableChannel(final String name)
	{
		super(name);
	}

	@Override
	public Set<Player> getDeafTargets()
	{
		return deafPlayers;
	}

	@Override
	public void muteChannel(final Player player)
	{
		deafPlayers.add(player);
	}

	@Override
	public void unmuteChannel(final Player player)
	{
		deafPlayers.remove(player);
	}
}
