package de.st_ddt.crazychats.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazyplugin.CrazyLightPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.Tabbed;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TargetDateParamitrisable;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.Permission;

public class CommandServerSilence extends CommandExecutor
{

	public CommandServerSilence(final CrazyChats plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYCHATS.COMMAND.SERVERSILENCED $Until$")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		final Map<String, Paramitrisable> params = new HashMap<String, Paramitrisable>();
		final TargetDateParamitrisable until = new TargetDateParamitrisable(30000);
		params.put("u", until);
		params.put("until", until);
		ChatHelperExtended.readParameters(args, params, until);
		plugin.setServerSilenced(until.getValue());
		plugin.sendLocaleMessage("COMMAND.SERVERSILENCED", sender, CrazyLightPluginInterface.DATETIMEFORMAT.format(until.getValue()));
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		final Map<String, Tabbed> params = new TreeMap<String, Tabbed>();
		final TargetDateParamitrisable until = new TargetDateParamitrisable(30000);
		params.put("u", until);
		params.put("until", until);
		return ChatHelperExtended.tabHelp(args, params, until);
	}

	@Override
	@Permission("crazychats.serversilence")
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazychats.serversilence");
	}
}
