package de.st_ddt.crazyutil.databases;

import java.sql.Connection;
import java.sql.SQLException;

import de.st_ddt.crazyutil.ConfigurationSaveable;

public interface SQLConnection extends ConfigurationSaveable
{

	public Connection openConnection() throws SQLException;
}
