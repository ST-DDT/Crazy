package de.st_ddt.crazyspawner.commands;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.NamedEntitySpawner;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.NamedEntitySpawnerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.PlayerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.Permission;

public class CommandMountMe extends CommandExecutor
{

	public CommandMountMe(final CrazySpawner plugin)
	{
		super(plugin);
	}

	@Override
	@Permission({ "crazyspawner.mountme.self", "crazyspawner.mountme.others", "crazyspawner.mountme.*", "crazyspawner.mountme.<ENTITYTYPE>.*", "crazyspawner.mountme.<CUSTOMENTITYNAME>" })
	@Localized({ "CRAZYSPAWNER.COMMAND.MOUNTME.SELF $EntityType$", "CRAZYSPAWNER.COMMAND.MOUNTME.OTHER $EntityType$ $MountedPlayer$", "CRAZYSPAWNER.COMMAND.MOUNTME.MOUNTED $EntityType$ $Sender$" })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		final Map<String, TabbedParamitrisable> params = new TreeMap<String, TabbedParamitrisable>();
		final NamedEntitySpawnerParamitrisable spawnerParam = new NamedEntitySpawnerParamitrisable((NamedEntitySpawner) null);
		params.put("c", spawnerParam);
		params.put("creature", spawnerParam);
		params.put("e", spawnerParam);
		params.put("entity", spawnerParam);
		final PlayerParamitrisable playerParam = new PlayerParamitrisable(sender);
		params.put("p", playerParam);
		params.put("plr", playerParam);
		params.put("player", playerParam);
		ChatHelperExtended.readParameters(args, params, spawnerParam, playerParam);
		final NamedEntitySpawner spawner = spawnerParam.getValue();
		if (spawner == null)
			throw new CrazyCommandUsageException("<entity:NamedEntityType> [player:Player]");
		final Player player = playerParam.getValue();
		if (player == null)
			throw new CrazyCommandUsageException("<entity:NamedEntityType> <player:Player>");
		if (!PermissionModule.hasPermission(sender, "crazyspawner.mountme." + (player == sender ? "self" : "others")))
			throw new CrazyCommandPermissionException();
		if (!(PermissionModule.hasPermission(sender, "crazyspawner.mountme.*") || PermissionModule.hasPermission(sender, "crazyspawner.mountme." + spawner.getType().name() + ".*") || PermissionModule.hasPermission(sender, "crazyspawner.mountme." + spawner.getName())))
			throw new CrazyCommandPermissionException();
		Entity entity = spawner.spawn(player.getLocation());
		while (entity.getPassenger() != null)
			entity = entity.getPassenger();
		entity.setPassenger(player);
		if (player == sender)
			plugin.sendLocaleMessage("COMMAND.MOUNTME.SELF", sender, spawnerParam.getValue().getType().name());
		else
		{
			plugin.sendLocaleMessage("COMMAND.MOUNTME.OTHER", sender, spawnerParam.getValue().getType().name(), player.getName());
			plugin.sendLocaleMessage("COMMAND.MOUNTME.MOUNTED", player, spawnerParam.getValue().getType().name(), sender.getName());
		}
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		final Map<String, TabbedParamitrisable> params = new TreeMap<String, TabbedParamitrisable>();
		final NamedEntitySpawnerParamitrisable entityParam = new NamedEntitySpawnerParamitrisable((NamedEntitySpawner) null);
		params.put("c", entityParam);
		params.put("creature", entityParam);
		params.put("e", entityParam);
		params.put("entity", entityParam);
		final PlayerParamitrisable playerParam = new PlayerParamitrisable(sender);
		params.put("p", playerParam);
		params.put("plr", playerParam);
		params.put("player", playerParam);
		return ChatHelperExtended.tabHelp(args, params, entityParam, playerParam);
	}

	@Override
	@Permission("crazyspawner.mountme")
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazyspawner.mountme");
	}
}
