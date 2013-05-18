package de.st_ddt.crazychats.commands;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.data.ChatPlayerData;
import de.st_ddt.crazyplugin.commands.CrazyPlayerDataPluginCommandMainReload;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUnsupportedException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;

public class CommandMainReload extends CrazyPlayerDataPluginCommandMainReload<ChatPlayerData>
{

	public CommandMainReload(final CrazyChats plugin)
	{
		super(plugin);
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		final Map<String, Paramitrisable> params = new TreeMap<String, Paramitrisable>();
		final BooleanParamitrisable config = new BooleanParamitrisable(args.length == 0);
		params.put("c", config);
		params.put("cfg", config);
		params.put("config", config);
		final BooleanParamitrisable database = new BooleanParamitrisable(args.length == 0);
		params.put("d", database);
		params.put("db", database);
		params.put("database", database);
		final String[] pipe = ChatHelperExtended.readParameters(args, params);
		if (pipe != null)
			throw new CrazyCommandUnsupportedException("PipeCommands", pipe);
		if (!config.getValue() && !database.getValue())
			throw new CrazyCommandUsageException("[c:true] [d:true]", "[cfg:true] [db:true]", "[config:true] [database:true]");
		if (config.getValue())
		{
			plugin.reloadConfig();
			plugin.loadConfiguration();
			plugin.sendLocaleMessage("COMMAND.CONFIG.RELOADED", sender);
			plugin.saveConfiguration();
		}
		if (database.getValue())
		{
			plugin.loadDatabase();
			plugin.sendLocaleMessage("COMMAND.DATABASE.RELOADED", sender);
			plugin.saveDatabase();
			for (final Player player : Bukkit.getOnlinePlayers())
				((CrazyChats) plugin).getPlayerListener().PlayerJoinEnabled(player);
		}
	}
}
