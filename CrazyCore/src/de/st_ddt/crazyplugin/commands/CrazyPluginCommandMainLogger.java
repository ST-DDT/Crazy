package de.st_ddt.crazyplugin.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.Logger;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.StringParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class CrazyPluginCommandMainLogger extends CrazyCommandExecutor<CrazyPluginInterface>
{

	public CrazyPluginCommandMainLogger(final CrazyPluginInterface plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYPLUGIN.COMMAND.CONFIG.NOLOGGERS", "CRAZYPLUGIN.COMMAND.CONFIG.LOGGER $Channel$ $Path$ $Console$" })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		final Logger logger = plugin.getCrazyLogger();
		if (logger.getAllLogChannelCount() == 0)
		{
			plugin.sendLocaleMessage("COMMAND.CONFIG.NOLOGGERS", sender);
			return;
		}
		if (args.length == 0)
			throw new CrazyCommandNoSuchException("LogChannel", "(none)", logger.getLogChannelNames());
		if (args[0].equals("*"))
		{
			for (final String channel : logger.getLogChannelNames())
			{
				args[0] = channel;
				command(sender, args);
			}
			return;
		}
		final String channel = args[0];
		if (!logger.hasLogChannel(channel))
			throw new CrazyCommandNoSuchException("LogChannel", channel, logger.getLogChannelNames());
		final boolean changes = args.length > 1;
		// Change Logger Config
		if (changes)
		{
			// Create Params
			final TreeMap<String, Paramitrisable> params = new TreeMap<String, Paramitrisable>();
			final StringParamitrisable pathParam = new StringParamitrisable(logger.getPath(channel));
			params.put("path", pathParam);
			final BooleanParamitrisable consoleParam = new BooleanParamitrisable(logger.isLoggedToConsole(channel));
			params.put("console", consoleParam);
			// Read Params
			try
			{
				ChatHelperExtended.readParameters(ChatHelperExtended.shiftArray(args, 1), params, pathParam, consoleParam);
			}
			catch (final CrazyCommandException e)
			{
				e.addCommandPrefix(channel);
				throw e;
			}
			// Apply Params
			final String path = pathParam.getValue();
			final boolean console = consoleParam.getValue();
			if (path != null)
				if (path.equalsIgnoreCase("false"))
					logger.createEmptyLogChannels(channel);
				else if (path.equalsIgnoreCase("true"))
					logger.createLogChannel(channel, "logs/plugin.log", null);
				else
					logger.createLogChannel(channel, path, null);
			logger.setLoggedToConsole(channel, console);
		}
		// Show
		if (logger.isActiveLogChannel(channel))
			plugin.sendLocaleMessage("COMMAND.CONFIG.LOGGER", sender, channel, logger.getPath(channel), logger.isLoggedToConsole(channel) ? "True" : "False");
		else
			plugin.sendLocaleMessage("COMMAND.CONFIG.LOGGER", sender, channel, "disabled", logger.isLoggedToConsole(channel) ? "True" : "False");
		if (changes)
		{
			logger.save(plugin.getConfig(), "logs.");
			plugin.saveConfig();
		}
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		final List<String> res = new ArrayList<String>();
		if (args.length == 1)
		{
			for (final String name : plugin.getCrazyLogger().getLogChannelNames())
				if (name.toLowerCase().startsWith(args[0].toLowerCase()))
					res.add(name);
		}
		else
		{
			final String last = args[args.length - 1].toLowerCase();
			if ("path:".startsWith(last))
				res.add("path:");
			if ("console:".startsWith(last))
				res.add("console:");
		}
		return res;
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, plugin.getName().toLowerCase() + ".logger");
	}
}
