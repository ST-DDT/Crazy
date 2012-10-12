package de.st_ddt.crazyonline.tasks;

import de.st_ddt.crazyonline.CrazyOnline;
import de.st_ddt.crazyutil.locales.Localized;

public class DropInactiveAccountsTask implements Runnable
{

	private final CrazyOnline plugin;

	public DropInactiveAccountsTask(final CrazyOnline plugin)
	{
		super();
		this.plugin = plugin;
	}

	@Override
	@Localized("CRAZYONLINE.COMMAND.DROPOLDDATA.DELETED $DropCauser$ $KeptDays$ $DroppedAmount$")
	public void run()
	{
		final int amount = plugin.dropInactiveAccounts();
		if (amount > 0)
			plugin.broadcastLocaleMessage(true, "crazyonline.warndelete", "COMMAND.DROPOLDDATA.DELETED", "DropTask", plugin.getAutoDelete(), amount);
	}
}
