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
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
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
import de.st_ddt.crazyspawner.entities.CustomEntitySpawner;
import de.st_ddt.crazyspawner.entities.properties.EquipmentProperties;
import de.st_ddt.crazyspawner.entities.properties.PotionProterty;
import de.st_ddt.crazyspawner.listener.CreatureListener;
import de.st_ddt.crazyspawner.listener.PlayerListener;
import de.st_ddt.crazyspawner.tasks.SpawnTask;
import de.st_ddt.crazyspawner.tasks.options.Thunder;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.CrazyPipe;
import de.st_ddt.crazyutil.Drop;
import de.st_ddt.crazyutil.NamedEntitySpawner;
import de.st_ddt.crazyutil.VersionComparator;
import de.st_ddt.crazyutil.metrics.Metrics;
import de.st_ddt.crazyutil.metrics.Metrics.Graph;
import de.st_ddt.crazyutil.metrics.Metrics.Plotter;
import de.st_ddt.crazyutil.modes.BooleanFalseMode;
import de.st_ddt.crazyutil.modes.DoubleMode;
import de.st_ddt.crazyutil.paramitrisable.CreatureParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.EnumParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.LocationParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.NamedEntitySpawnerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.LocalizedVariable;

@LocalizedVariable(variables = { "CRAZYPLUGIN" }, values = { "CRAZYSPAWNER" })
public class CrazySpawner extends CrazyPlugin
{

	protected final static boolean v146OrLater = VersionComparator.compareVersions(ChatHelper.getMinecraftVersion(), "1.4.6") >= 0;
	protected final static boolean v15OrLater = VersionComparator.compareVersions(ChatHelper.getMinecraftVersion(), "1.5") >= 0;
	private static CrazySpawner plugin;
	protected final Set<CustomEntitySpawner> customEntities = new LinkedHashSet<CustomEntitySpawner>();
	protected final CustomEntitySpawner[] overwriteEntities = new CustomEntitySpawner[EntityType.values().length];
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
		pm.registerEvents(new CreatureListener(this, overwriteEntities), this);
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
			pluginStats.addPlotter(new Plotter("CustomEntities")
			{

				@Override
				public int getValue()
				{
					return customEntities.size();
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
			final Graph customEntityCount = metrics.createGraph("Custom Entities");
			for (final EntityType type : CreatureParamitrisable.CREATURE_TYPES)
				customEntityCount.addPlotter(new Plotter(type.getName())
				{

					@Override
					public int getValue()
					{
						int i = 0;
						for (final CustomEntitySpawner customEntity : customEntities)
							if (customEntity.getType() == type)
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
		registerHooks();
		super.onEnable();
		if (isUpdated)
		{
			saveExamples();
			final ConsoleCommandSender console = Bukkit.getConsoleSender();
			if (VersionComparator.compareVersions(previousVersion, "3.7") == -1)
			{
				// DefaultCreatures
				// - Spider_Skeleton
				final CustomEntitySpawner spiderSkeleton = new CustomEntitySpawner("Spider_Skeleton", EntityType.SPIDER, console, "passenger:SKELETON");
				customEntities.add(spiderSkeleton);
				NamedEntitySpawnerParamitrisable.registerNamedEntitySpawner(spiderSkeleton);
				// - Zombie_Skeleton
				final CustomEntitySpawner spiderZombie = new CustomEntitySpawner("Spider_Zombie", EntityType.SPIDER, console, "ZOMBIE");
				customEntities.add(spiderZombie);
				NamedEntitySpawnerParamitrisable.registerNamedEntitySpawner(spiderZombie);
				// - Diamont_Zombie
				final CustomEntitySpawner diamondZombie = new CustomEntitySpawner("Diamont_Zombie", EntityType.ZOMBIE, console, "boots:DIAMOND_BOOTS", "bootsdropchance:0.01", "leggings:DIAMOND_LEGGINGS", "leggingsdropchance:0.01", "chestplate:DIAMOND_CHESTPLATE", "chestplatedropchance:0.01", "helmet:DIAMOND_HELMET", "helmetdropchance:0.01", "iteminhand:DIAMOND_SWORD", "iteminhanddropchance:0.01");
				customEntities.add(diamondZombie);
				NamedEntitySpawnerParamitrisable.registerNamedEntitySpawner(diamondZombie);
				// - Giant
				final CustomEntitySpawner giant = new CustomEntitySpawner("Giant", EntityType.GIANT);
				customEntities.add(giant);
				NamedEntitySpawnerParamitrisable.registerNamedEntitySpawner(giant);
				// - Healthy_Giant
				if (v146OrLater)
				{
					final CustomEntitySpawner healthyGiant = new CustomEntitySpawner("Healthy_Giant", EntityType.GIANT, console, "maxhealth:200");
					customEntities.add(healthyGiant);
					NamedEntitySpawnerParamitrisable.registerNamedEntitySpawner(healthyGiant);
				}
				// - Spider_Diamont_Zombie
				final CustomEntitySpawner spiderDiamondZombie = new CustomEntitySpawner("Spider_Diamont_Zombie", EntityType.SPIDER, console, "passenger:Diamont_Zombie");
				customEntities.add(spiderDiamondZombie);
				NamedEntitySpawnerParamitrisable.registerNamedEntitySpawner(spiderDiamondZombie);
				saveConfiguration();
			}
			if (VersionComparator.compareVersions(previousVersion, "3.11") == -1)
			{
				// - Speedy_Baby_Zombie
				final ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
				boots.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);
				final LeatherArmorMeta meta = (LeatherArmorMeta) boots.getItemMeta();
				meta.setColor(Color.RED);
				boots.setItemMeta(meta);
				final CustomEntitySpawner speedyZombie = new CustomEntitySpawner("Speedy_Baby_Zombie", EntityType.ZOMBIE, console, "baby:true");
				speedyZombie.addEntityProperty(new EquipmentProperties(boots, 0.5F, null, 0, null, 0, null, 0, null, 0, null, null));
				speedyZombie.addEntityProperty(new PotionProterty(new PotionEffectType[] { PotionEffectType.SPEED }, new int[] { 5 }));
				customEntities.add(speedyZombie);
				NamedEntitySpawnerParamitrisable.registerNamedEntitySpawner(speedyZombie);
				saveConfiguration();
			}
			if (VersionComparator.compareVersions(previousVersion, "3.11.1") == -1)
			{
				getConfig().set("example", null);
				saveConfiguration();
			}
			if (VersionComparator.compareVersions(previousVersion, "3.15") == -1)
			{
				// - Healthy_Diamont_Zombie
				final ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
				final ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
				final ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
				chestplate.setDurability((short) 3);
				chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 5);
				chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 5);
				chestplate.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
				if (v146OrLater)
					chestplate.addUnsafeEnchantment(Enchantment.THORNS, 3);
				final ItemMeta meta = chestplate.getItemMeta();
				meta.setDisplayName("Holy Chestplate of the Goddes");
				final List<String> lore = new ArrayList<String>();
				lore.add(ChatColor.RED + "Blessed by the goddess kiss");
				lore.add("Manufactured by the best dwarfs known");
				meta.setLore(lore);
				chestplate.setItemMeta(meta);
				final ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
				final ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
				final List<Drop> drops = new ArrayList<Drop>();
				drops.add(new Drop(new ItemStack(Material.DIAMOND, 2), 0.5F));
				drops.add(new Drop(new ItemStack(Material.DIAMOND, 3), 0.5F));
				drops.add(new Drop(new ItemStack(Material.GOLD_INGOT, 2), 0.5F));
				drops.add(new Drop(new ItemStack(Material.GOLD_INGOT, 3), 0.5F));
				drops.add(new Drop(new ItemStack(Material.EMERALD, 2), 0.5F));
				drops.add(new Drop(boots, 1F));
				drops.add(new Drop(leggings, 1F));
				drops.add(new Drop(chestplate, 1F));
				drops.add(new Drop(helmet, 1F));
				drops.add(new Drop(sword, 1F));
				final CustomEntitySpawner healthyDiamondZombie = new CustomEntitySpawner("Healthy_Diamont_Zombie", EntityType.ZOMBIE, console, "customName:&3Diamond_Zombie", "showNameAboveHead:true", "showHealth:true", "maxHealth:100", "minDamage:3", "maxDamage:7", "minXP:10", "maxXP:20");
				healthyDiamondZombie.addEntityProperty(new EquipmentProperties(boots, 1F, leggings, 1F, chestplate, 1F, helmet, 1F, sword, 1F, drops, false));
				customEntities.add(healthyDiamondZombie);
				NamedEntitySpawnerParamitrisable.registerNamedEntitySpawner(healthyDiamondZombie);
				saveConfiguration();
			}
		}
		registerCommands();
		registerMetrics();
		sendLocaleMessage("CREATURES.AVAILABLE", Bukkit.getConsoleSender(), NamedEntitySpawnerParamitrisable.ENTITY_TYPES.size());
	}

	private void saveExamples()
	{
		final File exampleFolder = new File(getDataFolder(), "example");
		exampleFolder.mkdirs();
		// ExampleCreature
		for (final EntityType type : CustomEntitySpawner.getSpawnableEntityTypes())
		{
			final YamlConfiguration customEntity = new YamlConfiguration();
			customEntity.options().header("CrazySpawner example Entity" + type.name() + ".yml\n" + "For more information visit\n" + "https://github.com/ST-DDT/Crazy/blob/master/CrazySpawner/docs/example/Entity.yml\n" + "Custom entities have to be defined inside config.yml");
			new CustomEntitySpawner(type).dummySave(customEntity, "customEntities.example" + type.name() + ".");
			customEntity.set("customEntities.example" + type.name() + ".type", type.name());
			try
			{
				customEntity.save(new File(exampleFolder, "Entity" + type.name() + ".yml"));
			}
			catch (final IOException e)
			{
				System.err.println("[CrazySpawner] Could not save example Entity.yml.");
				System.err.println(e.getMessage());
			}
		}
		// ExampleType
		final YamlConfiguration entityTypes = new YamlConfiguration();
		entityTypes.set("exampleEntityType", EnumParamitrisable.getEnumNames(CustomEntitySpawner.getSpawnableEntityTypes()));
		try
		{
			entityTypes.save(new File(exampleFolder, "EntityType.yml"));
		}
		catch (final IOException e)
		{
			System.err.println("[CrazySpawner] Could not save example EntityType.yml.");
			System.err.println(e.getMessage());
		}
		// CustomEntityNames
		final YamlConfiguration customTypes = new YamlConfiguration();
		customTypes.set("exampleCustomEntityNames", new ArrayList<String>(NamedEntitySpawnerParamitrisable.ENTITY_TYPES.keySet()));
		try
		{
			customTypes.save(new File(exampleFolder, "CustomEntityNames.yml"));
		}
		catch (final IOException e)
		{
			System.err.println("[CrazySpawner] Could not save example CustomEntityNames.yml.");
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
		// ExampleMaterial
		final YamlConfiguration material = new YamlConfiguration();
		material.set("exampleMaterial", EnumParamitrisable.getEnumNames(Material.values()).toArray());
		try
		{
			material.save(new File(exampleFolder, "Material.yml"));
		}
		catch (final IOException e)
		{
			System.err.println("[CrazySpawner] Could not save example Material.yml.");
			System.err.println(e.getMessage());
		}
		// ExampleProfession
		final YamlConfiguration professions = new YamlConfiguration();
		professions.set("exampleProfession", EnumParamitrisable.getEnumNames(Profession.values()).toArray());
		try
		{
			professions.save(new File(exampleFolder, "Profession.yml"));
		}
		catch (final IOException e)
		{
			System.err.println("[CrazySpawner] Could not save example Profession.yml.");
			System.err.println(e.getMessage());
		}
		// ExampleCatType
		final YamlConfiguration catTypes = new YamlConfiguration();
		catTypes.set("exampleCatType", EnumParamitrisable.getEnumNames(Ocelot.Type.values()).toArray());
		try
		{
			catTypes.save(new File(exampleFolder, "CatType.yml"));
		}
		catch (final IOException e)
		{
			System.err.println("[CrazySpawner] Could not save example CatType.yml.");
			System.err.println(e.getMessage());
		}
		// ExampleSkeletonType
		final YamlConfiguration skeletonType = new YamlConfiguration();
		skeletonType.set("exampleSkeletonType", EnumParamitrisable.getEnumNames(SkeletonType.values()).toArray());
		try
		{
			skeletonType.save(new File(exampleFolder, "SkeletonType.yml"));
		}
		catch (final IOException e)
		{
			System.err.println("[CrazySpawner] Could not save example SkeletonType.yml.");
			System.err.println(e.getMessage());
		}
		// ExampleItem
		final YamlConfiguration item = new YamlConfiguration();
		item.options().header("CrazySpawner example Item.yml\n" + "For more information visit\n" + "https://github.com/ST-DDT/Crazy/blob/master/CrazySpawner/docs/example/Item.yml\n" + "Items have to be defined inside config.yml (in the custom customEntity inventory slots)");
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

	@Override
	@Localized("CRAZYSPAWNER.CREATURES.LOADED $Count$")
	public void loadConfiguration()
	{
		final ConfigurationSection config = getConfig();
		// CustomEntities
		customEntities.clear();
		final ConfigurationSection customEntityConfig = config.getConfigurationSection("customEntities");
		if (customEntityConfig != null)
			for (final String key : customEntityConfig.getKeys(false))
				try
				{
					final CustomEntitySpawner customEntity = new CustomEntitySpawner(customEntityConfig.getConfigurationSection(key));
					customEntities.add(customEntity);
					NamedEntitySpawnerParamitrisable.registerNamedEntitySpawner(customEntity);
				}
				catch (final IllegalArgumentException e)
				{
					System.err.println("Could not load customEntity " + key);
					System.err.println(e.getMessage());
				}
		sendLocaleMessage("CREATURES.LOADED", Bukkit.getConsoleSender(), customEntities.size());
		// OverwriteEntities
		for (final EntityType type : EntityType.values())
			if (type.getEntityClass() != null && LivingEntity.class.isAssignableFrom(type.getEntityClass()))
			{
				final NamedEntitySpawner spawner = NamedEntitySpawnerParamitrisable.getNamedEntitySpawner(config.getString("overwriteEntities." + type.name(), null));
				if (spawner == null)
					overwriteEntities[type.ordinal()] = null;
				else if (spawner.getType() == type)
					if (spawner instanceof CustomEntitySpawner)
						overwriteEntities[type.ordinal()] = (CustomEntitySpawner) spawner;
					else
					{
						System.err.println("Could not use " + spawner.getName() + " to overwrite default " + type.name() + " entities (Only custom entities allowed)!");
						overwriteEntities[type.ordinal()] = null;
					}
				else
				{
					System.err.println("Could not use " + spawner.getName() + " to overwrite default " + type.name() + " entities (Wrong EntityType)!");
					overwriteEntities[type.ordinal()] = null;
				}
			}
			else
				overwriteEntities[type.ordinal()] = null;
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
		if (customEntities.size() == 0)
			config.set("customEntities", new HashMap<String, Object>(0));
		else
		{
			config.set("customEntities", null);
			for (final CustomEntitySpawner customEntity : customEntities)
				customEntity.save(config, "customEntities." + customEntity.getName() + ".");
		}
		// OverwriteEntities
		for (final EntityType type : EntityType.values())
		{
			final NamedEntitySpawner spawner = overwriteEntities[type.ordinal()];
			if (spawner == null)
				config.set("overwriteEntities." + type.name(), null);
			else
				config.set("overwriteEntities." + type.name(), spawner.getName());
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

	public void addCustomEntity(final CustomEntitySpawner customEntity, final String... aliases)
	{
		customEntities.add(customEntity);
		NamedEntitySpawnerParamitrisable.registerNamedEntitySpawner(customEntity, aliases);
		saveConfiguration();
	}

	public Set<CustomEntitySpawner> getCreatures()
	{
		return customEntities;
	}

	public void removeCustomEntity(final CustomEntitySpawner customEntity)
	{
		customEntities.remove(customEntity);
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
