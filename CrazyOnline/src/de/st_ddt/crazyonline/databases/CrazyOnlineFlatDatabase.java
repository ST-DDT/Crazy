package de.st_ddt.crazyonline.databases;

import java.io.File;

import de.st_ddt.crazyonline.OnlinePlayerData;
import de.st_ddt.crazyutil.databases.FlatDatabase;

public class CrazyOnlineFlatDatabase extends FlatDatabase<OnlinePlayerData>
{

	public CrazyOnlineFlatDatabase(final File file, final String colName, final String colFirstLogin, final String colLastLogin, final String colLastLogout, final String colOnlineTime)
	{
		super(OnlinePlayerData.class, file, new String[] { colName, colFirstLogin, colLastLogin, colLastLogout, colOnlineTime });
	}
}
