package de.st_ddt.crazysquads.listener;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;
import org.kitteh.tag.TagAPI;

import de.st_ddt.crazysquads.CrazySquads;
import de.st_ddt.crazysquads.data.Squad;
import de.st_ddt.crazysquads.events.CrazySquadsSquadJoinEvent;
import de.st_ddt.crazysquads.events.CrazySquadsSquadLeaveEvent;

public class CrazySquadsTagAPIListener implements Listener
{

	private final CrazySquads plugin;

	public CrazySquadsTagAPIListener(final CrazySquads plugin)
	{
		super();
		this.plugin = plugin;
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
		{

			@Override
			public void run()
			{
				for (final Player player : Bukkit.getOnlinePlayers())
					TagAPI.refreshPlayer(player);
			}
		}, 5);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void PlayerNameTag(final PlayerReceiveNameTagEvent event)
	{
		final Squad squad = plugin.getSquads().get(event.getPlayer());
		if (squad != null)
			if (squad.getMembers().contains(event.getNamedPlayer()))
				event.setTag(plugin.getSquadHeadNamePrefix() + event.getNamedPlayer().getName());
	}

	@EventHandler
	public void SquadJoin(final CrazySquadsSquadJoinEvent event)
	{
		final Player player = event.getNewMember();
		final Set<Player> members = event.getSquad().getMembers();
		final Player[] list = members.toArray(new Player[members.size()]);
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
		{

			@Override
			public void run()
			{
				if (player.isOnline())
					for (final Player member : list)
					{
						TagAPI.refreshPlayer(member, player);
						TagAPI.refreshPlayer(player, member);
					}
			}
		}, 5);
	}

	@EventHandler
	public void SquadLeave(final CrazySquadsSquadLeaveEvent event)
	{
		final Player player = event.getLeftMember();
		final Set<Player> members = event.getSquad().getMembers();
		final Player[] list = members.toArray(new Player[members.size()]);
		if (player.isOnline())
			for (final Player member : list)
			{
				TagAPI.refreshPlayer(member, player);
				TagAPI.refreshPlayer(player, member);
			}
	}
}
