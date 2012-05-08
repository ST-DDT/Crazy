package de.st_ddt.crazyarena.spleef;

public class ArenaSpleefTimeOut implements Runnable
{

	protected final ArenaSpleef arena;
	protected boolean cancelled = false;

	public ArenaSpleefTimeOut(final ArenaSpleef arena)
	{
		this.arena = arena;
	}

	public boolean isCancelled()
	{
		return cancelled;
	}

	public void cancel()
	{
		cancelled = true;
	}

	@Override
	public void run()
	{
		if (cancelled)
			return;
		arena.getParticipants().sendLocaleMessage("TIMEOUT");
		arena.stop(null, true);
	}
}
