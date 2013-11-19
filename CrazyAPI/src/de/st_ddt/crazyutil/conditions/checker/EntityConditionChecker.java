package de.st_ddt.crazyutil.conditions.checker;

import org.bukkit.entity.Entity;

import de.st_ddt.crazyutil.conditions.checker.ConditionChecker;

public interface EntityConditionChecker extends ConditionChecker
{

	public Entity getEntity();

	public class SimpleEntityConditionChecker implements EntityConditionChecker
	{

		private final Entity entity;

		public SimpleEntityConditionChecker(final Entity entity)
		{
			super();
			this.entity = entity;
		}

		@Override
		public Entity getEntity()
		{
			return entity;
		}
	}
}
