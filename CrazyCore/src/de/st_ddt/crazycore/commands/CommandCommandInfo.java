package de.st_ddt.crazycore.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.CommandHelper;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.Permission;

public class CommandCommandInfo extends CommandExecutor
{

	public CommandCommandInfo(final CrazyCore plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYCORE.COMMAND.COMMANDINFO.NAME $Name$", "CRAZYCORE.COMMAND.COMMANDINFO.DESCRIPTION $Description$", "CRAZYCORE.COMMAND.COMMANDINFO.USAGE $Usage$", "CRAZYCORE.COMMAND.COMMANDINFO.PERMISSION $Permission$", "CRAZYCORE.COMMAND.COMMANDINFO.ALIASES $Aliases$", "CRAZYCORE.COMMAND.COMMANDINFO.OWNER $Owner$", })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length != 1)
			throw new CrazyCommandUsageException("<Command>");
		final String commandName = args[0];
		final TreeMap<String, Command> commands = new TreeMap<String, Command>(String.CASE_INSENSITIVE_ORDER);
		commands.putAll(CommandHelper.getSaveCommandMap());
		final Command command = commands.get(commandName);
		if (command == null)
			throw new CrazyCommandNoSuchException("Command", commandName);
		plugin.sendLocaleMessage("COMMAND.COMMANDINFO.NAME", sender, command.getName());
		plugin.sendLocaleMessage("COMMAND.COMMANDINFO.DESCRIPTION", sender, command.getDescription());
		plugin.sendLocaleMessage("COMMAND.COMMANDINFO.USAGE", sender, command.getUsage());
		final String permission = command.getPermission();
		if (permission == null)
			plugin.sendLocaleMessage("COMMAND.COMMANDINFO.PERMISSION", sender, "UNKNOWN");
		else
			plugin.sendLocaleMessage("COMMAND.COMMANDINFO.PERMISSION", sender, permission.toLowerCase());
		plugin.sendLocaleMessage("COMMAND.COMMANDINFO.ALIASES", sender, ChatHelper.listingString(",  ", command.getAliases()));
		if (command instanceof PluginIdentifiableCommand)
			plugin.sendLocaleMessage("COMMAND.COMMANDINFO.OWNER", sender, ((PluginIdentifiableCommand) command).getPlugin().getName());
		else
			plugin.sendLocaleMessage("COMMAND.COMMANDINFO.OWNER", sender, "SERVER");
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		if (args.length != 1)
			return null;
		final String arg = args[0].toLowerCase();
		final List<String> res = new ArrayList<String>(20);
		for (final String command : CommandHelper.getCommandNames())
			if (command.toLowerCase().startsWith(arg))
			{
				res.add(command);
				if (res.size() == 20)
					return res;
			}
		return res;
	}

	@Override
	@Permission("crazycore.commandinfo")
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazycore.commandinfo");
	}
}
