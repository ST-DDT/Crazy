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
import de.st_ddt.crazyutil.source.Localized;

public class CrazyLoginFilterCommandIP extends CrazyLoginFilterCommandExecutor
{

	public CrazyLoginFilterCommandIP(final CrazyLoginFilter plugin)
	{
		super(plugin);
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!(sender instanceof Player))
			throw new CrazyCommandExecutorException(false);
		final Player player = (Player) sender;
		ip(sender, args, plugin.getPlayerData(player));
	}

	@Localized({ "CRAZYLOGINFILTER.COMMAND.IP.LIST.HEADER $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$", "CRAZYLOGINFILTER.COMMAND.IP.LIST.LISTFORMAT $Index$ $Entry$ $ChatHeader$", "CRAZYLOGINFILTER.COMMAND.IP.LIST.ENTRYFORMAT", "CRAZYLOGINFILTER.COMMAND.IP.ADDED $IP$", "CRAZYLOGINFILTER.COMMAND.IP.DELETED $IP$", "CRAZYLOGINFILTER.COMMAND.IP.CHECK $Boolean$", "CRAZYLOGINFILTER.COMMAND.IP.WHITELIST $Boolean$" })
	public void ip(final CommandSender sender, final String[] args, final PlayerAccessFilter data) throws CrazyCommandException
	{
		if (data == null)
			throw new CrazyCommandCircumstanceException("when AccessFilter is created!");
		if (args.length != 1 && args.length != 2)
			throw new CrazyCommandUsageException("show [Page]", "<add/add* (Regex)/remove> <Value>", "whitelist [True/False]");
		final String commandLabel = args[0].toLowerCase();
		if (args.length == 1)
		{
			if (commandLabel.equals("show") || commandLabel.equals("list"))
				plugin.sendLocaleList(sender, "COMMAND.IP.LIST", 10, 1, data.getIPs());
			else if (commandLabel.equals("check"))
				plugin.sendLocaleMessage("COMMAND.IP.CHECK", sender, data.isCheckIP() ? "True" : "False");
			else if (commandLabel.equals("whitelist"))
				plugin.sendLocaleMessage("COMMAND.IP.WHITELIST", sender, data.isWhitelistIP() ? "True" : "False");
		}
		else
		{
			String ip = args[1];
			final String regex = StringUtils.replace(ip, "*", ".*");
			if (commandLabel.equals("add"))
			{
				data.addIP(regex);
				plugin.sendLocaleMessage("COMMAND.IP.ADDED", sender, regex);
				if (data == plugin.getServerAccessFilter())
					plugin.saveConfiguration();
				else
					plugin.getCrazyDatabase().save(data);
			}
			else if (commandLabel.equals("add*"))
			{
				data.addIP(ip);
				plugin.sendLocaleMessage("COMMAND.IP.ADDED", sender, ip);
				if (data == plugin.getServerAccessFilter())
					plugin.saveConfiguration();
				else
					plugin.getCrazyDatabase().save(data);
			}
			else if (commandLabel.equals("remove") || commandLabel.equals("delete"))
			{
				try
				{
					final int index = Integer.parseInt(ip);
					ip = data.removeIP(index);
				}
				catch (final NumberFormatException e)
				{
					data.removeIP(ip);
				}
				plugin.sendLocaleMessage("COMMAND.IP.DELETED", sender, ip);
				if (data == plugin.getServerAccessFilter())
					plugin.saveConfiguration();
				else
					plugin.getCrazyDatabase().save(data);
			}
			else if (commandLabel.equals("show") || commandLabel.equals("list"))
			{
				if (args[1].equals("*"))
					plugin.sendLocaleList(sender, "COMMAND.IP.LIST", 10, -1, data.getIPs());
				else
					try
					{
						final int page = Integer.parseInt(args[1]);
						plugin.sendLocaleList(sender, "COMMAND.IP.LIST", 10, page, data.getIPs());
					}
					catch (final NumberFormatException e)
					{
						throw new CrazyCommandParameterException(1, "Integer");
					}
			}
			else if (commandLabel.equals("check"))
			{
				boolean newValue = false;
				if (args[1].equalsIgnoreCase("1") || args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("yes"))
					newValue = true;
				data.setCheckIP(newValue);
				plugin.sendLocaleMessage("COMMAND.IP.CHECK", sender, data.isCheckIP() ? "True" : "False");
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
				data.setWhitelistIP(newValue);
				plugin.sendLocaleMessage("COMMAND.IP.WHITELIST", sender, data.isWhitelistIP() ? "True" : "False");
				if (data == plugin.getServerAccessFilter())
					plugin.saveConfiguration();
				else
					plugin.getCrazyDatabase().save(data);
			}
		}
	}
}
