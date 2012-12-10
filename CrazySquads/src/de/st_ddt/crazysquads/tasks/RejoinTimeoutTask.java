package de.st_ddt.crazysquads.tasks;

import java.util.Map;

public class RejoinTimeoutTask implements Runnable
{

	private final Map<String, ?> rejoins;
	private final String name;

	public RejoinTimeoutTask(final Map<String, ?> rejoins, final String name)
	{
		super();
		this.rejoins = rejoins;
		this.name = name;
	}

	@Override
	public void run()
	{
		rejoins.remove(name);
	}
}
