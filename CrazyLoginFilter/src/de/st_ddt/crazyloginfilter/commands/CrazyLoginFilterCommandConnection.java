package de.st_ddt.crazyloginfilter.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyloginfilter.CrazyLoginFilter;
import de.st_ddt.crazyloginfilter.data.PlayerAccessFilter;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandExecutorException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazyLoginFilterCommandConnection extends CrazyLoginFilterCommandExecutor
{

	public CrazyLoginFilterCommandConnection(final CrazyLoginFilter plugin)
	{
		super(plugin);
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!(sender instanceof Player))
			throw new CrazyCommandExecutorException(false);
		final Player player = (Player) sender;
		connection(sender, args, plugin.getPlayerData(player));
	}

	@Localized({ "CRAZYLOGINFILTER.COMMAND.CONNECTION.LIST.HEADER $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$", "CRAZYLOGINFILTER.COMMAND.CONNECTION.LIST.LISTFORMAT $Index$ $Entry$ $ChatHeader$", "CRAZYLOGINFILTER.COMMAND.CONNECTION.LIST.ENTRYFORMAT", "CRAZYLOGINFILTER.COMMAND.CONNECTION.ADDED $Connection$", "CRAZYLOGINFILTER.COMMAND.CONNECTION.DELETED $Connection$", "CRAZYLOGINFILTER.COMMAND.CONNECTION.CHECK $Boolean$", "CRAZYLOGINFILTER.COMMAND.CONNECTION.WHITELIST $Boolean$" })
	public void connection(final CommandSender sender, final String[] args, final PlayerAccessFilter data) throws CrazyCommandException
	{
		if (data == null)
			throw new CrazyCommandCircumstanceException("when AccessFilter is created!");
		if (args.length != 1 && args.length != 2)
			throw new CrazyCommandUsageException("show [Page]", "<add/add* (Regex)/remove> <Value>", "<whitelist/check> [True/False]");
		final String commandLabel = args[0].toLowerCase();
		if (args.length == 1)
		{
			if (commandLabel.equals("show") || commandLabel.equals("list"))
				plugin.sendLocaleList(sender, "COMMAND.CONNECTION.LIST", 10, 1, data.getConnections());
			else if (commandLabel.equals("check"))
				plugin.sendLocaleMessage("COMMAND.CONNECTION.CHECK", sender, data.isCheckConnection() ? "True" : "False");
			else if (commandLabel.equals("whitelist"))
				plugin.sendLocaleMessage("COMMAND.CONNECTION.WHITELIST", sender, data.isWhitelistConnection() ? "True" : "False");
		}
		else
		{
			String connection = args[1];
			final String regex = StringUtils.replace(connection, "*", ".*");
			if (commandLabel.equals("add"))
			{
				data.addConnection(regex);
				plugin.sendLocaleMessage("COMMAND.CONNECTION.ADDED", sender, regex);
				if (data == plugin.getServerAccessFilter())
					plugin.saveConfiguration();
				else
					plugin.getCrazyDatabase().save(data);
			}
			else if (commandLabel.equals("add*"))
			{
				data.addConnection(connection);
				plugin.sendLocaleMessage("COMMAND.CONNECTION.ADDED", sender, connection);
				if (data == plugin.getServerAccessFilter())
					plugin.saveConfiguration();
				else
					plugin.getCrazyDatabase().save(data);
			}
			else if (commandLabel.equals("remove") || commandLabel.equals("delete"))
			{
				try
				{
					final int index = Integer.parseInt(connection);
					connection = data.removeConnection(index);
				}
				catch (final NumberFormatException e)
				{
					data.removeConnection(connection);
				}
				plugin.sendLocaleMessage("COMMAND.CONNECTION.DELETED", sender, connection);
				if (data == plugin.getServerAccessFilter())
					plugin.saveConfiguration();
				else
					plugin.getCrazyDatabase().save(data);
			}
			else if (commandLabel.equals("show") || commandLabel.equals("list"))
			{
				if (args[1].equals("*"))
					plugin.sendLocaleList(sender, "COMMAND.CONNECTION.LIST", 10, -1, data.getIPs());
				else
					try
					{
						final int page = Integer.parseInt(args[1]);
						plugin.sendLocaleList(sender, "COMMAND.CONNECTION.LIST", 10, page, data.getConnections());
					}
					catch (final NumberFormatException e)
					{
						throw new CrazyCommandParameterException(1, "Integer");
					}
			}
			else if (commandLabel.equals("check"))
			{
				boolean newValue = false;
				if (args[1].equals("1") || args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("yes"))
					newValue = true;
				data.setCheckConnection(newValue);
				plugin.sendLocaleMessage("COMMAND.CONNECTION.CHECK", sender, data.isCheckConnection() ? "True" : "False");
				if (data == plugin.getServerAccessFilter())
					plugin.saveConfiguration();
				else
					plugin.getCrazyDatabase().save(data);
			}
			else if (commandLabel.equals("whitelist"))
			{
				boolean newValue = false;
				if (args[1].equals("1") || args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("yes"))
					newValue = true;
				data.setWhitelistConnection(newValue);
				plugin.sendLocaleMessage("COMMAND.CONNECTION.WHITELIST", sender, data.isWhitelistConnection() ? "True" : "False");
				if (data == plugin.getServerAccessFilter())
					plugin.saveConfiguration();
				else
					plugin.getCrazyDatabase().save(data);
			}
		}
	}
}
