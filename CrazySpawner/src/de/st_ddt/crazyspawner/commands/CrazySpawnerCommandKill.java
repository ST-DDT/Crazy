package de.st_ddt.crazyspawner.commands;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Animals;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Golem;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wither;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.VersionComparator;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.DoubleParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.LocationParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;

public class CrazySpawnerCommandKill extends CrazySpawnerCommandExecutor
{

	private final boolean v142OrLater;

	public CrazySpawnerCommandKill(final CrazySpawner plugin)
	{
		super(plugin);
		final String mcVersion = Bukkit.getVersion().split("-", 4)[2];
		v142OrLater = (VersionComparator.compareVersions(mcVersion, "1.4.2") >= 0);
	}

	@Override
	@Localized({ "CRAZYSPAWNER.COMMAND.KILLED.MONSTERS $Amount$", "CRAZYSPAWNER.COMMAND.KILLED.ANIMALS $Amount$", "CRAZYSPAWNER.COMMAND.KILLED.GOLEMS $Amount$", "CRAZYSPAWNER.COMMAND.KILLED.VILLAGER $Amount$", "CRAZYSPAWNER.COMMAND.KILLED.BOSSES $Amount$" })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!sender.hasPermission("crazyspawner.kill"))
			throw new CrazyCommandPermissionException();
		final Map<String, Paramitrisable> params = new TreeMap<String, Paramitrisable>();
		final DoubleParamitrisable range = new DoubleParamitrisable(16D);
		params.put("", range);
		params.put("r", range);
		params.put("range", range);
		final LocationParamitrisable location = new LocationParamitrisable(sender);
		location.addFullParams(params, "l", "loc", "location");
		final BooleanParamitrisable monster = new BooleanParamitrisable(true);
		params.put("m", monster);
		params.put("monster", monster);
		final BooleanParamitrisable animals = new BooleanParamitrisable(false);
		params.put("a", animals);
		params.put("animals", animals);
		final BooleanParamitrisable golems = new BooleanParamitrisable(false);
		params.put("g", golems);
		params.put("golems", golems);
		final BooleanParamitrisable villager = new BooleanParamitrisable(false);
		params.put("v", villager);
		params.put("villager", villager);
		final BooleanParamitrisable bosses = new BooleanParamitrisable(false);
		params.put("b", bosses);
		params.put("bosses", bosses);
		ChatHelperExtended.readParameters(args, params);
		final World world = location.getValue().getWorld();
		if (monster.getValue())
			plugin.sendLocaleMessage("COMMAND.KILLED.MONSTERS", sender, killEntities(world.getEntitiesByClasses(Monster.class, Slime.class), location.getValue(), range.getValue()));
		if (animals.getValue())
			plugin.sendLocaleMessage("COMMAND.KILLED.ANIMALS", sender, killEntities(world.getEntitiesByClasses(Animals.class, Squid.class), location.getValue(), range.getValue()));
		if (animals.getValue())
			plugin.sendLocaleMessage("COMMAND.KILLED.GOLEMS", sender, killEntities(world.getEntitiesByClasses(Golem.class), location.getValue(), range.getValue()));
		if (animals.getValue())
			plugin.sendLocaleMessage("COMMAND.KILLED.VILLAGER", sender, killEntities(world.getEntitiesByClasses(Villager.class), location.getValue(), range.getValue()));
		if (bosses.getValue())
			if (v142OrLater)
				plugin.sendLocaleMessage("COMMAND.KILLED.BOSSES", sender, killEntities(world.getEntitiesByClasses(EnderDragon.class, Wither.class), location.getValue(), range.getValue()));
			else
				plugin.sendLocaleMessage("COMMAND.KILLED.BOSSES", sender, killEntities(world.getEntitiesByClasses(EnderDragon.class), location.getValue(), range.getValue()));
	}

	public int killEntities(final Collection<Entity> entities, final Location location, final double range)
	{
		int res = 0;
		for (final Entity entity : entities)
			if (location.distance(entity.getLocation()) < range)
			{
				entity.remove();
				res++;
			}
		return res;
	}
}
