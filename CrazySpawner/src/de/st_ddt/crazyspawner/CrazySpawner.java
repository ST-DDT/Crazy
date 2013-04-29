package de.st_ddt.crazyspawner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffectType;

import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.data.ParameterData;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.commands.CommandCreatureSpawner;
import de.st_ddt.crazyspawner.commands.CommandKill;
import de.st_ddt.crazyspawner.commands.CommandSpawn;
import de.st_ddt.crazyspawner.commands.CommandSpawnList;
import de.st_ddt.crazyspawner.commands.CommandSpawnRemove;
import de.st_ddt.crazyspawner.commands.CommandTheEndAutoRespawn;
import de.st_ddt.crazyspawner.data.CustomCreature;
import de.st_ddt.crazyspawner.data.CustomCreature_1_4_5;
import de.st_ddt.crazyspawner.data.CustomCreature_1_4_6;
import de.st_ddt.crazyspawner.data.CustomCreature_1_5;
import de.st_ddt.crazyspawner.data.options.Thunder;
import de.st_ddt.crazyspawner.listener.CreatureListener;
import de.st_ddt.crazyspawner.listener.PlayerListener;
import de.st_ddt.crazyspawner.tasks.SpawnTask;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.CrazyPipe;
import de.st_ddt.crazyutil.ExtendedCreatureType;
import de.st_ddt.crazyutil.VersionComparator;
import de.st_ddt.crazyutil.metrics.Metrics;
import de.st_ddt.crazyutil.metrics.Metrics.Graph;
import de.st_ddt.crazyutil.metrics.Metrics.Plotter;
import de.st_ddt.crazyutil.modes.BooleanFalseMode;
import de.st_ddt.crazyutil.modes.DoubleMode;
import de.st_ddt.crazyutil.paramitrisable.CreatureParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.EnumParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.ExtendedCreatureParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.LocationParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.LocalizedVariable;

@LocalizedVariable(variables = { "CRAZYPLUGIN" }, values = { "CRAZYSPAWNER" })
public class CrazySpawner extends CrazyPlugin
{

	protected final static boolean v146OrLater = VersionComparator.compareVersions(ChatHelper.getMinecraftVersion(), "1.4.6") >= 0;
	protected final static boolean v15OrLater = VersionComparator.compareVersions(ChatHelper.getMinecraftVersion(), "1.5") >= 0;
	private static CrazySpawner plugin;
	protected final Set<CustomCreature> creatures = new LinkedHashSet<CustomCreature>();
	protected final Set<SpawnTask> tasks = new TreeSet<SpawnTask>();
	protected final Map<Player, EntityType> creatureSelection = new HashMap<Player, EntityType>();
	protected double defaultAlarmRange;
	protected boolean monsterExplosionDamageEnabled;
	static
	{
		CrazyPipe.registerPipe(new CrazyPipe()
		{

			@Override
			public void execute(final CommandSender sender, final Collection<? extends ParameterData> datas, final String... pipeArgs) throws CrazyException
			{
				for (final ParameterData data : datas)
					strikeTarget(sender, data, ChatHelper.putArgsPara(sender, pipeArgs, data));
			}

			private void strikeTarget(final CommandSender sender, final ParameterData data, final String[] pipeArgs) throws CrazyException
			{
				final Map<String, Paramitrisable> params = new HashMap<String, Paramitrisable>();
				final LocationParamitrisable location = new LocationParamitrisable(sender);
				location.addFullParams(params, "", "l", "loc", "location");
				ChatHelperExtended.readParameters(pipeArgs, params, location);
				if (location.getValue() == null)
					throw new CrazyCommandUsageException("[World] <X> <Y> <Z>");
				if (location.getValue().getWorld() == null)
					throw new CrazyCommandUsageException("<World> <X> <Y> <Z>");
				location.getValue().getWorld().strikeLightning(location.getValue());
			}
		}, "thunder", "strike");
	}

	public static CrazySpawner getPlugin()
	{
		return plugin;
	}

	public CrazySpawner()
	{
		super();
		registerModes();
	}

	@Localized("CRAZYSPAWNER.MODE.CHANGE $Name$ $Value$")
	private void registerModes()
	{
		modeCommand.addMode(new DoubleMode(this, "defaultAlarmRange")
		{

			@Override
			public Double getValue()
			{
				return defaultAlarmRange;
			}

			@Override
			public void setValue(final Double newValue) throws CrazyException
			{
				defaultAlarmRange = newValue;
				saveConfiguration();
			}
		});
		modeCommand.addMode(new BooleanFalseMode(this, "monsterExplosionDamageEnabled")
		{

			@Override
			public Boolean getValue()
			{
				return monsterExplosionDamageEnabled;
			}

			@Override
			public void setValue(final Boolean newValue) throws CrazyException
			{
				monsterExplosionDamageEnabled = newValue;
				saveConfiguration();
			}
		});
	}

	private void registerHooks()
	{
		final PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerListener(this, creatureSelection), this);
		pm.registerEvents(new CreatureListener(this), this);
	}

	private void registerCommands()
	{
		getCommand("crazyspawn").setExecutor(new CommandSpawn(this));
		getCommand("crazykill").setExecutor(new CommandKill(this));
		getCommand("crazycreaturespawner").setExecutor(new CommandCreatureSpawner(this, creatureSelection));
		getCommand("crazytheendautorespawn").setExecutor(new CommandTheEndAutoRespawn(this));
		mainCommand.addSubCommand(new CommandSpawnList(this), "l", "list");
		mainCommand.addSubCommand(new CommandSpawnRemove(this), "rem", "remove");
	}

	private void registerMetrics()
	{
		final boolean metricsEnabled = getConfig().getBoolean("metrics.enabled", true);
		getConfig().set("metrics.enabled", metricsEnabled);
		if (!metricsEnabled)
			return;
		try
		{
			final Metrics metrics = new Metrics(this);
			final Graph pluginStats = metrics.createGraph("Plugin stats");
			pluginStats.addPlotter(new Plotter("CustomCreatures")
			{

				@Override
				public int getValue()
				{
					return creatures.size();
				}
			});
			pluginStats.addPlotter(new Plotter("SpawnTasks")
			{

				@Override
				public int getValue()
				{
					return tasks.size();
				}
			});
			final Graph creatureCount = metrics.createGraph("Custom creatures");
			for (final EntityType type : CreatureParamitrisable.CREATURE_TYPES)
				creatureCount.addPlotter(new Plotter(type.getName())
				{

					@Override
					public int getValue()
					{
						int i = 0;
						for (final CustomCreature creature : creatures)
							if (creature.getType() == type)
								i++;
						return i;
					}
				});
			metrics.start();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
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
		registerHooks();
		super.onEnable();
		if (isUpdated)
		{
			saveExamples();
			if (VersionComparator.compareVersions(previousVersion, "3.7") == -1)
			{
				// DefaultCreatures
				// - Spider_Skeleton
				final CustomCreature spiderSkeleton = new CustomCreature_1_4_5("Spider_Skeleton", EntityType.SPIDER, "SKELETON");
				creatures.add(spiderSkeleton);
				ExtendedCreatureParamitrisable.registerExtendedEntityType(spiderSkeleton);
				// - Zombie_Skeleton
				final CustomCreature spiderZombie = new CustomCreature_1_4_5("Spider_Zombie", EntityType.SPIDER, "ZOMBIE");
				creatures.add(spiderZombie);
				ExtendedCreatureParamitrisable.registerExtendedEntityType(spiderZombie);
				// - Diamont_Zombie
				final CustomCreature diamondZombie = new CustomCreature_1_4_5("Diamont_Zombie", EntityType.ZOMBIE, new ItemStack(Material.DIAMOND_BOOTS), 0.01F, new ItemStack(Material.DIAMOND_LEGGINGS), 0.01F, new ItemStack(Material.DIAMOND_CHESTPLATE), 0.01F, new ItemStack(Material.DIAMOND_HELMET), 0.01F, new ItemStack(Material.DIAMOND_SWORD), 0.01F);
				creatures.add(diamondZombie);
				ExtendedCreatureParamitrisable.registerExtendedEntityType(diamondZombie);
				// - Healthy_Diamont_Zombie
				final ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
				chestplate.setAmount(1);
				chestplate.setDurability((short) 3);
				chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 5);
				chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 5);
				chestplate.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
				if (v146OrLater)
					chestplate.addUnsafeEnchantment(Enchantment.THORNS, 3);
				final ItemMeta meta = chestplate.getItemMeta();
				meta.setDisplayName("Holy Chestplate of the Goddes");
				final List<String> lore = new ArrayList<String>();
				lore.add("Blessed by the goddess kiss");
				lore.add("Manufactured by the best dwarfs known");
				meta.setLore(lore);
				chestplate.setItemMeta(meta);
				final CustomCreature healthyDiamondZombie;
				if (v15OrLater)
					healthyDiamondZombie = new CustomCreature_1_5("Healthy_Diamont_Zombie", ChatColor.AQUA + "Diamond_Zombie", true, EntityType.ZOMBIE, 100, new ItemStack(Material.DIAMOND_BOOTS), 1F, new ItemStack(Material.DIAMOND_LEGGINGS), 1F, chestplate, 1F, new ItemStack(Material.DIAMOND_HELMET), 1F, new ItemStack(Material.DIAMOND_SWORD), 1F);
				else if (v146OrLater)
					healthyDiamondZombie = new CustomCreature_1_4_6("Healthy_Diamont_Zombie", EntityType.ZOMBIE, 100, new ItemStack(Material.DIAMOND_BOOTS), 1F, new ItemStack(Material.DIAMOND_LEGGINGS), 1F, chestplate, 1F, new ItemStack(Material.DIAMOND_HELMET), 1F, new ItemStack(Material.DIAMOND_SWORD), 1F);
				else
					healthyDiamondZombie = new CustomCreature_1_4_5("Healthy_Diamont_Zombie", EntityType.ZOMBIE, new ItemStack(Material.DIAMOND_BOOTS), 1F, new ItemStack(Material.DIAMOND_LEGGINGS), 1F, chestplate, 1F, new ItemStack(Material.DIAMOND_HELMET), 1F, new ItemStack(Material.DIAMOND_SWORD), 1F);
				creatures.add(healthyDiamondZombie);
				ExtendedCreatureParamitrisable.registerExtendedEntityType(healthyDiamondZombie);
				// - Giant
				final CustomCreature giant = new CustomCreature_1_4_5("Giant", EntityType.GIANT);
				creatures.add(giant);
				ExtendedCreatureParamitrisable.registerExtendedEntityType(giant);
				// - Healthy_Giant
				if (v146OrLater)
				{
					final CustomCreature healthyGiant = new CustomCreature_1_4_6("Healthy_Giant", EntityType.GIANT, 200);
					creatures.add(healthyGiant);
					ExtendedCreatureParamitrisable.registerExtendedEntityType(healthyGiant);
				}
				// - Spider_Diamont_Zombie
				final CustomCreature spiderDiamondZombie = new CustomCreature_1_4_5("Spider_Diamont_Zombie", EntityType.SPIDER, diamondZombie);
				creatures.add(spiderDiamondZombie);
				ExtendedCreatureParamitrisable.registerExtendedEntityType(spiderDiamondZombie);
				saveConfiguration();
			}
			if (VersionComparator.compareVersions(previousVersion, "3.11") == -1)
			{
				// - Speedy_Baby_Zombie
				final Map<PotionEffectType, Integer> potions = new HashMap<PotionEffectType, Integer>();
				potions.put(PotionEffectType.SPEED, 5);
				final CustomCreature speedyZombie = new CustomCreature_1_4_5("Speedy_Baby_Zombie", EntityType.ZOMBIE, true, false, false, false, null, 0, false, false, null, null, 0, null, 0, null, 0, null, 0, null, 0, null, potions);
				creatures.add(speedyZombie);
				ExtendedCreatureParamitrisable.registerExtendedEntityType(speedyZombie);
				saveConfiguration();
			}
			if (VersionComparator.compareVersions(previousVersion, "3.11.1") == -1)
			{
				getConfig().set("example", null);
				saveConfiguration();
			}
		}
		registerCommands();
		registerMetrics();
		sendLocaleMessage("CREATURES.AVAILABLE", Bukkit.getConsoleSender(), ExtendedCreatureParamitrisable.CREATURE_TYPES.size());
	}

	private void saveExamples()
	{
		final File exampleFolder = new File(getDataFolder(), "example");
		exampleFolder.mkdirs();
		// ExampleCreature
		final YamlConfiguration creature = new YamlConfiguration();
		creature.options().header("CrazySpawner example Creature.yml\n" + "For more information visit\n" + "https://github.com/ST-DDT/Crazy/blob/master/CrazySpawner/docs/example/Creature.yml\n" + "Custom creatures have to be defined inside config.yml");
		CustomCreature_1_4_5.dummySave(creature, "exampleCreature.");
		try
		{
			creature.save(new File(exampleFolder, "Creature.yml"));
		}
		catch (final IOException e)
		{
			System.err.println("[CrazySpawner] Could not save example Creature.yml.");
			System.err.println(e.getMessage());
		}
		// ExampleType
		final YamlConfiguration entityTypes = new YamlConfiguration();
		entityTypes.set("exampleEntityType", CreatureParamitrisable.CREATURE_NAMES);
		try
		{
			entityTypes.save(new File(exampleFolder, "EntityType.yml"));
		}
		catch (final IOException e)
		{
			System.err.println("[CrazySpawner] Could not save example EntityType.yml.");
			System.err.println(e.getMessage());
		}
		// ExampleColor
		final YamlConfiguration dyeColors = new YamlConfiguration();
		dyeColors.set("exampleDyeColor", EnumParamitrisable.getEnumNames(DyeColor.values()).toArray());
		try
		{
			dyeColors.save(new File(exampleFolder, "DyeColor.yml"));
		}
		catch (final IOException e)
		{
			System.err.println("[CrazySpawner] Could not save example DyeColor.yml.");
			System.err.println(e.getMessage());
		}
		// ExampleItem
		final YamlConfiguration item = new YamlConfiguration();
		item.options().header("CrazySpawner example Item.yml\n" + "For more information visit\n" + "https://github.com/ST-DDT/Crazy/blob/master/CrazySpawner/docs/example/Item.yml\n" + "Items have to be defined inside config.yml (in the custom creature inventory slots)");
		item.set("exampleItem.type", "Material");
		item.set("exampleItem.damage", "short (0 (full) - 528 (broken, upper limit may differ (mostly below)))");
		item.set("exampleItem.amount", "int (1-64)");
		item.set("exampleItem.meta.==", "ItemMeta");
		item.set("exampleItem.meta.meta-type", "UNSPECIFIC");
		item.set("exampleItem.meta.display-name", "String");
		item.set("exampleItem.meta.lore", new String[] { "Line1", "Line2", "..." });
		item.set("exampleItem.meta.enchants.ENCHANTMENT1", "int (1-255)");
		item.set("exampleItem.meta.enchants.ENCHANTMENT2", "int (1-255)");
		item.set("exampleItem.meta.enchants.ENCHANTMENTx", "int (1-255)");
		try
		{
			item.save(new File(exampleFolder, "Item.yml"));
		}
		catch (final IOException e)
		{
			System.err.println("[CrazySpawner] Could not save example Item.yml.");
			System.err.println(e.getMessage());
		}
		// ExampleEnchantment
		final YamlConfiguration enchantments = new YamlConfiguration();
		final List<String> exampleEnchantments = new ArrayList<String>();
		for (final Enchantment enchantment : Enchantment.values())
			if (enchantment != null)
				exampleEnchantments.add(enchantment.getName());
		enchantments.set("exampleEnchantment", exampleEnchantments);
		try
		{
			enchantments.save(new File(exampleFolder, "Enchantment.yml"));
		}
		catch (final IOException e)
		{
			System.err.println("[CrazySpawner] Could not save example Enchantment.yml.");
			System.err.println(e.getMessage());
		}
		// ExamplePotionEffect
		final YamlConfiguration potionEffects = new YamlConfiguration();
		final List<String> examplePotionEffects = new ArrayList<String>();
		for (final PotionEffectType potionEffect : PotionEffectType.values())
			if (potionEffect != null)
				examplePotionEffects.add(potionEffect.getName());
		potionEffects.set("examplePotionEffect", examplePotionEffects);
		try
		{
			potionEffects.save(new File(exampleFolder, "PotionEffect.yml"));
		}
		catch (final IOException e)
		{
			System.err.println("[CrazySpawner] Could not save example PotionEffect.yml.");
			System.err.println(e.getMessage());
		}
		// ExampleThunder
		final YamlConfiguration thunder = new YamlConfiguration();
		thunder.set("exampleThunder", EnumParamitrisable.getEnumNames(Thunder.values()).toArray());
		try
		{
			thunder.save(new File(exampleFolder, "Thunder.yml"));
		}
		catch (final IOException e)
		{
			System.err.println("[CrazySpawner] Could not save example Thunder.yml.");
			System.err.println(e.getMessage());
		}
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
					final CustomCreature creature;
					if (v15OrLater)
						creature = new CustomCreature_1_5(creatureConfig.getConfigurationSection(key));
					else if (v146OrLater)
						creature = new CustomCreature_1_4_6(creatureConfig.getConfigurationSection(key));
					else
						creature = new CustomCreature_1_4_5(creatureConfig.getConfigurationSection(key));
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
		defaultAlarmRange = config.getDouble("defaultAlarmRange", 10);
		monsterExplosionDamageEnabled = config.getBoolean("monsterExplosionDamageEnabled", true);
	}

	@Override
	public void saveConfiguration()
	{
		final ConfigurationSection config = getConfig();
		if (creatures.size() == 0)
			config.set("creatures", new HashMap<String, Object>(0));
		else
		{
			config.set("creatures", null);
			for (final CustomCreature creature : creatures)
				creature.save(config, "creatures." + creature.getName() + ".");
		}
		if (tasks.size() == 0)
			config.set("tasks", new HashMap<String, Object>(0));
		else
		{
			config.set("tasks", null);
			int i = 0;
			for (final SpawnTask task : tasks)
				task.save(config, "tasks.t" + i++ + ".");
		}
		config.set("defaultAlarmRange", defaultAlarmRange);
		config.set("monsterExplosionDamageEnabled", monsterExplosionDamageEnabled);
		super.saveConfiguration();
	}

	public void addCustomCreature(final CustomCreature creature)
	{
		creatures.add(creature);
		ExtendedCreatureParamitrisable.registerExtendedEntityType(creature);
		saveConfiguration();
	}

	public Set<CustomCreature> getCreatures()
	{
		return creatures;
	}

	public void removeCustomCreature(final CustomCreature creature)
	{
		creatures.remove(creature);
		saveConfiguration();
	}

	public void addSpawnTask(final SpawnTask task)
	{
		tasks.add(task);
		saveConfiguration();
	}

	public Set<SpawnTask> getTasks()
	{
		return tasks;
	}

	public void removeSpawnTask(final SpawnTask task)
	{
		tasks.remove(task);
		saveConfiguration();
	}

	public final double getDefaultAlarmRange()
	{
		return defaultAlarmRange;
	}

	public final boolean isMonsterExplosionDamageEnabled()
	{
		return monsterExplosionDamageEnabled;
	}
}
