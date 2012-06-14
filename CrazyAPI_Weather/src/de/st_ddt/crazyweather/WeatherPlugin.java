package de.st_ddt.crazyweather;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.CrazyPluginInterface;

public interface WeatherPlugin extends CrazyPluginInterface
{

	public abstract void setWeather(final Weather weather, final World world, final boolean keepStatic, final boolean keepLoad, int duration);

	public abstract void strikeTarget(final CommandSender sender, final Location target);

	public abstract void strikeTarget(final CommandSender sender, final Player player);

	public abstract int getThunderTool();

	public abstract boolean isLightningDisabled();

	public abstract boolean isLightningDamageDisabled();

	public abstract WeatherData getWorldWeather(final World world);
}
