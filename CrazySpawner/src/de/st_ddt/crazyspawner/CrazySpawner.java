package de.st_ddt.crazyspawner;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Animals;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandExecutorException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;

@SuppressWarnings("deprecation")
public class CrazySpawner extends CrazyPlugin
{

	@Override
	public boolean command(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyCommandException
	{
		if (!(sender instanceof Player))
			throw new CrazyCommandExecutorException(false);
		final Player player = (Player) sender;
		if (commandLabel.equalsIgnoreCase("cms") || commandLabel.equalsIgnoreCase("crazymobs") || commandLabel.equalsIgnoreCase("crazyspawn"))
		{
			commandSpawn(player, args);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("cmk") || commandLabel.equalsIgnoreCase("crazymobs") || commandLabel.equalsIgnoreCase("butcher") || commandLabel.equalsIgnoreCase("killall"))
		{
			int range = 20;
			boolean killpets = false;
			switch (args.length)
			{
				case 2:
					if (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("1"))
						killpets = true;
				case 1:
					try
					{
						range = Integer.parseInt(args[0]);
					}
					catch (final NumberFormatException e)
					{
						throw new CrazyCommandParameterException(1, "Integer/Number");
					}
					if (range <= 0)
						throw new CrazyCommandParameterException(1, "positive Numbers");
				case 0:
					int anz = 0;
					for (final Entity entity : player.getWorld().getEntitiesByClass(Monster.class))
						if (entity.getLocation().distance(player.getLocation()) < range)
						{
							entity.remove();
							anz++;
						}
					sendLocaleMessage("COMMAND.KILL.MONSTERS", sender, String.valueOf(anz));
					anz = 0;
					if (killpets)
					{
						for (final Entity entity : player.getWorld().getEntitiesByClass(Animals.class))
							if (entity.getLocation().distance(player.getLocation()) < range)
							{
								entity.remove();
								anz++;
							}
						sendLocaleMessage("COMMAND.KILL.ANIMALS", sender, String.valueOf(anz));
					}
					return true;
			}
		}
		return false;
	}

	private void commandSpawn(final Player player, final String[] args) throws CrazyCommandException
	{
		if (!player.hasPermission("crazyspawner.spawn"))
			throw new CrazyCommandPermissionException();
		if (args.length < 2)
			throw new CrazyCommandUsageException("/cms <Monstername> [[amount:]Integer] [delay:Integer] [repeat:Integer] [interval:Integer]");
		final int length = args.length;
		CreatureType type = null;
		int amount = 1;
		int delay = 0;
		int repeat = 0;
		int interval = 1;
		try
		{
			type = CreatureType.valueOf(args[0].toUpperCase());// .fromName(args[0]);
		}
		catch (final Exception e)
		{
			type = null;
		}
		if (type == null)
			throw new CrazyCommandParameterException(1, "Monstername", "sheep", "creeper", "zombie");
		for (int i = 1; i < length; i++)
		{
			final String arg = args[i];
			if (arg.startsWith("amount:"))
				try
				{
					amount = Integer.parseInt(arg.substring(7));
					if (amount < 0)
						throw new CrazyCommandParameterException(i, "positive Integer");
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(i, "amount:Integer");
				}
			else if (arg.startsWith("delay:"))
				try
				{
					delay = Integer.parseInt(arg.substring(6));
					if (delay <= 0)
						throw new CrazyCommandParameterException(i, "positive Integer");
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(i, "delay:Integer");
				}
			else if (arg.startsWith("repeat:"))
				try
				{
					repeat = Integer.parseInt(arg.substring(7));
					if (repeat < 0)
						throw new CrazyCommandParameterException(i, "positive Integer");
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(i, "repeat:Integer");
				}
			else if (arg.startsWith("interval:"))
				try
				{
					interval = Integer.parseInt(arg.substring(9));
					if (interval <= 0)
						throw new CrazyCommandParameterException(i, "positive Integer");
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(i, "interval:Integer");
				}
			else
				try
				{
					amount = Integer.parseInt(arg);
					if (amount < 0)
						throw new CrazyCommandParameterException(i, "positive Integer");
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandUsageException("/cms <Monstername> [[amount:]Integer] [delay:Integer] [repeat:Integer] [interval:Integer]");
				}
		}
		try
		{
			final Location location = player.getLocation();
			while (location.add(0, -1, 0).getBlock().isEmpty() && location.getBlockZ() > 0)
				;
			location.add(0, 1, 0);
			delay -= interval;
			delay *= 20;
			interval *= 20;
			for (int i = 0; i <= repeat; i++)
				Bukkit.getScheduler().scheduleAsyncDelayedTask(this, new CrazySpawnerSpawnTask(location, type, amount), delay += interval);
		}
		catch (final Exception e)
		{
			throw new CrazyCommandException();
		}
		sendLocaleMessage("COMMAND.SPAWN", player, String.valueOf(amount), type.getName());
	}

	@Override
	protected String getShortPluginName()
	{
		return "mobs";
	}
}
