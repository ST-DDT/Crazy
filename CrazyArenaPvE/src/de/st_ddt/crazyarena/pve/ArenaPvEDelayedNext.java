package de.st_ddt.crazyarena.pve;

public class ArenaPvEDelayedNext extends ArenaPvEDelayed
{

	private boolean canceled;

	public ArenaPvEDelayedNext(ArenaPvE arena)
	{
		super(arena);
	}

	@Override
	public void run()
	{
		if (arena != null && !canceled)
			arena.startRound();
	}

	public void cancel()
	{
		this.canceled = true;
	}
}
