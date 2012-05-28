package de.st_ddt.crazyutil;

public class ToStringDataGetter implements EntryDataGetter<Object>
{

	@Override
	public String getEntryData(Object entry)
	{
		return entry.toString();
	}
}
