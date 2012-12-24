package de.st_ddt.crazyutil.databases;

@Deprecated
public class MySQLColumn extends SQLColumn
{

	public MySQLColumn(final String name, final String type, final boolean primary, final boolean autoincrement)
	{
		super(name, type, primary, autoincrement);
	}

	public MySQLColumn(final String name, final String type, final String defaults, final boolean nulled, final boolean autoincrement)
	{
		super(name, type, defaults, nulled, autoincrement);
	}

	public MySQLColumn(final String name, final String type)
	{
		super(name, type);
	}
}
