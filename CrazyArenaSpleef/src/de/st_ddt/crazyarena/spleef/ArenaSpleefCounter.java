package de.st_ddt.crazyarena.spleef;

public class ArenaSpleefCounter implements Runnable
{

	protected final ArenaSpleef arena;
	protected final String localePath;
	protected final int delay;

	public ArenaSpleefCounter(ArenaSpleef arena, String localePath, int delay)
	{
		this.arena = arena;
		this.localePath = localePath;
		this.delay = delay;
	}

	@Override
	public void run()
	{
		arena.getParticipants().sendLocaleMessage(localePath, delay);
	}
}
