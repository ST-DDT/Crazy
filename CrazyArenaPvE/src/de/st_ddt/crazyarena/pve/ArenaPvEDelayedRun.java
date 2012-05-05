package de.st_ddt.crazyarena.pve;

public class ArenaPvEDelayedRun extends ArenaPvEDelayed
{

	public ArenaPvEDelayedRun(ArenaPvE arena)
	{
		super(arena);
	}

	@Override
	public void run()
	{
		if (arena != null)
			arena.delayedStartRound();
	}
}
