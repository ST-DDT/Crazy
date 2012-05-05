package de.st_ddt.crazyarena.pve;

public abstract class ArenaPvEDelayed implements Runnable
{

	protected final ArenaPvE arena;

	public ArenaPvEDelayed(ArenaPvE arena)
	{
		super();
		this.arena = arena;
	}
}
