package de.st_ddt.crazyutil.databases;

import java.sql.Connection;
import java.sql.SQLException;

public interface MainConnection
{

	public Connection openConnection() throws SQLException;
}
