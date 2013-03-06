package de.st_ddt.crazyspawner;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyspawner.commands.CommandCreatureSpawner;
import de.st_ddt.crazyspawner.commands.CommandKill;
import de.st_ddt.crazyspawner.commands.CommandSpawn;
import de.st_ddt.crazyspawner.commands.CommandTheEndAutoRespawn;
import de.st_ddt.crazyspawner.data.CustomCreature;
import de.st_ddt.crazyspawner.listener.PlayerListener;
import de.st_ddt.crazyspawner.tasks.SpawnTask;
import de.st_ddt.crazyutil.ExtendedCreatureType;
import de.st_ddt.crazyutil.VersionComparator;
import de.st_ddt.crazyutil.paramitrisable.ExtendedCreatureParamitrisable;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.LocalizedVariable;

@LocalizedVariable(variables = { "CRAZYPLUGIN" }, values = { "CRAZYSPAWNER" })
public class CrazySpawner extends CrazyPlugin
{

	private static CrazySpawner plugin;
	private final Set<CustomCreature> creatures = new LinkedHashSet<CustomCreature>();
	private final Set<SpawnTask> tasks = new TreeSet<SpawnTask>();
	private final Map<Player, EntityType> creatureSelection = new HashMap<Player, EntityType>();

	public static CrazySpawner getPlugin()
	{
		return plugin;
	}

	public void registerHooks()
	{
		final PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerListener(this, creatureSelection), this);
	}

	private void registerCommands()
	{
		getCommand("crazyspawn").setExecutor(new CommandSpawn(this));
		getCommand("crazykill").setExecutor(new CommandKill(this));
		getCommand("crazycreaturespawner").setExecutor(new CommandCreatureSpawner(this, creatureSelection));
		getCommand("crazytheendautorespawn").setExecutor(new CommandTheEndAutoRespawn(this));
	}

	@Override
	public void onLoad()
	{
		plugin = this;
		super.onLoad();
	}

	@Override
	@Localized("CRAZYSPAWNER.CREATURES.AVAILABLE $Count$")
	public void onEnable()
	{
		registerEnderCrystalType();
		super.onEnable();
		if (isUpdated)
			if (VersionComparator.compareVersions(previousVersion, "3.7") == -1)
			{
				CustomCreature.dummySave(getConfig(), "example.creature.");
				final CustomCreature spiderSkeleton = new CustomCreature("Spider_Skeleton", EntityType.SPIDER, "SKELETON");
				creatures.add(spiderSkeleton);
				ExtendedCreatureParamitrisable.registerExtendedEntityType(spiderSkeleton);
				final CustomCreature diamondZombie = new CustomCreature("Diamont_Zombie", EntityType.ZOMBIE, new ItemStack(Material.DIAMOND_BOOTS), 0.01F, new ItemStack(Material.DIAMOND_LEGGINGS), 0.01F, new ItemStack(Material.DIAMOND_CHESTPLATE), 0.01F, new ItemStack(Material.DIAMOND_HELMET), 0.01F, new ItemStack(Material.DIAMOND_SWORD), 0.01F);
				creatures.add(diamondZombie);
				ExtendedCreatureParamitrisable.registerExtendedEntityType(diamondZombie);
				final CustomCreature spiderDiamondZombie = new CustomCreature("Spider_Diamont_Zombie", EntityType.SPIDER, diamondZombie);
				creatures.add(spiderDiamondZombie);
				ExtendedCreatureParamitrisable.registerExtendedEntityType(spiderDiamondZombie);
			}
		registerHooks();
		registerCommands();
		sendLocaleMessage("CREATURES.AVAILABLE", Bukkit.getConsoleSender(), ExtendedCreatureParamitrisable.CREATURE_TYPES.size());
	}

	private void registerEnderCrystalType()
	{
		ExtendedCreatureParamitrisable.registerExtendedEntityType(new ExtendedCreatureType()
		{

			@Override
			public String getName()
			{
				return "ENDER_CRYSTAL";
			}

			@Override
			public EntityType getType()
			{
				return EntityType.ENDER_CRYSTAL;
			}

			@Override
			public Entity spawn(final Location location)
			{
				if (location.getChunk().isLoaded())
				{
					location.setX(Math.floor(location.getX()) + 0.5);
					location.setY(Math.floor(location.getY()));
					location.setZ(Math.floor(location.getZ()) + 0.5);
					location.setYaw(0);
					location.setPitch(0);
					location.clone().add(0, 1, 0).getBlock().setType(Material.FIRE);
					location.getBlock().setType(Material.BEDROCK);
					return location.getWorld().spawnEntity(location, EntityType.ENDER_CRYSTAL);
				}
				else
					return null;
			}

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				return world.getEntitiesByClass(EntityType.ENDER_CRYSTAL.getEntityClass());
			}

			@Override
			public String toString()
			{
				return getName();
			}
		}, "DRAGON_CRYSTAL", "HEAL_CRYSTAL", "ENDERCRYSTAL");
	}

	@Override
	@Localized("CRAZYSPAWNER.CREATURES.LOADED $Count$")
	public void loadConfiguration()
	{
		final ConfigurationSection config = getConfig();
		creatures.clear();
		final ConfigurationSection creatureConfig = config.getConfigurationSection("creatures");
		if (creatureConfig != null)
			for (final String key : creatureConfig.getKeys(false))
				try
				{
					final CustomCreature creature = new CustomCreature(creatureConfig.getConfigurationSection(key));
					creatures.add(creature);
					ExtendedCreatureParamitrisable.registerExtendedEntityType(creature);
				}
				catch (final IllegalArgumentException e)
				{
					System.err.println("Could not load creature " + key);
					System.err.println(e.getMessage());
				}
		sendLocaleMessage("CREATURES.LOADED", Bukkit.getConsoleSender(), creatures.size());
		for (final SpawnTask task : tasks)
			task.cancel();
		tasks.clear();
		final ConfigurationSection taskConfig = config.getConfigurationSection("tasks");
		if (taskConfig != null)
			for (final String key : taskConfig.getKeys(false))
				try
				{
					tasks.add(new SpawnTask(plugin, taskConfig.getConfigurationSection(key)));
				}
				catch (final IllegalArgumentException e)
				{
					e.printStackTrace();
				}
		for (final SpawnTask task : tasks)
			task.start(100);
	}

	@Override
	public void saveConfiguration()
	{
		final ConfigurationSection config = getConfig();
		config.set("creatures", null);
		for (final CustomCreature creature : creatures)
			creature.save(config, "creatures." + creature.getName() + ".");
		config.set("tasks", null);
		int i = 0;
		for (final SpawnTask task : tasks)
			task.save(config, "tasks.t" + i++ + ".");
		super.saveConfiguration();
	}

	public void addSpawnTask(final SpawnTask task)
	{
		tasks.add(task);
		saveConfiguration();
	}

	public void removeSpawnTask(final SpawnTask task)
	{
		tasks.remove(task);
		saveConfiguration();
	}
}
