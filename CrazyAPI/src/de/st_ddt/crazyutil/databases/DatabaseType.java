package de.st_ddt.crazyutil.databases;

public enum DatabaseType
{
	CONFIG(true), MYSQL(false), FLAT(true);

	private final boolean staticDatabase;

	private DatabaseType(final boolean staticDatabase)
	{
		this.staticDatabase = staticDatabase;
	}

	public boolean isStaticDatabase()
	{
		return staticDatabase;
	}
}
