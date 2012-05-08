package de.st_ddt.crazyarena.spleef;

public class ArenaSpleefGoTimer implements Runnable
{

	protected final ArenaSpleef arena;

	public ArenaSpleefGoTimer(ArenaSpleef arena)
	{
		this.arena = arena;
	}

	@Override
	public void run()
	{
		arena.getParticipants().sendLocaleMessage("START");
		arena.delayedStart();
	}
}
