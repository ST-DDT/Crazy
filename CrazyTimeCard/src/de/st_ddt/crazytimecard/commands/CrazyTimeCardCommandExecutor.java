package de.st_ddt.crazytimecard.commands;

import de.st_ddt.crazyplugin.commands.CrazyCommandExecutor;
import de.st_ddt.crazytimecard.CrazyTimeCard;

public abstract class CrazyTimeCardCommandExecutor extends CrazyCommandExecutor<CrazyTimeCard>
{

	public CrazyTimeCardCommandExecutor(final CrazyTimeCard plugin)
	{
		super(plugin);
	}
}
