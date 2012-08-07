package de.st_ddt.crazyonline.tasks;

import de.st_ddt.crazyonline.CrazyOnline;

public class DropInactiveAccountsTask implements Runnable
{

	private final CrazyOnline plugin;

	public DropInactiveAccountsTask(final CrazyOnline plugin)
	{
		super();
		this.plugin = plugin;
	}

	@Override
	public void run()
	{
		final int amount = plugin.dropInactiveAccounts();
		if (amount > 0)
		{
			final int autoDelete = plugin.getAutoDelete();
			plugin.broadcastLocaleMessage("ACCOUNTS.DELETED", "DropTask", autoDelete, amount);
		}
	}
}
