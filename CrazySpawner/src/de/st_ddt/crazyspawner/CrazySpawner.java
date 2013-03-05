package de.st_ddt.crazyspawner;

import java.util.Collection;
import java.util.HashMap;
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
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyspawner.commands.CommandCreatureSpawner;
import de.st_ddt.crazyspawner.commands.CommandKill;
import de.st_ddt.crazyspawner.commands.CommandSpawn;
import de.st_ddt.crazyspawner.commands.CommandTheEndAutoRespawn;
import de.st_ddt.crazyspawner.listener.CrazySpawnerPlayerListener;
import de.st_ddt.crazyspawner.tasks.SpawnTask;
import de.st_ddt.crazyutil.ExtendedCreatureType;
import de.st_ddt.crazyutil.paramitrisable.ExtendedCreatureParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class CrazySpawner extends CrazyPlugin
{

	private static CrazySpawner plugin;
	private final Set<SpawnTask> tasks = new TreeSet<SpawnTask>();
	private final Map<Player, EntityType> creatureSelection = new HashMap<Player, EntityType>();

	public static CrazySpawner getPlugin()
	{
		return plugin;
	}

	public void registerHooks()
	{
		final PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new CrazySpawnerPlayerListener(this, creatureSelection), this);
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
		sendLocaleMessage("CREATURES.AVAILABLE", Bukkit.getConsoleSender(), ExtendedCreatureParamitrisable.CREATURE_TYPES.size());
		super.onEnable();
		registerHooks();
		registerCommands();
	}

	@Override
	public void loadConfiguration()
	{
		final ConfigurationSection config = getConfig();
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
