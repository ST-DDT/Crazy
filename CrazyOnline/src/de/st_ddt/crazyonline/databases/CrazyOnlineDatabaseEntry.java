package de.st_ddt.crazyonline.databases;

import de.st_ddt.crazyonline.OnlinePlayerData;
import de.st_ddt.crazyutil.databases.DatabaseEntry;

public abstract class CrazyOnlineDatabaseEntry<T> extends DatabaseEntry<OnlinePlayerData, T>
{

	public CrazyOnlineDatabaseEntry(OnlinePlayerData infoObject)
	{
		super(infoObject);
	}

	public CrazyOnlineDatabaseEntry(T rawData)
	{
		super(rawData);
	}

}
