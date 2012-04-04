package de.st_ddt.crazypunisher.events;

import de.st_ddt.crazyplugin.events.CrazyEvent;
import de.st_ddt.crazypunisher.CrazyPunisher;

public abstract class CrazyPunisherEvent extends CrazyEvent<CrazyPunisher>
{

	public CrazyPunisherEvent()
	{
		super(CrazyPunisher.getPlugin());
	}
}
