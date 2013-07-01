package de.st_ddt.crazyspawner.commands;

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
	@Permission("crazyspawner.mountme.others")
	@Localized({ "CRAZYSPAWNER.MOUNTME.SELF $EntityType$", "CRAZYSPAWNER.MOUNTME.OTHER $EntityType$ $MountedPlayer$", "CRAZYSPAWNER.MOUNTME.MOUNTED $EntityType$ $Sender$" })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
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
		ChatHelperExtended.readParameters(args, params, playerParam);
		if (entityParam.getValue() == null)
			throw new CrazyCommandUsageException("<entity:NamedEntityType> [player:Player]");
		final Player player = playerParam.getValue();
		if (player == null)
			throw new CrazyCommandUsageException("<entity:NamedEntityType> <player:Player>");
		Entity entity = entityParam.getValue().spawn(player.getLocation());
		while (entity.getPassenger() != null)
			entity = entity.getPassenger();
		entity.setPassenger(player);
		if (player == sender)
			plugin.sendLocaleMessage("COMMAND.MOUNTME.SELF", sender, entityParam.getValue().getType().name());
		else if (PermissionModule.hasPermission(sender, "crazyspawner.mountme.others"))
		{
			plugin.sendLocaleMessage("COMMAND.MOUNTME.OTHER", sender, entityParam.getValue().getType().name(), player.getName());
			plugin.sendLocaleMessage("COMMAND.MOUNTME.MOUNTED", sender, entityParam.getValue().getType().name(), sender.getName());
		}
		else
			throw new CrazyCommandPermissionException();
	}

	@Override
	@Permission("crazyspawner.mountme")
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazyspawner.mountme");
	}
}
