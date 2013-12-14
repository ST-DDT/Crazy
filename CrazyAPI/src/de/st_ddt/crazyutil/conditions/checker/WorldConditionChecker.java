package de.st_ddt.crazyutil.conditions.checker;

import org.bukkit.World;

public interface WorldConditionChecker extends ConditionChecker
{

	public World getWorld();

	public class SimpleWorldConditionChecker implements WorldConditionChecker
	{

		private final World world;

		public SimpleWorldConditionChecker(final World world)
		{
			super();
			this.world = world;
		}

		@Override
		public World getWorld()
		{
			return world;
		}
	}
}
