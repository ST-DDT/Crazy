package de.st_ddt.crazyarena.arenas.pve.rounds;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;

import de.st_ddt.crazyarena.arenas.pve.PvEArena;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;

public abstract class AbstractRound implements Round
{

	public static Round load(final PvEArena arena, final ConfigurationSection config)
	{
		return ObjectSaveLoadHelper.load(config, Round.class, new Class<?>[] { PvEArena.class, ConfigurationSection.class }, new Object[] { arena, config });
	}

	protected final Set<LivingEntity> entities = new HashSet<LivingEntity>();
	protected final PvEArena arena;
	protected int priority;
	protected int offset;
	protected int interval;
	protected boolean repeat;
	protected boolean clearAfter;

	public AbstractRound(final PvEArena arena, final int priority, final int offset, final int interval, final boolean repeat, final boolean clearAfter)
	{
		super();
		this.arena = arena;
		this.priority = priority;
		this.offset = offset;
		this.interval = interval;
		this.repeat = repeat;
		this.clearAfter = clearAfter;
	}

	public AbstractRound(final PvEArena arena, final ConfigurationSection config)
	{
		super();
		this.arena = arena;
		this.priority = config.getInt("priority", 1);
		this.offset = config.getInt("offset", 0);
		this.interval = config.getInt("interval", 1);
		this.repeat = config.getBoolean("repeat", true);
		this.clearAfter = config.getBoolean("clearAfter", false);
	}

	@Override
	public boolean isApplyable(final int roundNumber)
	{
		if (repeat)
			return (roundNumber - offset) % interval == 0;
		else
			return (roundNumber - offset) == 0;
	}

	@Override
	public final int getPriority()
	{
		return priority;
	}

	@Override
	public void next()
	{
		if (clearAfter)
			for (final LivingEntity entity : entities)
				entity.remove();
	}

	@Override
	public void reset()
	{
		for (final LivingEntity entity : entities)
			entity.remove();
	}

	@Override
	public void creatureDeath(final LivingEntity entity)
	{
		entities.remove(entity);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "type", getClass().getName());
		config.set(path + "priority", priority);
		config.set(path + "offset", offset);
		config.set(path + "interval", interval);
		config.set(path + "repeat", repeat);
		config.set(path + "clearAfter", clearAfter);
	}
}
