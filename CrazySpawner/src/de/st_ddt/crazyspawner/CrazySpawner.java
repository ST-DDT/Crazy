package de.st_ddt.crazyspawner;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyspawner.commands.CrazySpawnerCommandKill;
import de.st_ddt.crazyspawner.commands.CrazySpawnerCommandSpawn;
import de.st_ddt.crazyspawner.tasks.SpawnTask;

public class CrazySpawner extends CrazyPlugin
{

	private static CrazySpawner plugin;
	private final Set<SpawnTask> tasks = new HashSet<SpawnTask>();

	public static CrazySpawner getPlugin()
	{
		return plugin;
	}

	private void registerCommands()
	{
		getCommand("crazyspawn").setExecutor(new CrazySpawnerCommandSpawn(this));
		getCommand("crazykill").setExecutor(new CrazySpawnerCommandKill(this));
	}

	@Override
	public void onLoad()
	{
		plugin = this;
		super.onLoad();
	}

	@Override
	public void onEnable()
	{
		super.onEnable();
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
