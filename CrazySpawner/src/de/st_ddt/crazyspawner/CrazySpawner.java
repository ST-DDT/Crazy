package de.st_ddt.crazyspawner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyspawner.commands.CrazySpawnerCommandCreatureSpawner;
import de.st_ddt.crazyspawner.commands.CrazySpawnerCommandKill;
import de.st_ddt.crazyspawner.commands.CrazySpawnerCommandSpawn;
import de.st_ddt.crazyspawner.listener.CrazySpawnerPlayerListener;
import de.st_ddt.crazyspawner.tasks.SpawnTask;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.paramitrisable.ExtendedCreatureParamitrisable;

public class CrazySpawner extends CrazyPlugin
{

	private static CrazySpawner plugin;
	private final Set<SpawnTask> tasks = new HashSet<SpawnTask>();
	private final Map<String, EntityType> creatureSelection = new HashMap<String, EntityType>();

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
		getCommand("crazyspawn").setExecutor(new CrazySpawnerCommandSpawn(this));
		getCommand("crazykill").setExecutor(new CrazySpawnerCommandKill(this));
		getCommand("crazycreaturespawner").setExecutor(new CrazySpawnerCommandCreatureSpawner(this, creatureSelection));
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
		final ConfigurationSection taskConfig = config.getConfigurationSection("tasks");
		tasks.clear();
		if (taskConfig != null)
			for (final String key : taskConfig.getKeys(false))
				tasks.add(new SpawnTask(plugin, taskConfig.getConfigurationSection(key)));
		for (final SpawnTask task : tasks)
			task.run();
	}

	@Override
	public void saveConfiguration()
	{
		final ConfigurationSection config = getConfig();
		config.set("tasks", null);
		final int i = 0;
		for (final SpawnTask task : tasks)
			task.save(config, "tasks." + i + ".");
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
