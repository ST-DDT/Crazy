package de.st_ddt.crazyarena.spleef;

import org.bukkit.entity.Player;

public class ArenaSpleefKicker implements Runnable
{

	protected final ArenaSpleef arena;
	protected final Player player;
	private boolean cancelled = false;

	public ArenaSpleefKicker(ArenaSpleef arena, Player player)
	{
		super();
		this.arena = arena;
		this.player = player;
	}

	public void cancel()
	{
		cancelled = true;
	}

	@Override
	public void run()
	{
		if (cancelled)
			return;
		arena.PlayerDeath(player);
	}
}
