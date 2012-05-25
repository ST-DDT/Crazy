package de.st_ddt.crazyloginfilter;

import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazyloginfilter.databases.CrazyLoginFilterConfigurationDatabase;
import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.databases.Database;
import de.st_ddt.crazyutil.databases.DatabaseType;

public class CrazyLoginFilter extends CrazyPlugin
{

	private static CrazyLoginFilter plugin;
	protected final HashMap<String, PlayerAccessFilter> datas = new HashMap<String, PlayerAccessFilter>();
	private CrazyLoginFilterPlayerListener playerListener;
	// Database
	protected Database<PlayerAccessFilter> database;

	public static CrazyLoginFilter getPlugin()
	{
		return plugin;
	}

	@Override
	public void onEnable()
	{
		plugin = this;
		registerHooks();
		super.onEnable();
	}

	public void registerHooks()
	{
		this.playerListener = new CrazyLoginFilterPlayerListener(this);
		final PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(playerListener, this);
	}

	@Override
	public void load()
	{
		super.load();
		setupDatabase();
		datas.clear();
		if (database != null)
			for (final PlayerAccessFilter data : database.getAllEntries())
				datas.put(data.getName().toLowerCase(), data);
	}

	public void setupDatabase()
	{
		final ConfigurationSection config = getConfig();
		String saveType = config.getString("database.saveType", "flat").toLowerCase();
		DatabaseType type = DatabaseType.valueOf(saveType.toUpperCase());
		final String tableName = config.getString("database.tableName", "accessfilter");
		config.set("database.tableName", tableName);
		// Columns
		final String colName = config.getString("database.columns.name", "name");
		config.set("database.columns.name", colName);
		final String colCheckIPs = config.getString("database.columns.checkIPs", "checkIPs");
		config.set("database.columns.checkIPs", colCheckIPs);
		final String colCheckConnections = config.getString("database.columns.checkConnections", "checkConnections");
		config.set("database.columns.checkConnections", colCheckConnections);
		final String colWhitelistIPs = config.getString("database.columns.whitelistIPs", "whitelistIPs");
		config.set("database.columns.whitelistIPs", colWhitelistIPs);
		final String colWhitelistConnections = config.getString("database.columns.whitelistConnections", "whitelistConnections");
		config.set("database.columns.whitelistConnections", colWhitelistConnections);
		final String colIPs = config.getString("database.columns.IPs", "IPs");
		config.set("database.columns.IPs", colIPs);
		final String colConnections = config.getString("database.columns.connections", "connections");
		config.set("database.columns.connections", colConnections);
		try
		{
			if (type == DatabaseType.CONFIG)
			{
				database = new CrazyLoginFilterConfigurationDatabase(config, tableName, colName, colCheckIPs, colCheckConnections, colWhitelistIPs, colWhitelistConnections, colIPs, colConnections);
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			database = null;
		}
		finally
		{
			if (database == null)
				broadcastLocaleMessage(true, "crazyloginfilter.warndatabase", "CRAZYLOGINFILTER.DATABASE.ACCESSWARN", saveType);
		}
	}

	@Override
	public void save()
	{
		final ConfigurationSection config = getConfig();
		if (database != null)
			config.set("database.saveType", database.getType().toString());
		if (database != null)
			database.saveAll(datas.values());
		super.save();
	}

	@Override
	public boolean commandMain(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyException
	{
		if (commandLabel.equalsIgnoreCase("show"))
		{
			commandMainShow(sender, args);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("ip") || commandLabel.equalsIgnoreCase("ips"))
		{
			commandMainIP(sender, args);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("connection") || commandLabel.equalsIgnoreCase("connections"))
		{
			commandMainConnection(sender, args);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("delete"))
		{
			commandMainDelete(sender, args);
			return true;
		}
		return false;
	}

	private void commandMainShow(CommandSender sender, String[] args)
	{
		// EDIT Auto-generated method stub
	}

	private void commandMainIP(CommandSender sender, String[] args)
	{
		// EDIT Auto-generated method stub
	}

	private void commandMainConnection(CommandSender sender, String[] args)
	{
		// EDIT Auto-generated method stub
	}

	private void commandMainDelete(CommandSender sender, String[] args)
	{
		// EDIT Auto-generated method stub
	}

	public boolean checkIP(Player player)
	{
		return checkIP(player.getName(), player.getAddress().getAddress().getHostAddress());
	}

	public boolean checkIP(String player, String IP)
	{
		PlayerAccessFilter filter = getPlayerAccessFilter(player);
		if (filter == null)
			return true;
		return filter.checkIP(IP);
	}

	public boolean checkConnection(Player player)
	{
		return checkConnection(player.getName(), player.getAddress().getHostName());
	}

	public boolean checkConnection(String player, String connection)
	{
		PlayerAccessFilter filter = getPlayerAccessFilter(player);
		if (filter == null)
			return true;
		return filter.checkConnection(connection);
	}

	public PlayerAccessFilter getPlayerAccessFilter(Player player)
	{
		return getPlayerAccessFilter(player.getName());
	}

	public PlayerAccessFilter getPlayerAccessFilter(String player)
	{
		return datas.get(player.toLowerCase());
	}
}
