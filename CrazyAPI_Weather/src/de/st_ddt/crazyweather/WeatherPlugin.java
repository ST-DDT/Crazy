package de.st_ddt.crazyweather;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyweather.data.WeatherData;

public interface WeatherPlugin extends CrazyPluginInterface
{

	public static final WeatherPluginProvider WEATHERPLUGINPROVIDER = new WeatherPluginProvider();

	/**
	 * Changes the weather of the specified world.
	 * 
	 * @param weather
	 *            The new weather for that world.
	 * @param world
	 *            The world to change the weather in.
	 * @param keepStatic
	 *            Whether the weather remains the same until changed by commands or server restart/reload.
	 * @param keepLoad
	 *            Whether the weather remains the same until changed by commands.
	 * @param duration
	 *            The amount of seconds the weather remains the same.
	 */
	public void setWeather(final Weather weather, final World world, final boolean keepStatic, final boolean keepLoad, int duration);

	public void strikeTarget(final CommandSender sender, final Location target);

	public void strikeTarget(final CommandSender sender, final Player player);

	public int getThunderTool();

	public boolean isLightningDisabled();

	public boolean isLightningDamageDisabled();

	public WeatherData getWorldWeather(final World world);

	public class WeatherPluginProvider
	{

		private WeatherPlugin plugin;

		private WeatherPluginProvider()
		{
			super();
		}

		public WeatherPlugin getPlugin()
		{
			return plugin;
		}

		protected void setPlugin(final WeatherPlugin plugin)
		{
			this.plugin = plugin;
		}
	}
}
