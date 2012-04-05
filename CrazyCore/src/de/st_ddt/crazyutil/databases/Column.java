package de.st_ddt.crazyutil.databases;

public class Column
{

	protected final String name;
	protected final String type;
	protected final boolean primary;
	protected final boolean nulled;
	protected final String defaults;
	protected final boolean autoincrement;

	public Column(String name, String type)
	{
		super();
		this.name = name;
		this.type = type;
		this.primary = false;
		this.nulled = true;
		this.defaults = null;
		this.autoincrement = false;
	}

	public Column(String name, String type, boolean primary)
	{
		super();
		this.name = name;
		this.type = type;
		this.primary = primary;
		this.nulled = false;
		this.defaults = null;
		this.autoincrement = true;
	}

	public Column(String name, String type, String defaults, boolean nulled, boolean autoincrement)
	{
		super();
		this.name = name;
		this.type = type;
		this.primary = false;
		this.nulled = nulled;
		this.defaults = defaults;
		this.autoincrement = autoincrement;
	}

	public String getName()
	{
		return name;
	}

	public String getType()
	{
		return type;
	}

	public boolean isPrimary()
	{
		return primary;
	}

	public boolean isNulled()
	{
		return nulled;
	}

	public String getDefaults()
	{
		return defaults;
	}

	public boolean isAutoincrement()
	{
		return autoincrement;
	}

	public String getCreateString()
	{
		return name + " " + type + " " + (autoincrement ? "auto_increment " : "") + (primary ? "primary key " : "") + (nulled ? "NULL " : "NOT NULL") + (defaults == null ? "" : "DEFAULT " + defaults + " ");
	}

	public static String getFullCreateString(Column... columns)
	{
		int length = columns.length;
		if (length == 0)
			return "";
		String res = columns[0].getCreateString();
		for (int i = 1; i < length; i++)
			res += ", " + columns[i].getCreateString();
		return res;
	}
}
