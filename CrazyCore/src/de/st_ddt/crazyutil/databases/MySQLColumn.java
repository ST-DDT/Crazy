package de.st_ddt.crazyutil.databases;

@Deprecated
public class MySQLColumn extends SQLColumn
{

	public MySQLColumn(String name, String type, boolean primary, boolean autoincrement)
	{
		super(name, type, primary, autoincrement);
	}

	public MySQLColumn(String name, String type, String defaults, boolean nulled, boolean autoincrement)
	{
		super(name, type, defaults, nulled, autoincrement);
	}

	public MySQLColumn(String name, String type)
	{
		super(name, type);
	}
}
