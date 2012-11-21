package de.st_ddt.crazychats.channels;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class PrivateChannel extends AbstractChannel implements GroupChannelInterface
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
	public String getFormat()
	{
		return plugin.getPrivateChatFormat();
	}

	@Override
	public boolean canBeDeleted()
	{
		return targets.size() == 0;
	}

	@Override
	public void join(final Player player) throws CrazyException
	{
		throw new CrazyCommandPermissionException();
	}

	@Override
	public void leave(final Player player) throws CrazyException
	{
		throw new CrazyCommandPermissionException();
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
}
