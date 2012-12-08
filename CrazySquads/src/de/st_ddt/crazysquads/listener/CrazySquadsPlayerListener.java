package de.st_ddt.crazysquads.listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import de.st_ddt.crazysquads.CrazySquads;
import de.st_ddt.crazysquads.data.Squad;
import de.st_ddt.crazysquads.events.CrazySquadsSquadDeleteEvent;
import de.st_ddt.crazysquads.events.CrazySquadsSquadLeaveEvent;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazySquadsPlayerListener implements Listener
{

	protected final CrazySquads plugin;

	public CrazySquadsPlayerListener(final CrazySquads plugin)
	{
		super();
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void PlayerQuit(final PlayerQuitEvent event)
	{
		PlayerQuit(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void PlayerKick(final PlayerKickEvent event)
	{
		PlayerQuit(event.getPlayer());
	}

	public void PlayerQuit(final Player player)
	{
		plugin.getInvites().remove(player);
		final Squad squad = plugin.getSquads().remove(player);
		if (squad != null)
		{
			final Set<Player> members = squad.getMembers();
			members.remove(player);
			if (squad.getOwner() == player)
			{
				if (members.size() > 0)
				{
					final Player newOwner = members.iterator().next();
					squad.setOwner(newOwner);
					plugin.sendLocaleMessage("SQUAD.OWNERLEFT", members, player.getName(), newOwner.getName());
				}
			}
			else
				plugin.sendLocaleMessage("SQUAD.LEFT", members, player.getName());
			new CrazySquadsSquadLeaveEvent(plugin, squad, player).callEvent();
			if (members.size() == 0)
				new CrazySquadsSquadDeleteEvent(plugin, squad).callEvent();
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void PlayerDamage(final EntityDamageByEntityEvent event)
	{
		if (!(event.getDamager() instanceof Player))
			return;
		if (!(event.getEntity() instanceof Player))
			return;
		final Squad squad = plugin.getSquads().get(event.getEntity());
		if (squad != null)
			if (squad.getMembers().contains(event.getDamager()))
				event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	@Localized("CRAZYSQUADS.SQUAD.LOOTSHARE $Player$ $Amount$ $Item$")
	public void PlayerPickupItem(final PlayerPickupItemEvent event)
	{
		final Player player = event.getPlayer();
		final Squad squad = plugin.getSquads().get(player);
		if (squad == null)
			return;
		if (squad.getLootRule().isShareEnabled())
		{
			final Set<Player> members = squad.getMembers();
			final List<Player> activeMembers = new ArrayList<Player>(members.size());
			synchronized (members)
			{
				final Location location = player.getLocation();
				final double range = plugin.getMaxShareRange();
				for (final Player member : members)
					if (location.distance(member.getLocation()) < range)
						activeMembers.add(member);
			}
			Collections.shuffle(activeMembers);
			for (final ItemStack item : shareItems(activeMembers, members, event.getItem().getItemStack(), squad.getLootRule().isSilent()))
				event.getItem().getWorld().dropItem(event.getItem().getLocation(), item);
			event.getItem().remove();
			event.setCancelled(true);
		}
		else if (!squad.getLootRule().isSilent())
		{
			final ItemStack item = event.getItem().getItemStack();
			plugin.sendLocaleMessage("SQUAD.LOOTSHARE", squad.getMembers(), player.getName(), item.getAmount(), item.getType().toString());
		}
	}

	@Localized("CRAZYSQUADS.SQUAD.LOOTSHARE $Player$ $Amount$ $Item$")
	protected Set<ItemStack> shareItems(final List<Player> targets, final Set<Player> members, final ItemStack item, final boolean silent)
	{
		final Set<ItemStack> items = new HashSet<ItemStack>();
		final int amount = item.getAmount();
		final int shared = amount / targets.size();
		int overhead = amount % targets.size();
		for (final Player player : targets)
		{
			final int preCount;
			if (overhead-- > 0)
				preCount = shared + 1;
			else
				preCount = shared;
			if (preCount == 0)
				return items;
			final ItemStack gift = item.clone();
			gift.setAmount(preCount);
			final Collection<ItemStack> rest = player.getInventory().addItem(gift).values();
			items.addAll(rest);
			if (!silent)
				if (rest.size() == 0)
					plugin.sendLocaleMessage("SQUAD.LOOTSHARE", members, player.getName(), preCount, item.getType().toString());
				else
				{
					final ItemStack delta = rest.iterator().next();
					if (preCount - delta.getAmount() > 0)
						plugin.sendLocaleMessage("SQUAD.LOOTSHARE", members, player.getName(), preCount - delta.getAmount(), item.getType().toString());
				}
		}
		return items;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	@Localized("CRAZYSQUADS.SQUAD.XPSHARE $Player$ $XP$")
	public void PlayerXP(final PlayerExpChangeEvent event)
	{
		final Player player = event.getPlayer();
		final Squad squad = plugin.getSquads().get(player);
		if (squad == null)
			return;
		if (squad.getXPRule().isShareEnabled())
		{
			final Set<Player> members = squad.getMembers();
			final List<Player> activeMembers = new ArrayList<Player>(members.size());
			synchronized (members)
			{
				final Location location = player.getLocation();
				final double range = plugin.getMaxShareRange();
				for (final Player member : members)
					if (location.distance(member.getLocation()) < range)
						activeMembers.add(member);
			}
			Collections.shuffle(activeMembers);
			final int amount = event.getAmount();
			final int shared = amount / activeMembers.size();
			int overhead = amount % activeMembers.size();
			for (final Player target : activeMembers)
				if (overhead-- > 0)
				{
					target.giveExp(shared + 1);
					if (!squad.getXPRule().isSilent())
						plugin.sendLocaleMessage("SQUAD.XPSHARE", members, target.getName(), shared + 1);
				}
				else if (shared > 0)
				{
					target.giveExp(shared);
					if (!squad.getXPRule().isSilent())
						plugin.sendLocaleMessage("SQUAD.XPSHARE", members, target.getName(), shared);
				}
		}
		else
			plugin.sendLocaleMessage("SQUAD.XPSHARE", squad.getMembers(), player.getName(), event.getAmount());
	}
}
