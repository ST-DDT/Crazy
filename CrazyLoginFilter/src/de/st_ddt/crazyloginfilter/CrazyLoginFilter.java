package de.st_ddt.crazyloginfilter;

import java.util.HashMap;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazyloginfilter.databases.CrazyLoginFilterConfigurationDatabase;
import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandExecutorException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyplugin.exceptions.CrazyNotImplementedException;
import de.st_ddt.crazyutil.ToStringDataGetter;
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
	protected String getShortPluginName()
	{
		return "clf";
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
		String saveType = config.getString("database.saveType", "CONFIG").toUpperCase();
		DatabaseType type = DatabaseType.valueOf(saveType);
		final String tableName = config.getString("database.tableName", "accessfilter");
		config.set("database.tableName", tableName);
		// Columns
		final String colName = config.getString("database.columns.name", "name");
		config.set("database.columns.name", colName);
		final String colCheckIPs = config.getString("database.columns.checkIPs", "checkIPs");
		config.set("database.columns.checkIPs", colCheckIPs);
		final String colWhitelistIPs = config.getString("database.columns.whitelistIPs", "whitelistIPs");
		config.set("database.columns.whitelistIPs", colWhitelistIPs);
		final String colIPs = config.getString("database.columns.IPs", "IPs");
		config.set("database.columns.IPs", colIPs);
		final String colCheckConnections = config.getString("database.columns.checkConnections", "checkConnections");
		config.set("database.columns.checkConnections", colCheckConnections);
		final String colWhitelistConnections = config.getString("database.columns.whitelistConnections", "whitelistConnections");
		config.set("database.columns.whitelistConnections", colWhitelistConnections);
		final String colConnections = config.getString("database.columns.connections", "connections");
		config.set("database.columns.connections", colConnections);
		try
		{
			if (type == DatabaseType.CONFIG)
			{
				database = new CrazyLoginFilterConfigurationDatabase(config, tableName, colName, colCheckIPs, colWhitelistIPs, colIPs, colCheckConnections, colWhitelistConnections, colConnections);
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
				broadcastLocaleMessage(true, "crazyloginfilter.warndatabase", "DATABASE.ACCESSWARN", saveType);
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
		if (commandLabel.equalsIgnoreCase("create"))
		{
			commandMainCreate(sender, args);
			return true;
		}
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

	private void commandMainCreate(CommandSender sender, String[] args) throws CrazyCommandException
	{
		if (sender instanceof ConsoleCommandSender)
			throw new CrazyCommandExecutorException(false);
		Player player = (Player) sender;
		PlayerAccessFilter data = new PlayerAccessFilter(player);
		data.setCheckIP(false);
		data.setCheckConnection(false);
		datas.put(player.getName().toLowerCase(), data);
		sendLocaleMessage("COMMMAND.CREATED", sender, player.getName());
	}

	private void commandMainShow(CommandSender sender, String[] args) throws CrazyException
	{
		throw new CrazyNotImplementedException();
	}

	private void commandMainIP(CommandSender sender, String[] args) throws CrazyCommandException
	{
		if (sender instanceof ConsoleCommandSender)
			throw new CrazyCommandExecutorException(false);
		Player player = (Player) sender;
		commandMainIP(sender, args, getPlayerAccessFilter(player));
	}

	private void commandMainIP(CommandSender sender, String[] args, PlayerAccessFilter data) throws CrazyCommandException
	{
		if (data == null)
			throw new CrazyCommandCircumstanceException("when AccessFilter is created!");
		switch (args.length)
		{
			case 1:
				if (args[0].equalsIgnoreCase("show") || args[0].equalsIgnoreCase("list"))
				{
					sendListMessage(sender, "COMMMAND.IP.LISTHEAD", 1, data.getIPs(), new ToStringDataGetter());
					return;
				}
				if (args[0].equalsIgnoreCase("check"))
				{
					sendLocaleMessage("COMMMAND.IP.CHECK", sender, data.isCheckIP() ? "True" : "False");
					return;
				}
				if (args[0].equalsIgnoreCase("whitelist"))
				{
					sendLocaleMessage("COMMMAND.IP.WHITELIST", sender, data.isWhitelistIP() ? "True" : "False");
					return;
				}
				break;
			case 2:
				String regex = args[1];
				if (args[0].equalsIgnoreCase("add"))
				{
					data.addIP(regex);
					sendLocaleMessage("COMMAND.IP.ADDED", sender, regex);
					database.save(data);
					return;
				}
				if (args[0].equalsIgnoreCase("remove"))
				{
					try
					{
						int index = Integer.parseInt(regex);
						regex = data.removeIP(index);
					}
					catch (NumberFormatException e)
					{
						data.removeIP(regex);
					}
					sendLocaleMessage("COMMAND.IP.REMOVED", sender, regex);
					database.save(data);
					return;
				}
				if (args[0].equalsIgnoreCase("show") || args[0].equalsIgnoreCase("list"))
				{
					try
					{
						int page = Integer.parseInt(args[1]);
						sendListMessage(sender, "COMMMAND.IP.LISTHEAD", page, data.getIPs(), new ToStringDataGetter());
					}
					catch (NumberFormatException e)
					{
						throw new CrazyCommandParameterException(1, "Integer");
					}
					return;
				}
				if (args[0].equalsIgnoreCase("check"))
				{
					boolean newValue = false;
					if (args[1].equalsIgnoreCase("1") || args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("yes"))
						newValue = true;
					data.setCheckIP(newValue);
					sendLocaleMessage("COMMMAND.IP.CHECK", sender, data.isWhitelistIP() ? "True" : "False");
					database.save(data);
					return;
				}
				if (args[0].equalsIgnoreCase("whitelist"))
				{
					boolean newValue = false;
					if (args[1].equalsIgnoreCase("1") || args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("yes"))
						newValue = true;
					data.setWhitelistIP(newValue);
					sendLocaleMessage("COMMMAND.IP.WHITELIST", sender, data.isWhitelistIP() ? "True" : "False");
					database.save(data);
					return;
				}
		}
		throw new CrazyCommandUsageException("/crazyloginfilter ip show [Page]", "/crazloginfilter ip <add/remove> <Value>", "/crazloginfilter ip whitelist [True/False]");
	}

	private void commandMainConnection(CommandSender sender, String[] args) throws CrazyCommandException
	{
		if (sender instanceof ConsoleCommandSender)
			throw new CrazyCommandExecutorException(false);
		Player player = (Player) sender;
		commandMainConnection(sender, args, getPlayerAccessFilter(player));
	}

	private void commandMainConnection(CommandSender sender, String[] args, PlayerAccessFilter data) throws CrazyCommandException
	{
		if (data == null)
			throw new CrazyCommandCircumstanceException("when AccessFilter is created!");
		switch (args.length)
		{
			case 1:
				if (args[0].equalsIgnoreCase("show") || args[0].equalsIgnoreCase("list"))
				{
					sendListMessage(sender, "COMMMAND.CONNECTION.LISTHEAD", 1, data.getConnections(), new ToStringDataGetter());
					return;
				}
				if (args[0].equalsIgnoreCase("check"))
				{
					sendLocaleMessage("COMMMAND.CONNECTION.CHECK", sender, data.isCheckConnection() ? "True" : "False");
					return;
				}
				if (args[0].equalsIgnoreCase("whitelist"))
				{
					sendLocaleMessage("COMMMAND.CONNECTION.WHITELIST", sender, data.isWhitelistConnection() ? "True" : "False");
					return;
				}
				break;
			case 2:
				String regex = args[1];
				if (args[0].equalsIgnoreCase("add"))
				{
					data.addConnection(regex);
					sendLocaleMessage("COMMAND.CONNECTION.ADDED", sender, regex);
					database.save(data);
					return;
				}
				if (args[0].equalsIgnoreCase("remove"))
				{
					try
					{
						int index = Integer.parseInt(regex);
						regex = data.removeConnection(index);
					}
					catch (NumberFormatException e)
					{
						data.removeConnection(regex);
					}
					sendLocaleMessage("COMMAND.CONNECTION.REMOVED", sender, regex);
					database.save(data);
					return;
				}
				if (args[0].equalsIgnoreCase("show") || args[0].equalsIgnoreCase("list"))
				{
					try
					{
						int page = Integer.parseInt(args[1]);
						sendListMessage(sender, "COMMMAND.CONNECTION.LISTHEAD", page, data.getConnections(), new ToStringDataGetter());
					}
					catch (NumberFormatException e)
					{
						throw new CrazyCommandParameterException(1, "Integer");
					}
					return;
				}
				if (args[0].equalsIgnoreCase("check"))
				{
					boolean newValue = false;
					if (args[1].equalsIgnoreCase("1") || args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("yes"))
						newValue = true;
					data.setCheckConnection(newValue);
					sendLocaleMessage("COMMMAND.CONNECTION.CHECK", sender, data.isWhitelistConnection() ? "True" : "False");
					database.save(data);
					return;
				}
				if (args[0].equalsIgnoreCase("whitelist"))
				{
					boolean newValue = false;
					if (args[1].equalsIgnoreCase("1") || args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("yes"))
						newValue = true;
					data.setWhitelistConnection(newValue);
					sendLocaleMessage("COMMMAND.CONNECTION.WHITELIST", sender, data.isWhitelistConnection() ? "True" : "False");
					database.save(data);
					return;
				}
		}
		throw new CrazyCommandUsageException("/crazyloginfilter connection show [Page]", "/crazloginfilter connection <add/remove> <Value>", "/crazloginfilter connection whitelist [True/False]");
	}

	private void commandMainDelete(CommandSender sender, String[] args) throws CrazyException
	{
		throw new CrazyNotImplementedException();
	}

	public boolean checkIP(Player player)
	{
		return checkIP(player.getName(), player.getAddress().getAddress().getHostAddress());
	}

	public boolean checkIP(Player player, String IP)
	{
		return checkIP(player.getName(), IP);
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

	public boolean checkConnection(Player player, String connection)
	{
		return checkConnection(player.getName(), connection);
	}

	public boolean checkConnection(String player, String connection)
	{
		PlayerAccessFilter filter = getPlayerAccessFilter(player);
		if (filter == null)
			return true;
		return filter.checkConnection(connection);
	}

	public PlayerAccessFilter getPlayerAccessFilter(OfflinePlayer player)
	{
		return getPlayerAccessFilter(player.getName());
	}

	public PlayerAccessFilter getPlayerAccessFilter(String player)
	{
		return datas.get(player.toLowerCase());
	}

	public boolean deletePlayerData(String player)
	{
		PlayerAccessFilter data = datas.remove(player.toLowerCase());
		if (data == null)
			return false;
		if (database != null)
			database.delete(data.getName());
		return true;
	}
}
