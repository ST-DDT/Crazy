package de.st_ddt.crazypromoter;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.platymuus.bukkit.permissions.PermissionsPlugin;

import de.st_ddt.crazyutil.conditions.Condition;

public class Promotion
{

	protected String name;
	protected Condition<Player> condition;
	protected String newGroup;
	protected List<String> removeGroups;
	protected final PermissionsPlugin permissionsPlugin = ((PermissionsPlugin) Bukkit.getPluginManager().getPlugin("PermissionsBukkit"));

	public Promotion(ConfigurationSection config)
	{
		super();
		name = config.getString("name");
		removeGroups = config.getStringList("removeGroups");
		newGroup = config.getString("newGroup");
		condition = Condition.load(config.getConfigurationSection("condition"));
	}

	public Promotion(String name)
	{
		super();
		this.name = name;
		this.removeGroups = new ArrayList<String>();
		this.newGroup = "default";
	}

	public boolean isApplyable(Player player)
	{
		return condition.match(player);
	}

	public void apply(Player player)
	{
		for (String group : removeGroups)
			permissionsPlugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "permissions player removegroup " + player.getName() + " " + group);
		permissionsPlugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "permissions player addgroup " + player.getName() + " " + newGroup);
	}

	public String getName()
	{
		return name;
	}

	public void save(FileConfiguration config, String path)
	{
		config.set(path + "name", name);
		config.set(path + "removeGroups", removeGroups);
		config.set(path + "newGroup", newGroup);
		condition.save(config, path + "condition.");
	}

	public Condition<Player> getCondition()
	{
		return condition;
	}

	public void setCondition(Condition<Player> condition)
	{
		this.condition = condition;
	}
}
