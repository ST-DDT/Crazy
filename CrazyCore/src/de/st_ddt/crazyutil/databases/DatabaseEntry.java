package de.st_ddt.crazyutil.databases;

public class DatabaseEntry<S extends Saveable, T>
{

	protected S info;

	public DatabaseEntry(S infoObject)
	{
		super();
		this.info = infoObject;
	}

	public DatabaseEntry(T rawData)
	{
		super();
		read(rawData);
	}

	public void read(T rawData)
	{
	}

	public S getInfo()
	{
		return info;
	}

	public void setInfo(S info)
	{
		this.info = info;
	}
}
