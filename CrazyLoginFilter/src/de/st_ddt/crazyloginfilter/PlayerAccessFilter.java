package de.st_ddt.crazyloginfilter;

import java.util.ArrayList;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.databases.ConfigurationDatabaseEntry;

public class PlayerAccessFilter implements ConfigurationDatabaseEntry
{

	protected final String name;
	protected boolean whitelistIP;
	protected boolean checkIP;
	protected final ArrayList<String> IPs = new ArrayList<String>();
	protected boolean whitelistConnection;
	protected boolean checkConnection;
	protected final ArrayList<String> connections = new ArrayList<String>();

	public PlayerAccessFilter(OfflinePlayer player)
	{
		this(player.toString());
	}

	public PlayerAccessFilter(String name)
	{
		super();
		this.name = name.toLowerCase();
	}

	public PlayerAccessFilter(ConfigurationSection config)
	{
		super();
		this.name = config.getString("name").toLowerCase();
		whitelistIP = config.getBoolean("whitelistIP");
		whitelistConnection = config.getBoolean("whitelistConnection");
		checkIP = config.getBoolean("checkIP");
		checkConnection = config.getBoolean("checkConnection");
		IPs.addAll(config.getStringList("ips"));
		connections.addAll(config.getStringList("connections"));
	}

	public String getName()
	{
		return name;
	}

	public boolean isWhitelistIP()
	{
		return whitelistIP;
	}

	public void setWhitelistIP(boolean whitelistIP)
	{
		this.whitelistIP = whitelistIP;
	}

	public boolean isWhitelistConnection()
	{
		return whitelistConnection;
	}

	public void setWhitelistConnection(boolean whitelistConnection)
	{
		this.whitelistConnection = whitelistConnection;
	}

	public boolean isCheckIP()
	{
		return checkIP;
	}

	public void setCheckIP(boolean checkIP)
	{
		this.checkIP = checkIP;
	}

	public boolean isCheckConnection()
	{
		return checkConnection;
	}

	public void setCheckConnection(boolean checkConnection)
	{
		this.checkConnection = checkConnection;
	}

	public boolean checkIP(String IP)
	{
		if (!checkIP)
			return true;
		for (String regex : IPs)
			if (IP.matches(regex))
				return whitelistIP;
		return !whitelistIP;
	}

	public ArrayList<String> getIPs()
	{
		return IPs;
	}

	public void addIP(String IP)
	{
		if (!IPs.contains(IP))
			IPs.add(IP);
	}

	public boolean removeIP(String IP)
	{
		return IPs.remove(IP);
	}

	public String removeIP(int index)
	{
		return IPs.remove(index);
	}

	public boolean checkConnection(String connection)
	{
		if (!checkConnection)
			return true;
		for (String regex : connections)
			if (connection.matches(regex))
				return whitelistConnection;
		return !whitelistConnection;
	}

	public ArrayList<String> getConnections()
	{
		return connections;
	}

	public void addConnection(String connection)
	{
		if (!connections.contains(connection))
			connections.add(connection);
	}

	public boolean removeConnection(String connection)
	{
		return connections.remove(connection);
	}

	public String removeConnection(int index)
	{
		return connections.remove(index);
	}

	@Override
	public void saveToConfigDatabase(ConfigurationSection config, String table, String[] columnNames)
	{
		config.set("name", name);
		config.set("whitelistIP", whitelistIP);
		config.set("whitelistConnection", whitelistConnection);
		config.set("checkIP", checkIP);
		config.set("checkConnection", checkConnection);
		config.set("ips", IPs);
		config.set("connections", connections);
	}
}
