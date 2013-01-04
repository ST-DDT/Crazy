package de.st_ddt.crazysquads.data;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.bukkit.entity.Player;

import de.st_ddt.crazysquads.CrazySquads;
import de.st_ddt.crazyutil.Named;
import de.st_ddt.crazyutil.paramitrisable.OfflinePlayerParamitrisable;

public class Squad implements Named
{

	private Player owner;
	private ShareRules loot = CrazySquads.getPlugin().getDefaultLootRules();
	private ShareRules xp = CrazySquads.getPlugin().getDefaultXPRules();
	private final Set<Player> members = Collections.synchronizedSet(new LinkedHashSet<Player>());

	public Squad(final Player owner)
	{
		super();
		this.owner = owner;
	}

	@Override
	public String getName()
	{
		return owner.getName() + "'s Squad";
	}

	public Player getOwner()
	{
		return owner;
	}

	public void setOwner(final Player owner)
	{
		this.owner = owner;
	}

	public ShareRules getLootRule()
	{
		return loot;
	}

	public void setLootRule(final ShareRules loot)
	{
		this.loot = loot;
	}

	public ShareRules getXPRule()
	{
		return xp;
	}

	public void setXPRule(final ShareRules xp)
	{
		this.xp = xp;
	}

	public Set<Player> getMembers()
	{
		return members;
	}

	public String[] getMemberNames()
	{
		return OfflinePlayerParamitrisable.getPlayerNames(members);
	}
}
