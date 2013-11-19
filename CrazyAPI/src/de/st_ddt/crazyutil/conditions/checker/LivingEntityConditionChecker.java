package de.st_ddt.crazyutil.conditions.checker;

import org.bukkit.entity.LivingEntity;

public interface LivingEntityConditionChecker extends EntityConditionChecker
{

	@Override
	public LivingEntity getEntity();

	public class SimpleLivingEntityConditionChecker implements LivingEntityConditionChecker
	{

		private final LivingEntity entity;

		public SimpleLivingEntityConditionChecker(final LivingEntity entity)
		{
			super();
			this.entity = entity;
		}

		@Override
		public LivingEntity getEntity()
		{
			return entity;
		}
	}
}
