package de.st_ddt.crazychats.channels;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

public class PrivateChannel extends AbstractChannel implements ControlledChannelInterface
{

	private final Set<Player> targets = Collections.synchronizedSet(new HashSet<Player>());
	private final Player owner;

	public PrivateChannel(final Player owner)
	{
		super("Private_" + owner.getName());
		aliases.add("t");
		aliases.add("talk");
		aliases.add("p");
		aliases.add("private");
		this.owner = owner;
	}

	@Override
	public boolean hasTalkPermission(final Player player)
	{
		return true;
	}

	@Override
	public Set<Player> getTargets(final Player player)
	{
		return targets;
	}

	@Override
	public String getFormat(final Player player)
	{
		return plugin.getPrivateChatFormat();
	}

	@Override
	public boolean kick(final Player player)
	{
		return targets.remove(targets);
	}

	public Player getOwner()
	{
		return owner;
	}

	@Override
	public String toString()
	{
		return "PrivateChatChannel" + (owner == null ? "" : " (Owner: " + owner.getName() + ")");
	}

	@Override
	public boolean isAffectedByServerSilence()
	{
		return false;
	}
}
