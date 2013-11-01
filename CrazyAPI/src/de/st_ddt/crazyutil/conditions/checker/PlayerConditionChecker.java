package de.st_ddt.crazyutil.conditions.checker;

import org.bukkit.entity.Player;

public interface PlayerConditionChecker extends ConditionChecker
{

	public Player getPlayer();

	public class SimplePlayerConditionChecker implements PlayerConditionChecker
	{

		private final Player player;

		public SimplePlayerConditionChecker(final Player player)
		{
			super();
			this.player = player;
		}

		@Override
		public Player getPlayer()
		{
			return player;
		}
	}
}
