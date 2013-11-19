package de.st_ddt.crazyutil.conditions.checker;

import org.bukkit.entity.Player;

public interface PlayerConditionChecker extends LivingEntityConditionChecker
{

	@Override
	public Player getEntity();

	public class SimplePlayerConditionChecker implements PlayerConditionChecker
	{

		private final Player player;

		public SimplePlayerConditionChecker(final Player player)
		{
			super();
			this.player = player;
		}

		@Override
		public Player getEntity()
		{
			return player;
		}
	}
}
