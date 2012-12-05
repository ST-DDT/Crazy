package de.st_ddt.crazyutil.databases;

public class SQLColumn
{

	protected final String name;
	protected String realName;
	protected final String type;
	protected final boolean primary;
	protected final boolean nulled;
	protected final String defaults;
	protected final boolean autoincrement;

	public SQLColumn(final String name, final String type)
	{
		super();
		this.name = name;
		this.realName = name;
		this.type = type;
		this.primary = false;
		this.nulled = true;
		this.defaults = null;
		this.autoincrement = false;
	}

	public SQLColumn(final String name, final String type, final boolean primary, final boolean autoincrement)
	{
		super();
		this.name = name;
		this.realName = name;
		this.type = type;
		this.primary = primary;
		this.nulled = false;
		this.defaults = null;
		this.autoincrement = autoincrement;
	}

	public SQLColumn(final String name, final String type, final String defaults, final boolean nulled, final boolean autoincrement)
	{
		super();
		this.name = name;
		this.realName = name;
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

	public String getRealName()
	{
		return realName;
	}

	public void setRealName(final String realName)
	{
		this.realName = realName;
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
		return getCreateString(false);
	}

	public String getCreateString(final boolean skipPrimary)
	{
		if (skipPrimary)
			return realName + " " + type + " " + (autoincrement ? "auto_increment " : "") + (primary ? "" : (nulled ? "NULL " : "NOT NULL ")) + (defaults == null ? "" : "DEFAULT " + defaults + " ");
		else
			return realName + " " + type + " " + (autoincrement ? "auto_increment " : "") + (primary ? "primary key " : (nulled ? "NULL " : "NOT NULL ")) + (defaults == null ? "" : "DEFAULT " + defaults + " ");
	}

	public static String getFullCreateString(final SQLColumn... columns)
	{
		final int length = columns.length;
		if (length == 0)
			return "";
		String res = columns[0].getCreateString();
		for (int i = 1; i < length; i++)
			res += ", " + columns[i].getCreateString();
		return res;
	}
}
