package de.st_ddt.crazyspawner.commands;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandExecutorException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.Tabbed;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.CreatureParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;

public class CrazySpawnerCommandCreatureSpawner extends CrazySpawnerCommandExecutor
{

	private final Map<String, EntityType> creatureSelection;

	public CrazySpawnerCommandCreatureSpawner(final CrazySpawner plugin, final Map<String, EntityType> creatureSelection)
	{
		super(plugin);
		this.creatureSelection = creatureSelection;
	}

	@Override
	@Localized("CRAZYSPAWNER.COMMAND.CREATURESPAWNER.SELECTED $Creature$")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!(sender instanceof Player))
			throw new CrazyCommandExecutorException(false);
		final Map<String, Paramitrisable> params = new TreeMap<String, Paramitrisable>();
		final CreatureParamitrisable creature = new CreatureParamitrisable(null);
		params.put("c", creature);
		params.put("creature", creature);
		ChatHelperExtended.readParameters(args, params, creature);
		if (creature.getValue() == null)
			throw new CrazyCommandUsageException("<Creature>");
		creatureSelection.put(sender.getName(), creature.getValue());
		plugin.sendLocaleMessage("COMMAND.CREATURESPAWNER.SELECTED", sender, creature.getValue());
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		final Map<String, Tabbed> params = new TreeMap<String, Tabbed>();
		final CreatureParamitrisable creature = new CreatureParamitrisable(null);
		params.put("c", creature);
		params.put("creature", creature);
		return ChatHelperExtended.tabHelp(args, params, creature);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazyspawner.creaturespawner");
	}
}
