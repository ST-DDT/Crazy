package de.st_ddt.crazyweather.commands;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandExecutorException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatConverter;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyweather.CrazyWeather;
import de.st_ddt.crazyweather.Weather;

public class CrazyWeatherCommandSun extends CrazyWeatherCommandExecutor
{

	public CrazyWeatherCommandSun(final CrazyWeather plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYWEATHER.COMMAND.WEATHER.SUN $World$ $Duration$", "CRAZYWEATHER.BROADCAST.WEATHER.SUN $Name$ $World$" })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!(sender instanceof Player))
			throw new CrazyCommandExecutorException(false);
		final Player player = (Player) sender;
		final World world = player.getWorld();
		if (!player.hasPermission("crazyweather.weather.sun") && !player.hasPermission("crazyweather." + world.getName() + ".weather.sun"))
			throw new CrazyCommandPermissionException();
		int duration = plugin.getRandomDuration();
		if (args.length > 0)
			duration = (int) (ChatConverter.stringToDuration(args) / 50);
		plugin.getWorldWeather(world).setWeather(Weather.SUN, false, false, duration);
		plugin.getCrazyLogger().log("Weather", player.getName() + " changed the weather on " + world.getName() + " to SUN for " + duration + " ticks.");
		plugin.sendLocaleMessage("COMMAND.WEATHER.SUN", player, world.getName(), ChatConverter.timeConverter(duration / 20, 1, player, 2, false));
		plugin.broadcastLocaleMessage("BROADCAST.WEATHER.SUN", player.getName(), world.getName());
	}
}
