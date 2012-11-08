package de.st_ddt.crazyweather;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.commands.CrazyPluginCommandMainMode;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyweather.commands.CrazyWeatherCommandRain;
import de.st_ddt.crazyweather.commands.CrazyWeatherCommandStrike;
import de.st_ddt.crazyweather.commands.CrazyWeatherCommandSun;
import de.st_ddt.crazyweather.commands.CrazyWeatherCommandThunderRain;
import de.st_ddt.crazyweather.commands.CrazyWeatherCommandWeather;
import de.st_ddt.crazyweather.listener.CrazyWeatherPlayerListener;
import de.st_ddt.crazyweather.listener.CrazyWeatherWeatherListener;

public class CrazyWeather extends CrazyPlugin implements WeatherPlugin
{

	private static CrazyWeather plugin;
	private final HashMap<String, WorldWeather> worldWeather = new HashMap<String, WorldWeather>();
	private final CrazyPluginCommandMainMode modeCommand = new CrazyPluginCommandMainMode(this);
	private CrazyWeatherPlayerListener playerListener = null;
	private CrazyWeatherWeatherListener weatherListener = null;
	private int tool = 280;
	private boolean lightningdisabled = false;
	private boolean lightningdamagedisabled = false;

	public static CrazyWeather getPlugin()
	{
		return plugin;
	}

	public CrazyWeather()
	{
		super();
		registerModes();
	}

	@Localized("CRAZYWEATHER.MODE.CHANGE $Name$ $Value$")
	private void registerModes()
	{
		modeCommand.addMode(modeCommand.new IntegerMode("thundertool")
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				sendLocaleMessage("MODE.CHANGE", sender, tool == -1 ? "DISABLED" : new ItemStack(tool).getType().toString(), tool);
			}

			@Override
			public void setValue(final Integer newValue) throws CrazyException
			{
				if (newValue < -1)
					throw new CrazyCommandParameterException(1, "ToolID", "-1 (disabled)", "0 (Air/Empty Hands)");
				tool = newValue;
			}

			@Override
			public Integer getValue()
			{
				return tool;
			}
		});
		modeCommand.addMode(modeCommand.new BooleanFalseMode("lightningdisabled")
		{

			@Override
			public void setValue(final Boolean newValue) throws CrazyException
			{
				lightningdisabled = newValue;
			}

			@Override
			public Boolean getValue()
			{
				return lightningdisabled;
			}
		});
		modeCommand.addMode(modeCommand.new BooleanFalseMode("lightningdamagedisabled")
		{

			@Override
			public void setValue(final Boolean newValue) throws CrazyException
			{
				lightningdamagedisabled = newValue;
			}

			@Override
			public Boolean getValue()
			{
				return lightningdamagedisabled;
			}
		});
	}

	private void registerCommands()
	{
		getCommand("sun").setExecutor(new CrazyWeatherCommandSun(this));
		getCommand("rain").setExecutor(new CrazyWeatherCommandRain(this));
		getCommand("thunderrain").setExecutor(new CrazyWeatherCommandThunderRain(this));
		getCommand("weather").setExecutor(new CrazyWeatherCommandWeather(this));
		getCommand("strike").setExecutor(new CrazyWeatherCommandStrike(this));
	}

	private void registerHooks()
	{
		this.playerListener = new CrazyWeatherPlayerListener(this);
		this.weatherListener = new CrazyWeatherWeatherListener(this);
		final PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(weatherListener, this);
		pm.registerEvents(playerListener, this);
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
		registerHooks();
		super.onEnable();
		registerCommands();
	}

	@Override
	public void loadConfiguration()
	{
		final ConfigurationSection config = getConfig();
		worldWeather.clear();
		for (final World world : getServer().getWorlds())
			loadWorld(world);
		tool = config.getInt("tool", 280);
		lightningdisabled = config.getBoolean("lightningdisabled", false);
		lightningdamagedisabled = config.getBoolean("lightningdamagedisabled", false);
		// Logger
		logger.createLogChannels(config.getConfigurationSection("logs"), "Weather", "ThunderStrike");
	}

	public WorldWeather loadWorld(final World world)
	{
		if (world == null)
			return null;
		final ConfigurationSection config = getConfig();
		final WorldWeather weather = new WorldWeather(world);
		weather.load(config.getConfigurationSection("worlds." + world.getName()));
		worldWeather.put(world.getName(), weather);
		if (weather.isOnLoadEnabled())
			weather.setWeather(weather.getStaticWeather(), true, true, 0);
		return weather;
	}

	@Override
	public void saveConfiguration()
	{
		final ConfigurationSection config = getConfig();
		for (final WorldWeather world : worldWeather.values())
			world.save(config, "worlds." + world.getWorldName() + ".");
		config.set("tool", tool);
		config.set("lightningdisabled", lightningdisabled);
		config.set("lightningdamagedisabled", lightningdamagedisabled);
		super.saveConfiguration();
	}

	@Override
	public void setWeather(final Weather weather, final World world, final boolean keepStatic, final boolean keepLoad, final int duration)
	{
		final WorldWeather worldWeather = getWorldWeather(world);
		if (worldWeather != null)
			worldWeather.setWeather(weather, keepStatic, keepLoad, duration);
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
	public WorldWeather getWorldWeather(final World world)
	{
		final WorldWeather weather = worldWeather.get(world.getName());
		if (weather != null)
			return weather;
		return loadWorld(world);
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

	public int getRandomDuration()
	{
		return (int) (20 * 60 * 20 + Math.round(20 * 60 * 20 * Math.random()));
	}
}
