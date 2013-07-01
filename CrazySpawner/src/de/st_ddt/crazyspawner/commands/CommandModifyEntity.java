package de.st_ddt.crazyspawner.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.CustomEntitySpawner;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.NamedEntitySpawner;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.EnumParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.NamedEntitySpawnerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.StringParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.Permission;

public class CommandModifyEntity extends CommandExecutor
{

	public CommandModifyEntity(final CrazySpawner plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYSPAWNER.COMMAND.MODIFYENTITY $EntityType$ $Name$")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length < 2)
			throw new CrazyCommandUsageException("<Inheritance/EntityType> [name:String] [Params...]");
		final String inheritance = args[0];
		final EntityType type;
		final StringParamitrisable nameParam;
		final Map<String, TabbedParamitrisable> params = new TreeMap<String, TabbedParamitrisable>();
		final NamedEntitySpawner spawner = NamedEntitySpawnerParamitrisable.getNamedEntitySpawner(inheritance);
		if (spawner == null || !(spawner instanceof CustomEntitySpawner))
		{
			type = EntityType.valueOf(inheritance.toUpperCase());
			if (type == null)
				throw new CrazyCommandNoSuchException("Inheritance/EntityType", inheritance, EnumParamitrisable.getEnumNames(CustomEntitySpawner.getSpawnableEntityTypes()));
			nameParam = CustomEntitySpawner.getCommandParams(type, params, sender);
		}
		else
		{
			type = spawner.getType();
			nameParam = ((CustomEntitySpawner) spawner).getCommandParams(params, sender);
		}
		ChatHelperExtended.readParameters(ChatHelperExtended.shiftArray(args, 1), new HashMap<String, TabbedParamitrisable>(params), nameParam);
		if (nameParam.getValue() == null)
			throw new CrazyCommandUsageException("<Inheritance/EntityType> <name:String> [Params...]");
		final CustomEntitySpawner entitySpawner = new CustomEntitySpawner(type, params);
		plugin.addCustomEntity(entitySpawner);
		plugin.sendLocaleMessage("COMMAND.MODIFYENTITY", sender, type.getName(), nameParam.getValue());
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		if (args.length == 1)
		{
			final List<String> res = new ArrayList<String>();
			res.addAll(EnumParamitrisable.getEnumNames(CustomEntitySpawner.getSpawnableEntityTypes()));
			for (final CustomEntitySpawner spawner : plugin.getCustomEntities())
				res.add(spawner.getName().toUpperCase());
			final String inheritance = args[0].toUpperCase();
			final Iterator<String> it = res.iterator();
			while (it.hasNext())
				if (!it.next().startsWith(inheritance))
					it.remove();
			return res;
		}
		else
		{
			final String inheritance = args[0];
			final EntityType type;
			final StringParamitrisable nameParam;
			final Map<String, TabbedParamitrisable> params = new TreeMap<String, TabbedParamitrisable>();
			final NamedEntitySpawner spawner = NamedEntitySpawnerParamitrisable.getNamedEntitySpawner(inheritance);
			if (spawner == null || !(spawner instanceof CustomEntitySpawner))
			{
				type = EntityType.valueOf(inheritance.toUpperCase());
				if (type == null)
					return new ArrayList<String>(0);
				nameParam = CustomEntitySpawner.getCommandParams(type, params, sender);
			}
			else
			{
				type = spawner.getType();
				nameParam = ((CustomEntitySpawner) spawner).getCommandParams(params, sender);
			}
			return ChatHelperExtended.tabHelp(ChatHelperExtended.shiftArray(args, 1), params, nameParam);
		}
	}

	@Override
	@Permission("crazyspawner.modifyentity")
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazyspawner.modifyentity");
	}
}
