package de.st_ddt.crazyutil.databases;

public abstract class DatabaseEntry<S extends DatabaseSaveable, T>
{

	public abstract S load(T rawData);

	public abstract void save(S data, T saveTo);
}
