package de.st_ddt.crazyloginfilter.data;

import java.util.ArrayList;
import java.util.Date;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.st_ddt.crazyloginfilter.CrazyLoginFilter;
import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.data.PlayerData;
import de.st_ddt.crazyutil.databases.ConfigurationDatabaseEntry;

public class PlayerAccessFilter extends PlayerData<PlayerAccessFilter> implements ConfigurationDatabaseEntry
{

	protected boolean checkIP;
	protected boolean whitelistIP;
	protected final ArrayList<String> IPs = new ArrayList<String>();
	protected boolean checkConnection;
	protected boolean whitelistConnection;
	protected final ArrayList<String> connections = new ArrayList<String>();

	public PlayerAccessFilter(final OfflinePlayer player)
	{
		this(player.getName());
	}

	public PlayerAccessFilter(final String name)
	{
		super(name);
	}

	public PlayerAccessFilter(final ConfigurationSection config)
	{
		super(config.getString("name"));
		whitelistIP = config.getBoolean("whitelistIP");
		whitelistConnection = config.getBoolean("whitelistConnection");
		checkIP = config.getBoolean("checkIP");
		checkConnection = config.getBoolean("checkConnection");
		IPs.addAll(config.getStringList("ips"));
		connections.addAll(config.getStringList("connections"));
	}

	public PlayerAccessFilter(final ConfigurationSection rawData, final String[] columnNames)
	{
		super(rawData.getString(columnNames[0]));
		whitelistIP = rawData.getBoolean(columnNames[2]);
		whitelistConnection = rawData.getBoolean(columnNames[5]);
		checkIP = rawData.getBoolean(columnNames[1]);
		checkConnection = rawData.getBoolean(columnNames[4]);
		IPs.addAll(rawData.getStringList(columnNames[3]));
		connections.addAll(rawData.getStringList(columnNames[6]));
	}

	public boolean isCheckIP()
	{
		return checkIP;
	}

	public void setCheckIP(final boolean checkIP)
	{
		this.checkIP = checkIP;
	}

	public boolean isWhitelistIP()
	{
		return whitelistIP;
	}

	public void setWhitelistIP(final boolean whitelistIP)
	{
		this.whitelistIP = whitelistIP;
	}

	public boolean checkIP(final String IP)
	{
		if (!checkIP)
			return true;
		for (final String regex : IPs)
			if (IP.matches(regex))
				return whitelistIP;
		return !whitelistIP;
	}

	public ArrayList<String> getIPs()
	{
		return IPs;
	}

	public void addIP(final String IP)
	{
		if (!IPs.contains(IP))
			IPs.add(IP);
	}

	public boolean removeIP(final String IP)
	{
		return IPs.remove(IP);
	}

	public String removeIP(final int index)
	{
		return IPs.remove(index);
	}

	public boolean isCheckConnection()
	{
		return checkConnection;
	}

	public void setCheckConnection(final boolean checkConnection)
	{
		this.checkConnection = checkConnection;
	}

	public boolean isWhitelistConnection()
	{
		return whitelistConnection;
	}

	public void setWhitelistConnection(final boolean whitelistConnection)
	{
		this.whitelistConnection = whitelistConnection;
	}

	public boolean checkConnection(final String connection)
	{
		if (!checkConnection)
			return true;
		for (final String regex : connections)
			if (connection.matches(regex))
				return whitelistConnection;
		return !whitelistConnection;
	}

	public ArrayList<String> getConnections()
	{
		return connections;
	}

	public void addConnection(final String connection)
	{
		if (!connections.contains(connection))
			connections.add(connection);
	}

	public boolean removeConnection(final String connection)
	{
		return connections.remove(connection);
	}

	public String removeConnection(final int index)
	{
		return connections.remove(index);
	}

	@Override
	public void saveToConfigDatabase(final ConfigurationSection config, final String table, final String[] columnNames)
	{
		config.set(table + "." + columnNames[0], name);
		config.set(table + "." + columnNames[1], checkIP);
		config.set(table + "." + columnNames[2], whitelistIP);
		config.set(table + "." + columnNames[3], IPs);
		config.set(table + "." + columnNames[4], checkConnection);
		config.set(table + "." + columnNames[5], whitelistConnection);
		config.set(table + "." + columnNames[6], connections);
	}

	@Override
	public String getParameter(final int index)
	{
		switch (index)
		{
			case 0:
				return getName();
		}
		return "";
	}

	@Override
	public int getParameterCount()
	{
		return 1;
	}

	@Override
	public String getShortInfo(final String... args)
	{
		return toString();
	}

	@Override
	public void show(final CommandSender sender)
	{
		final CrazyLoginFilter plugin = CrazyLoginFilter.getPlugin();
		final Player player = getPlayer();
		plugin.sendLocaleMessage("PLAYERINFO.HEAD", sender, CrazyPluginInterface.DateFormat.format(new Date()));
		plugin.sendLocaleMessage("PLAYERINFO.USERNAME", sender, getName());
		if (player != null)
		{
			plugin.sendLocaleMessage("PLAYERINFO.DISPLAYNAME", sender, player.getDisplayName());
			plugin.sendLocaleMessage("PLAYERINFO.IPADDRESS", sender, player.getAddress().getAddress().getHostAddress());
			plugin.sendLocaleMessage("PLAYERINFO.CONNECTION", sender, player.getAddress().getHostName());
			if (sender.hasPermission("crazyloginfilter.playerinfo.extended"))
				plugin.sendLocaleMessage("PLAYERINFO.URL", sender, player.getAddress().getAddress().getHostAddress());
		}
	}

	@Override
	public void show(final CommandSender sender, final String... args)
	{
		show(sender);
	}
}
