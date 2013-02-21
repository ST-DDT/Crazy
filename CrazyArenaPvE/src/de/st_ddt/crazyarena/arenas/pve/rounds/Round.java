package de.st_ddt.crazyarena.arenas.pve.rounds;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyarena.arenas.pve.PvEArena;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;

public abstract class Round
{

	public static Round load(final PvEArena arena, final ConfigurationSection config)
	{
		return ObjectSaveLoadHelper.load(config, Round.class, new Class<?>[] { PvEArena.class, ConfigurationSection.class }, new Object[] { arena, config });
	}

	protected final PvEArena arena;
	protected int priority;
	protected boolean repeat;
	protected int offset;
	protected int interval;

	public Round(final PvEArena arena, final int offset, final int interval, final boolean repeat)
	{
		super();
		this.arena = arena;
		this.offset = offset;
		this.interval = interval;
		this.repeat = repeat;
		this.priority = 1;
	}

	public Round(final PvEArena arena, final int offset, final int interval, final boolean repeat, final int priority)
	{
		this(arena, offset, interval, repeat);
		this.priority = priority;
	}

	public Round(final PvEArena arena, final ConfigurationSection config)
	{
		super();
		this.arena = arena;
		this.offset = config.getInt("offset", 0);
		this.interval = config.getInt("interval", 1);
		this.repeat = config.getBoolean("repeat", true);
		this.priority = config.getInt("priority", 1);
	}

	public boolean isApplyable(final int roundNumber)
	{
		if (repeat)
			return (roundNumber - offset) % interval == 0;
		else
			return (roundNumber - offset) == 0;
	}

	public int getPriority()
	{
		return priority;
	}

	public abstract void activate(int round);

	public abstract int getMonsterCount(int round);

	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "type", getClass().getName());
		config.set(path + "offset", offset);
		config.set(path + "interval", interval);
		config.set(path + "repeat", repeat);
		config.set(path + "priority", priority);
	}
}
