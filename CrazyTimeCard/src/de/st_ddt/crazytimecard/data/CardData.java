package de.st_ddt.crazytimecard.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.databases.ConfigurationDatabaseEntry;
import de.st_ddt.crazyutil.databases.DatabaseEntry;
import de.st_ddt.crazyutil.databases.FlatDatabaseEntry;
import de.st_ddt.crazyutil.databases.MySQLDatabase;
import de.st_ddt.crazyutil.databases.MySQLDatabaseEntry;

public class CardData implements DatabaseEntry, ConfigurationDatabaseEntry, FlatDatabaseEntry, MySQLDatabaseEntry
{

	private final String name;
	private final String owner;
	private String user;
	private long duration;

	public CardData(final String name, final String owner, final long duration)
	{
		super();
		this.name = name;
		this.owner = owner;
		this.duration = duration;
		this.user = null;
	}

	// aus Config-Datenbank laden
	public CardData(final ConfigurationSection config, final String[] columnNames)
	{
		this.name = config.getString(columnNames[0]);
		this.owner = config.getString(columnNames[1]);
		this.user = config.getString(columnNames[2]);
		this.duration = config.getLong(columnNames[2]);
	}

	// in Config-Datenbank speichern
	@Override
	public void saveToConfigDatabase(final ConfigurationSection config, final String path, final String[] columnNames)
	{
		config.set(path + columnNames[0], name);
		config.set(path + columnNames[1], name);
		config.set(path + columnNames[2], user);
		config.set(path + columnNames[3], duration);
	}

	// aus Flat-Datenbank laden
	public CardData(final String[] rawData)
	{
		this.name = rawData[0];
		this.owner = rawData[1];
		if (rawData[2].equals("."))
			this.user = null;
		else
			this.user = rawData[2];
		try
		{
			this.duration = Long.parseLong(rawData[3]);
		}
		catch (final NumberFormatException e)
		{
			this.duration = 86400000;
		}
	}

	// in Flat-Datenbank speichern
	@Override
	public String[] saveToFlatDatabase()
	{
		final String[] strings = new String[4];
		strings[0] = name;
		strings[1] = owner;
		if (user == null)
			strings[2] = ".";
		else
			strings[2] = user;
		strings[3] = String.valueOf(duration);
		return strings;
	}

	// aus MySQL-Datenbank laden
	public CardData(final ResultSet rawData, final String[] columnNames)
	{
		this.name = MySQLDatabase.readName(rawData, columnNames[0]);
		String tempOwner = null;
		try
		{
			tempOwner = rawData.getString(columnNames[1]);
		}
		catch (final SQLException e)
		{
			tempOwner = "FAILEDLOADING";
			e.printStackTrace();
		}
		finally
		{
			owner = tempOwner;
		}
		try
		{
			user = rawData.getString(columnNames[2]);
		}
		catch (final SQLException e)
		{
			user = "FAILEDLOADING";
			e.printStackTrace();
		}
		try
		{
			duration = rawData.getLong(columnNames[3]);
		}
		catch (final SQLException e)
		{
			duration = 0;
			e.printStackTrace();
		}
	}

	// in MySQL-Datenbank speichern
	@Override
	public String saveToMySQLDatabase(final String[] columnNames)
	{
		return columnNames[1] + "='" + owner + "', " + columnNames[2] + "='" + user + "', " + columnNames[3] + "='" + duration + "'";
	}

	@Override
	public String getName()
	{
		return name;
	}

	public String getOwner()
	{
		return owner;
	}

	public boolean isUsed()
	{
		return user != null;
	}

	public String getUser()
	{
		return user;
	}

	public void activate(final String user)
	{
		this.user = user;
	}

	public long getDuration()
	{
		return duration;
	}
}
