package de.st_ddt.crazyweather.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatConverter;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyweather.CrazyWeather;

public class CrazyWeatherCommandStrike extends CrazyWeatherCommandExecutor
{

	public CrazyWeatherCommandStrike(final CrazyWeather plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYWEATHER.COMMAND.STRIKE")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		Location location = null;
		switch (args.length)
		{
			case 0:
				if (sender instanceof ConsoleCommandSender)
					throw new CrazyCommandUsageException("<Player>", "<Location...>");
				location = ((Player) sender).getTargetBlock(null, 1024).getLocation();
				break;
			case 1:
				final Player player = Bukkit.getPlayer(args[0]);
				if (player == null)
					throw new CrazyCommandNoSuchException("Player", args[0]);
				location = player.getLocation();
				if (!sender.hasPermission("crazyweather.thunder.player") && !sender.hasPermission("crazyweather." + location.getWorld().getName() + ".thunder.player"))
					throw new CrazyCommandPermissionException();
				break;
			default:
				location = ChatConverter.stringToLocation(sender, args);
		}
		if (!sender.hasPermission("crazyweather.thunder") && !sender.hasPermission("crazyweather." + location.getWorld().getName() + ".thunder"))
			throw new CrazyCommandPermissionException();
		plugin.strikeTarget(sender, location);
		plugin.getCrazyLogger().log("ThunderStrike", sender.getName() + " send a thunderstrike to " + location.getWorld().getName());
		plugin.sendLocaleMessage("COMMAND.STRIKE", sender);
	}
}
