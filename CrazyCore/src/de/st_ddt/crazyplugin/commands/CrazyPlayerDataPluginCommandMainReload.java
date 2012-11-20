package de.st_ddt.crazyplugin.commands;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPlayerDataPluginInterface;
import de.st_ddt.crazyplugin.data.PlayerDataInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUnsupportedException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;

public class CrazyPlayerDataPluginCommandMainReload<T extends PlayerDataInterface> extends CrazyPlayerDataCommandExecutor<T, CrazyPlayerDataPluginInterface<T, ? extends T>>
{

	public CrazyPlayerDataPluginCommandMainReload(final CrazyPlayerDataPluginInterface<T, ? extends T> plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYPLUGIN.COMMAND.CONFIG.RELOADED", "CRAZYPLUGIN.COMMAND.DATABASE.RELOADED" })
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
		}
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		final Map<String, TabbedParamitrisable> params = new TreeMap<String, TabbedParamitrisable>();
		final BooleanParamitrisable config = new BooleanParamitrisable(args.length == 0);
		params.put("c", config);
		params.put("cfg", config);
		params.put("config", config);
		final BooleanParamitrisable database = new BooleanParamitrisable(args.length == 0);
		params.put("d", database);
		params.put("db", database);
		params.put("database", database);
		return ChatHelperExtended.tabHelp(args, params, config, database);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, plugin.getName().toLowerCase() + ".reload");
	}
}
