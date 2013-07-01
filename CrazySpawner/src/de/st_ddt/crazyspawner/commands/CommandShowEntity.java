package de.st_ddt.crazyspawner.commands;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.CustomEntitySpawner;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.Permission;

public class CommandShowEntity extends CommandExecutor
{

	public CommandShowEntity(final CrazySpawner plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYSPAWNER.COMMAND.SHOWENTITY")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length != 1)
			throw new CrazyCommandUsageException("<CustomEntityType>");
		final String name = args[0].toUpperCase();
		for (final CustomEntitySpawner spawner : plugin.getCustomEntities())
			if (spawner.getName().equals(name))
			{
				plugin.sendLocaleMessage("COMMAND.SHOWENTITY", sender);
				spawner.show(sender);
				return;
			}
		throw new CrazyCommandNoSuchException("CustomEntityType", name, tab(sender, args));
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		if (args.length != 1)
			return new ArrayList<String>(0);
		final String name = args[0].toUpperCase();
		final List<String> res = new LinkedList<String>();
		for (final CustomEntitySpawner spawner : plugin.getCustomEntities())
			if (spawner.getName().startsWith(name))
				res.add(name);
		return res;
	}

	@Override
	@Permission("crazyspawner.showentity")
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazyspawner.showentity");
	}
}
