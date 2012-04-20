package de.st_ddt.crazyweather;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandExecutorException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class CrazyWeather extends CrazyPlugin
{

	private static CrazyWeather plugin;
	private int tool = 280;
	private boolean lightningdisabled = false;
	private boolean lightningdamagedisabled = false;
	private ArrayList<WorldWeather> worldWeather;
	private CrazyWeatherPlayerListener playerListener = null;
	private CrazyWeatherWeatherListener weatherListener = null;

	public static CrazyWeather getPlugin()
	{
		return plugin;
	}

	@Override
	protected String getShortPluginName()
	{
		return "weather";
	}

	@Override
	public void onEnable()
	{
		plugin = this;
		registerHooks();
		super.onEnable();
	}

	@Override
	public void load()
	{
		super.load();
		ConfigurationSection config = getConfig();
		worldWeather = new ArrayList<WorldWeather>();
		for (World world : getServer().getWorlds())
		{
			WorldWeather weather = new WorldWeather(world);
			weather.load(config.getConfigurationSection("worlds." + world.getName()));
			worldWeather.add(weather);
		}
		tool = config.getInt("tool", 280);
		lightningdisabled = config.getBoolean("lightningdisabled", false);
		lightningdamagedisabled = config.getBoolean("lightningdamagedisabled", false);
	}

	@Override
	public void save()
	{
		ConfigurationSection config = getConfig();
		for (WorldWeather world : worldWeather)
			world.save(config, "worlds." + world.getName());
		config.set("tool", tool);
		config.set("lightningdisabled", lightningdisabled);
		config.set("lightningdamagedisabled", lightningdamagedisabled);
		super.save();
	}

	public void registerHooks()
	{
		this.playerListener = new CrazyWeatherPlayerListener();
		this.weatherListener = new CrazyWeatherWeatherListener();
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(weatherListener, this);
		pm.registerEvents(playerListener, this);
	}

	@Override
	public boolean command(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyCommandException
	{
		if (commandLabel.equalsIgnoreCase("sun") || commandLabel.equalsIgnoreCase("rain"))
		{
			if (!(sender instanceof Player))
				throw new CrazyCommandExecutorException(false);
			Player player = (Player) sender;
			if (!sender.hasPermission("crazyweather.change." + commandLabel))
				throw new CrazyCommandPermissionException();
			commandWeather(player, commandLabel, player.getWorld(), false, false);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("thunder"))
		{
			Location target = null;
			switch (args.length)
			{
				case 0:
					if (!sender.hasPermission("crazyweather.thunder"))
						throw new CrazyCommandPermissionException();
					if (sender instanceof Player)
						target = ((Player) sender).getTargetBlock(null, 1024).getLocation();
					break;
				case 1:
					if (!sender.hasPermission("crazyweather.thunder.player"))
						throw new CrazyCommandPermissionException();
					Player p = getServer().getPlayer(args[0]);
					if (p == null)
						throw new CrazyCommandNoSuchException("Player", args[0]);
					target = p.getLocation();
					break;
				default:
					throw new CrazyCommandUsageException("/thunder", "/thunder <Player>");
			}
			if (target == null)
				throw new CrazyCommandNoSuchException("Target", "none");
			commandThunder(sender, target);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("thundertool"))
		{
			if (!sender.hasPermission("crazyweather.thunder.tool") && !sender.hasPermission("crazyweather.thunder.toolchange"))
				throw new CrazyCommandPermissionException();
			switch (args.length)
			{
				case 0:
					sendLocaleMessage("THUNDERTOOL.GET", sender, new ItemStack(tool).getType().toString(), String.valueOf(tool));
					return true;
				case 1:
					if (!sender.hasPermission("crazyweather.thunder.toolchange"))
						throw new CrazyCommandPermissionException();
					try
					{
						int newtool = Integer.parseInt(args[0]);
						tool = newtool;
					}
					catch (NumberFormatException e)
					{
						throw new CrazyCommandUsageException("/thundertool", "/thundertool <ToolID>");
					}
					sendLocaleMessage("THUNDERTOOL.SET", sender, new ItemStack(tool).getType().toString(), String.valueOf(tool));
					return true;
				default:
					throw new CrazyCommandUsageException("/thundertool", "/thundertool <ToolID>");
			}
		}
		return false;
	}

	private void commandWeather(final CommandSender sender, final String weather, final World world, final boolean keepStatic, final boolean keepLoad)
	{
		WorldWeather worldWeather = getWorldWeather(world);
		if (worldWeather != null)
			worldWeather.setWeather(weather, keepStatic, keepLoad);
		else
			sendLocaleMessage("COMMAND.WEATHER.ERROR", sender);
		sendLocaleMessage("COMMAND.WEATHER.SUCCESS", sender);
	}

	public void commandThunder(final CommandSender sender, final Location target)
	{
		target.getWorld().strikeLightning(target);
	}

	@Override
	public boolean commandMain(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyException
	{
		String weather;
		if (commandLabel.equalsIgnoreCase("sun") || commandLabel.equalsIgnoreCase("sunny") || commandLabel.equalsIgnoreCase("dry") || commandLabel.equalsIgnoreCase("clear") || commandLabel.equalsIgnoreCase("false") || commandLabel.equalsIgnoreCase("none"))
			weather = "sun";
		else if (commandLabel.equalsIgnoreCase("rain") || commandLabel.equalsIgnoreCase("rainy") || commandLabel.equalsIgnoreCase("wet") || commandLabel.equalsIgnoreCase("snow") || commandLabel.equalsIgnoreCase("storm"))
			weather = "rain";
		else if (commandLabel.equalsIgnoreCase("thunder") || commandLabel.equalsIgnoreCase("lightning"))
			weather = "thunder";
		else
			return false;
		// throw new CrazyCommandParameterException(0, "Weather", ChatColor.YELLOW + "sun", ChatColor.BLUE + "rain", ChatColor.DARK_BLUE + "thunder");
		World world = null;
		boolean keepStatic = false;
		boolean keepLoad = false;
		switch (args.length)
		{
			case 2:
				if (args[1].equalsIgnoreCase("onload") || args[1].equalsIgnoreCase("load"))
				{
					keepLoad = true;
					keepStatic = true;
				}
				else if (args[1].equalsIgnoreCase("static") || args[1].equalsIgnoreCase("fixed"))
				{
					keepStatic = true;
				}
				else
					throw new CrazyCommandParameterException(2, "KeepType", "static", "onload");
			case 1:
				if (args[0].equalsIgnoreCase("onload") || args[0].equalsIgnoreCase("load"))
				{
					keepLoad = true;
					keepStatic = true;
				}
				else if (args[0].equalsIgnoreCase("static") || args[0].equalsIgnoreCase("fixed"))
				{
					keepStatic = true;
				}
				else
				{
					world = getServer().getWorld(args[0]);
					if (world != null)
						break;
					throw new CrazyCommandParameterException(1, "World / KeepType", getServer().getWorlds().get(0).getName(), "static", "onload");
				}
			case 0:
				break;
			default:
				throw new CrazyCommandUsageException("/weather <Type> [World] [Static/OnLoad]");
		}
		if (world == null)
			if (sender instanceof Player)
				world = ((Player) sender).getWorld();
		if (world == null)
			throw new CrazyCommandUsageException("/weather <Type> <World> [Static/OnLoad]");
		if (keepStatic)
			if (!sender.hasPermission("crazyweather.set." + weather))
				throw new CrazyCommandPermissionException();
		if (!sender.hasPermission("crazyweather.change." + weather))
			throw new CrazyCommandPermissionException();
		commandWeather(sender, weather, world, keepStatic, keepLoad);
		return true;
	}

	public int getTool()
	{
		return tool;
	}

	public boolean isLightningdisabled()
	{
		return lightningdisabled;
	}

	public boolean isLightningdamagedisabled()
	{
		return lightningdamagedisabled;
	}

	public WorldWeather getWorldWeather(World world)
	{
		for (WorldWeather temp : worldWeather)
			if (temp.equals(world))
				return temp;
		return null;
	}
}
