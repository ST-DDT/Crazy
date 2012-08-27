package de.st_ddt.crazyarena.arenas;

public enum ArenaStatus
{
	INITIALIZING(false, false, false), CONSTRUCTING(false, false, false), READY(false, true, false), SELECTING(true, true, true), WAITING(true, false, true), PLAYING(true, false, true), PAUSED(true, false, true), FINISHED(false, false, false), SHUTDOWN(false, false, false), DISABLED(false, false, false), ERROR(false, false, false);

	private final boolean spectator;
	private final boolean join;
	private final boolean active;

	private ArenaStatus(boolean spectator, boolean join, boolean active)
	{
		this.spectator = spectator;
		this.join = join;
		this.active = active;
	}

	public boolean allowSpectators()
	{
		return spectator;
	}

	public boolean allowJoins()
	{
		return join;
	}

	public boolean isActive()
	{
		return active;
	}
}
