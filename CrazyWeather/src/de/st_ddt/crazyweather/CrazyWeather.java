package de.st_ddt.crazyweather;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

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
		final ConfigurationSection config = getConfig();
		worldWeather = new ArrayList<WorldWeather>();
		for (final World world : getServer().getWorlds())
		{
			final WorldWeather weather = new WorldWeather(world);
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
		final ConfigurationSection config = getConfig();
		for (final WorldWeather world : worldWeather)
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
		final PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(weatherListener, this);
		pm.registerEvents(playerListener, this);
	}

	@Override
	public boolean command(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyCommandException
	{
		if (commandLabel.equalsIgnoreCase("sun") || commandLabel.equalsIgnoreCase("rain"))
		{
			commandWeather(sender, commandLabel.toLowerCase(), args);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("weather"))
		{
			commandWeather(sender, args);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("thunder"))
		{
			commandThunder(sender, args);
			return true;
		}
		return false;
	}

	private void commandWeather(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		World world = null;
		if (sender instanceof Player)
			world = ((Player) sender).getWorld();
		if (sender instanceof ConsoleCommandSender && args.length < 2)
			throw new CrazyCommandUsageException("/weather <World> <Weather> [static] [onLoad]");
		else if (args.length < 1)
			throw new CrazyCommandUsageException("/weather [World] <Weather> [static] [onLoad]");
		String weather = null;
		boolean keepStatic = false;
		boolean keepLoad = false;
		switch (args.length)
		{
			case 4:
				if (args[3].equalsIgnoreCase("static"))
					keepStatic = true;
				else if (args[3].equalsIgnoreCase("onLoad"))
					keepLoad = true;
			case 3:
				if (args[2].equalsIgnoreCase("static"))
					keepStatic = true;
				else if (args[2].equalsIgnoreCase("onLoad"))
					keepLoad = true;
			case 2:
				if (args[1].equalsIgnoreCase("sun") || args[1].equalsIgnoreCase("sunny") || args[1].equalsIgnoreCase("dry") || args[1].equalsIgnoreCase("clear") || args[1].equalsIgnoreCase("false") || args[1].equalsIgnoreCase("none"))
					weather = "sun";
				else if (args[1].equalsIgnoreCase("rain") || args[1].equalsIgnoreCase("rainy") || args[1].equalsIgnoreCase("wet") || args[1].equalsIgnoreCase("snow") || args[1].equalsIgnoreCase("storm"))
					weather = "rain";
				else if (args[1].equalsIgnoreCase("thunder") || args[1].equalsIgnoreCase("lightning"))
					weather = "thunder";
				else if (args[1].equalsIgnoreCase("static"))
					keepStatic = true;
				else if (args[1].equalsIgnoreCase("onLoad"))
					keepLoad = true;
			case 1:
				if (weather == null && world != null)
				{
					if (args[0].equalsIgnoreCase("sun") || args[0].equalsIgnoreCase("sunny") || args[0].equalsIgnoreCase("dry") || args[0].equalsIgnoreCase("clear") || args[0].equalsIgnoreCase("false") || args[0].equalsIgnoreCase("none"))
						weather = "sun";
					else if (args[0].equalsIgnoreCase("rain") || args[0].equalsIgnoreCase("rainy") || args[0].equalsIgnoreCase("wet") || args[0].equalsIgnoreCase("snow") || args[0].equalsIgnoreCase("storm"))
						weather = "rain";
					else if (args[0].equalsIgnoreCase("thunder") || args[0].equalsIgnoreCase("lightning"))
						weather = "thunder";
					else
					{
						world = Bukkit.getWorld(args[0]);
						if (world == null)
							throw new CrazyCommandParameterException(1, "Weather / World", "sun", "rain", "thunder", Bukkit.getWorlds().get(0).getName());
					}
				}
				else
				{
					world = Bukkit.getWorld(args[0]);
					if (world == null)
						throw new CrazyCommandNoSuchException("World", args[0]);
				}
				if (world != null && weather != null)
					break;
			default:
				if (sender instanceof ConsoleCommandSender)
					throw new CrazyCommandUsageException("/weather <World> <Weather> [static] [onLoad]");
				else
					throw new CrazyCommandUsageException("/weather [World] <Weather> [static] [onLoad]");
		}
		changeWeather(sender, weather, world, keepStatic, keepLoad);
	}

	private void commandWeather(final CommandSender sender, final String weather, final String[] args) throws CrazyCommandException
	{
		if (!(sender instanceof Player))
			throw new CrazyCommandExecutorException(false);
		final Player player = (Player) sender;
		final World world = player.getWorld();
		changeWeather(player, weather, world, false, false);
	}

	protected void changeWeather(final CommandSender sender, final String weather, final World world, final boolean keepStatic, final boolean keepLoad) throws CrazyCommandPermissionException
	{
		if (!hasWeatherPermission(sender, world, keepStatic || keepLoad, weather))
			throw new CrazyCommandPermissionException();
		final WorldWeather worldWeather = getWorldWeather(world);
		if (worldWeather != null)
		{
			worldWeather.setWeather(weather, keepStatic, keepLoad);
			sendLocaleMessage("COMMAND.WEATHER.SUCCESS", sender);
		}
		else
			sendLocaleMessage("COMMAND.WEATHER.ERROR", sender);
	}

	public void setWeather(final String weather, final World world, final boolean keepStatic, final boolean keepLoad)
	{
		final WorldWeather worldWeather = getWorldWeather(world);
		if (worldWeather != null)
			worldWeather.setWeather(weather, keepStatic, keepLoad);
	}

	private void commandThunder(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		Location target = null;
		Player player = null;
		if (sender instanceof Player)
			player = (Player) sender;
		switch (args.length)
		{
			case 0:
				if (sender instanceof ConsoleCommandSender)
					throw new CrazyCommandExecutorException(false);
				if (!sender.hasPermission("crazyweather.thunder"))
					throw new CrazyCommandPermissionException();
				target = player.getTargetBlock(null, 1024).getLocation();
				break;
			case 1:
				if (!sender.hasPermission("crazyweather.thunder.player"))
					throw new CrazyCommandPermissionException();
				player = getServer().getPlayer(args[0]);
				if (player == null)
					throw new CrazyCommandNoSuchException("Player", args[0]);
				target = player.getLocation();
				break;
			default:
				throw new CrazyCommandUsageException("/thunder", "/thunder <Player>");
		}
		if (target == null)
			throw new CrazyCommandNoSuchException("Target", "none");
		strikeTarget(sender, target);
	}

	public void strikeTarget(final CommandSender sender, final Location target)
	{
		target.getWorld().strikeLightning(target);
	}

	@Override
	public boolean commandMain(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyException
	{
		if (commandLabel.equalsIgnoreCase("mode"))
		{
			commandMainMode(sender, args);
			return true;
		}
		return false;
	}

	private void commandMainMode(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazyweather.mode"))
			throw new CrazyCommandPermissionException();
		switch (args.length)
		{
			case 2:
				if (args[0].equalsIgnoreCase("thundertool"))
				{
					if (!sender.hasPermission("crazyweather.thunder.toolchange"))
						throw new CrazyCommandPermissionException();
					try
					{
						final int newtool = Integer.parseInt(args[1]);
						tool = newtool;
					}
					catch (final NumberFormatException e)
					{
						throw new CrazyCommandUsageException("/thundertool", "/thundertool <ToolID>");
					}
					sendLocaleMessage("THUNDERTOOL.SET", sender, new ItemStack(tool).getType().toString(), tool);
					return;
				}
				throw new CrazyCommandNoSuchException("Mode", args[0]);
			case 1:
				if (args[0].equalsIgnoreCase("thundertool"))
				{
					if (!sender.hasPermission("crazyweather.thunder.tool") && !sender.hasPermission("crazyweather.thunder.toolchange"))
						throw new CrazyCommandPermissionException();
					sendLocaleMessage("THUNDERTOOL.GET", sender, new ItemStack(tool).getType().toString(), tool);
					return;
				}
				throw new CrazyCommandNoSuchException("Mode", args[0]);
			default:
				throw new CrazyCommandUsageException("/crazyweather mode <Mode> [Value]");
		}
	}

	private boolean hasWeatherPermission(CommandSender sender, World world, boolean set, String weather)
	{
		if (sender.hasPermission("crazyweather.set.*"))
			return true;
		if (world != null)
		{
			if (sender.hasPermission("crazyweather." + world.getName() + ".set.*"))
				return true;
			if (sender.hasPermission("crazyweather." + world.getName() + ".set." + weather))
				return true;
		}
		if (set)
			return false;
		if (sender.hasPermission("crazyweather.change.*"))
			return true;
		if (world != null)
		{
			if (sender.hasPermission("crazyweather." + world.getName() + ".change.*"))
				return true;
			if (sender.hasPermission("crazyweather." + world.getName() + ".change." + weather))
				return true;
		}
		return false;
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

	public WorldWeather getWorldWeather(final World world)
	{
		for (WorldWeather temp : worldWeather)
			if (temp.equals(world))
				return temp;
		return null;
	}
}
