package de.st_ddt.crazysquads.data;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.bukkit.entity.Player;

import de.st_ddt.crazysquads.CrazySquads;
import de.st_ddt.crazysquads.events.CrazySquadsSquadCreateEvent;
import de.st_ddt.crazyutil.Named;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.paramitrisable.OfflinePlayerParamitrisable;

public class Squad implements Named
{

	private Player owner;
	private Loot_Rules loot = Loot_Rules.LOOT_SHARESILENT;
	private XP_Rules xp = XP_Rules.XP_SHARESILENT;
	private final Set<Player> members = Collections.synchronizedSet(new LinkedHashSet<Player>());

	public Squad(final Player owner)
	{
		super();
		this.owner = owner;
		new CrazySquadsSquadCreateEvent(getPlugin(), this, members).callEvent();
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

	public Loot_Rules getLootRule()
	{
		return loot;
	}

	public void setLootRule(final Loot_Rules loot)
	{
		this.loot = loot;
	}

	public XP_Rules getXPRule()
	{
		return xp;
	}

	public void setXPRule(final XP_Rules xp)
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

	@Localized("CRAZYSQUADS.SQUAD.JOIN $Player$")
	public void join(final Player player)
	{
		getPlugin().sendLocaleMessage("SQUAD.JOIN", members, player.getName());
		members.add(player);
	}

	@Localized("CRAZYSQUADS.SQUAD.LEFT $Player$")
	public boolean leave(final Player player)
	{
		final boolean removed = members.remove(player);
		getPlugin().sendLocaleMessage("SQUAD.LEFT", members, player.getName());
		if (members.size() > 0)
			if (owner.equals(player))
			{
				owner = members.iterator().next();
				getPlugin().sendLocaleMessage("SQUAD.OWNERLEFT", members, owner.getName());
			}
		return removed;
	}

	@Localized("CRAZYSQUADS.SQUAD.KICKED $Owner$ $Player$")
	public boolean kick(final Player player)
	{
		final boolean removed = members.remove(player);
		getPlugin().sendLocaleMessage("SQUAD.KICKED", members, owner.getName(), player.getName());
		return removed;
	}

	protected final CrazySquads getPlugin()
	{
		return CrazySquads.getPlugin();
	}
}
