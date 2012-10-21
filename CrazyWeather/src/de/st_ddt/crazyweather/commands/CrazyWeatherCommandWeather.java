package de.st_ddt.crazyweather.commands;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatConverter;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.DurationParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.WeatherParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.WorldParamitrisable;
import de.st_ddt.crazyweather.CrazyWeather;

public class CrazyWeatherCommandWeather extends CrazyWeatherCommandExecutor
{

	public CrazyWeatherCommandWeather(final CrazyWeather plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYWEATHER.COMMAND.WEATHER $World$ $Duration$ $Weather$ $Static$ $Load$", "CRAZYWEATHER.BROADCAST.WEATHER $Name$ $World$ $Weather$ $Static$ $Load$" })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		final Map<String, Paramitrisable> params = new TreeMap<String, Paramitrisable>();
		final WorldParamitrisable world = new WorldParamitrisable(sender);
		params.put("world", world);
		final WeatherParamitrisable weather = new WeatherParamitrisable(null);
		params.put("weather", weather);
		params.put("", weather);
		final DurationParamitrisable duration = new DurationParamitrisable((long) plugin.getRandomDuration());
		params.put("d", duration);
		params.put("duration", duration);
		final BooleanParamitrisable keepStatic = new BooleanParamitrisable(false);
		params.put("static", keepStatic);
		final BooleanParamitrisable keepLoad = new BooleanParamitrisable(false);
		params.put("load", keepLoad);
		ChatHelperExtended.readParameters(args, params);
		if (!sender.hasPermission("crazyweather.weather." + weather.getValue().toString().toLowerCase()) && !sender.hasPermission("crazyweather." + world.getValue().getName() + ".weather." + weather.getValue().toString().toLowerCase()))
			throw new CrazyCommandPermissionException();
		if (keepStatic.getValue())
			if (!sender.hasPermission("crazyweather.weather.static") && !sender.hasPermission("crazyweather." + world.getValue().getName() + ".weather.static"))
				throw new CrazyCommandPermissionException();
		if (keepLoad.getValue())
			if (!sender.hasPermission("crazyweather.weather.load") && !sender.hasPermission("crazyweather." + world.getValue().getName() + ".weather.load"))
				throw new CrazyCommandPermissionException();
		plugin.getWorldWeather(world.getValue()).setWeather(weather.getValue(), keepStatic.getValue(), keepLoad.getValue(), (int) (duration.getValue() / 50));
		plugin.getCrazyLogger().log("Weather", sender.getName() + " changed the weather on " + world.getValue().getName() + " to " + weather + " for " + duration + " ms. (Static:" + keepStatic + ", Load:" + keepLoad + ")");
		plugin.sendLocaleMessage("COMMAND.WEATHER", sender, world.getValue().getName(), ChatConverter.timeConverter(duration.getValue() / 1000, 1, sender, 2, false), weather, keepStatic, keepLoad);
		plugin.broadcastLocaleMessage("BROADCAST.WEATHER", sender.getName(), world.getValue().getName(), weather, keepStatic, keepLoad);
	}
}
