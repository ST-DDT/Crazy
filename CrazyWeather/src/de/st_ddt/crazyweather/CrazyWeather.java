package de.st_ddt.crazyweather;

import java.util.HashMap;

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

public class CrazyWeather extends CrazyPlugin implements WeatherPlugin
{

	private static CrazyWeather plugin;
	private int tool = 280;
	private boolean lightningdisabled = false;
	private boolean lightningdamagedisabled = false;
	private final HashMap<String, WorldWeather> worldWeather = new HashMap<String, WorldWeather>();
	private CrazyWeatherPlayerListener playerListener = null;
	private CrazyWeatherWeatherListener weatherListener = null;

	public static CrazyWeather getPlugin()
	{
		return plugin;
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
		worldWeather.clear();
		for (final World world : getServer().getWorlds())
			loadWorld(world);
		tool = config.getInt("tool", 280);
		lightningdisabled = config.getBoolean("lightningdisabled", false);
		lightningdamagedisabled = config.getBoolean("lightningdamagedisabled", false);
	}

	public WorldWeather loadWorld(final World world)
	{
		if (world == null)
			return null;
		final ConfigurationSection config = getConfig();
		final WorldWeather weather = new WorldWeather(world);
		weather.load(config.getConfigurationSection("worlds." + world.getName()));
		worldWeather.put(world.getName(), weather);
		return weather;
	}

	@Override
	public void save()
	{
		final ConfigurationSection config = getConfig();
		for (final WorldWeather world : worldWeather.values())
			world.save(config, "worlds." + world.getWorldName() + ".");
		saveConfiguration();
	}

	public void saveConfiguration()
	{
		final ConfigurationSection config = getConfig();
		config.set("tool", tool);
		config.set("lightningdisabled", lightningdisabled);
		config.set("lightningdamagedisabled", lightningdamagedisabled);
		super.save();
	}

	public void registerHooks()
	{
		this.playerListener = new CrazyWeatherPlayerListener(this);
		this.weatherListener = new CrazyWeatherWeatherListener(this);
		final PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(weatherListener, this);
		pm.registerEvents(playerListener, this);
	}

	@Override
	public boolean command(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyCommandException
	{
		if (commandLabel.equals("sun") || commandLabel.equals("rain"))
		{
			commandWeather(sender, commandLabel, args);
			return true;
		}
		if (commandLabel.equals("weather"))
		{
			commandWeather(sender, args);
			return true;
		}
		if (commandLabel.equals("thunder"))
		{
			commandThunder(sender, args);
			return true;
		}
		return false;
	}

	private void commandWeather(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		Weather weather = null;
		World world = null;
		if (sender instanceof Player)
			world = ((Player) sender).getWorld();
		Boolean keepStatic = false;
		Boolean keepLoad = false;
		int duration = getRandomDuration();
		final int length = args.length;
		for (int i = 0; i < length; i++)
		{
			final String arg = args[i].toLowerCase();
			if (arg.startsWith("weather:"))
			{
				weather = Weather.getWeather(arg.substring(8));
				if (weather == null)
					throw new CrazyCommandParameterException(i, "Weather", "weather:SUN/RAIN/THUNDER/THUNDERRAIN");
			}
			else if (arg.startsWith("world:"))
			{
				world = Bukkit.getWorld(arg.substring(6));
				if (world == null)
					throw new CrazyCommandNoSuchException("World", arg.substring(6), worldWeather.keySet());
			}
			else if (arg.startsWith("duration:"))
			{
				try
				{
					duration = Integer.parseInt(arg.substring(9));
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(i, "Integer", "duration:0", "duration:100 (seconds)");
				}
				finally
				{
					duration = Math.max(0, duration);
				}
			}
			else if (arg.startsWith("static:"))
			{
				final String temp = arg.substring(7);
				if (temp.equalsIgnoreCase("true") || temp.equals("1"))
					keepStatic = true;
				else
					keepStatic = false;
			}
			else if (arg.startsWith("load:"))
			{
				final String temp = arg.substring(5);
				if (temp.equalsIgnoreCase("true") || temp.equals("1"))
					keepLoad = true;
				else
					keepLoad = false;
			}
			else
			{
				weather = Weather.getWeather(arg);
				if (weather == null)
					throw new CrazyCommandUsageException("/crazyweather [weather:]Weather [world:World] [duration:Integer] [static:Boolean] [load:Boolean]");
			}
		}
		if (weather == null)
			throw new CrazyCommandUsageException("/crazyweather [weather:]Weather [world:World] [duration:Integer] [static:Boolean] [load:Boolean]");
		if (world == null)
			throw new CrazyCommandUsageException("/crazyweather [weather:]Weather <world:World> [duration:Integer] [static:Boolean] [load:Boolean]");
		changeWeather(sender, weather, world, keepStatic, keepLoad, duration);
	}

	private void commandWeather(final CommandSender sender, final String weatherString, final String[] args) throws CrazyCommandException
	{
		if (!(sender instanceof Player))
			throw new CrazyCommandExecutorException(false);
		final Player player = (Player) sender;
		final World world = player.getWorld();
		changeWeather(player, Weather.getWeather(weatherString), world, false, false, getRandomDuration());
	}

	protected void changeWeather(final CommandSender sender, final Weather weather, final World world, final boolean keepStatic, final boolean keepLoad, final int duration) throws CrazyCommandPermissionException
	{
		if (!hasWeatherPermission(sender, world, keepStatic || keepLoad, weather))
			throw new CrazyCommandPermissionException();
		final WorldWeather worldWeather = getWorldWeather(world);
		if (worldWeather == null)
		{
			sendLocaleMessage("COMMAND.WEATHER.ERROR", sender);
			return;
		}
		else
		{
			worldWeather.setWeather(weather, keepStatic, keepLoad, duration);
			if ((weather != null) && (sender instanceof Player))
				sendLocaleMessage("WEATHER." + weather.toString(), sender, world.getName());
			sendLocaleMessage("COMMAND.WEATHER.SUCCESS", sender);
		}
	}

	protected int getRandomDuration()
	{
		return (int) Math.round(Math.random() * 10 * 60) + 20 * 60;
	}

	@Override
	public void setWeather(final Weather weather, final World world, final boolean keepStatic, final boolean keepLoad, final int duration)
	{
		final WorldWeather worldWeather = getWorldWeather(world);
		if (worldWeather != null)
			worldWeather.setWeather(weather, keepStatic, keepLoad, duration);
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

	@Override
	public void strikeTarget(final CommandSender sender, final Location target)
	{
		target.getWorld().strikeLightning(target);
	}

	@Override
	public void strikeTarget(final CommandSender sender, final Player player)
	{
		player.getWorld().strikeLightning(player.getLocation());
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
					int newtool = tool;
					try
					{
						newtool = Integer.parseInt(args[1]);
					}
					catch (final NumberFormatException e)
					{
						throw new CrazyCommandParameterException(1, "ToolID");
					}
					if (newtool < -1)
						throw new CrazyCommandParameterException(1, "ToolID", "-1 (disabled)", "0 (Air/Empty Hands)");
					tool = newtool;
					sendLocaleMessage("THUNDERTOOL.SET", sender, tool == -1 ? "DISABLED" : new ItemStack(tool).getType().toString(), tool);
					return;
				}
				throw new CrazyCommandNoSuchException("Mode", args[0], "thundertool");
			case 1:
				if (args[0].equalsIgnoreCase("thundertool"))
				{
					if (!sender.hasPermission("crazyweather.thunder.tool") && !sender.hasPermission("crazyweather.thunder.toolchange"))
						throw new CrazyCommandPermissionException();
					sendLocaleMessage("THUNDERTOOL.GET", sender, tool == -1 ? "DISABLED" : new ItemStack(tool).getType().toString(), tool);
					return;
				}
				throw new CrazyCommandNoSuchException("Mode", args[0], "thundertool");
			default:
				throw new CrazyCommandUsageException("/crazyweather mode <Mode> [Value]");
		}
	}

	private boolean hasWeatherPermission(final CommandSender sender, final World world, final boolean set, final Weather weather)
	{
		if (sender.hasPermission("crazyweather.set.*"))
			return true;
		if (world != null)
		{
			if (sender.hasPermission("crazyweather." + world.getName() + ".set.*"))
				return true;
			if (sender.hasPermission("crazyweather." + world.getName() + ".set." + weather.toString()))
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
			if (sender.hasPermission("crazyweather." + world.getName() + ".change." + weather.toString()))
				return true;
		}
		return false;
	}

	@Override
	public int getThunderTool()
	{
		return tool;
	}

	@Override
	public boolean isLightningDisabled()
	{
		return lightningdisabled;
	}

	@Override
	public boolean isLightningDamageDisabled()
	{
		return lightningdamagedisabled;
	}

	@Override
	public WorldWeather getWorldWeather(final World world)
	{
		final WorldWeather weather = worldWeather.get(world.getName());
		if (weather != null)
			return weather;
		return loadWorld(world);
	}
}
