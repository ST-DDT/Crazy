package de.st_ddt.crazyarena.arenas;

public enum ArenaStatus
{
	// Loading
	INITIALIZING(),
	// Edit/Setup Arenas
	CONSTRUCTING(),
	// Enabled Status START
	// Waiting for first player to join
	READY(false, true, false),
	// Players select their equip/team (waiting for players become ready)
	SELECTING(true, true, true),
	// Waiting for arena to start (show rules/teams)
	WAITING(true, false, true),
	// Arena in progress
	PLAYING(true, false, true),
	// Arena currently paused
	PAUSED(true, false, true),
	// Arena finished, give rewards, restore inventories, restore terrain
	FINISHED(false, false, false),
	// Enabled Status END
	// Disabled
	DISABLED(),
	// Arena is going to be deleted/server shuting down
	SHUTDOWN(),
	// Arena is in Error Mode and cannot continue operating
	ERROR();

	private final boolean enabled;
	private final boolean active;
	private final boolean spectator;
	private final boolean join;

	private ArenaStatus()
	{
		this.enabled = false;
		this.spectator = false;
		this.join = false;
		this.active = false;
	}

	private ArenaStatus(boolean spectator, boolean join, boolean active)
	{
		this.enabled = true;
		this.spectator = spectator;
		this.join = join;
		this.active = active;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public boolean isActive()
	{
		return active;
	}

	public boolean allowJoins()
	{
		return join;
	}

	public boolean allowSpectators()
	{
		return spectator;
	}
}
