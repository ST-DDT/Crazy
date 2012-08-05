package de.st_ddt.crazyutil;

import de.st_ddt.crazyplugin.data.Showable;

public class ShowableDataGetter implements EntryDataGetter<Showable>
{

	@Override
	public String getEntryData(final Showable entry)
	{
		return entry.getShortInfo((String) null);
	}
}
