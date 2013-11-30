package de.st_ddt.crazyutil.conditions.checker;

import org.bukkit.entity.Creature;

public interface CreatureConditionChecker extends LivingEntityConditionChecker
{

	@Override
	public Creature getEntity();

	public class SimpleCreatureConditionChecker implements CreatureConditionChecker
	{

		private final Creature entity;

		public SimpleCreatureConditionChecker(final Creature entity)
		{
			super();
			this.entity = entity;
		}

		@Override
		public Creature getEntity()
		{
			return entity;
		}
	}
}
