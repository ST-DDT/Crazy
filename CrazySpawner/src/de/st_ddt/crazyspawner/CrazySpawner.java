package de.st_ddt.crazyspawner;

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
	public boolean Command(CommandSender sender, String commandLabel, String[] args) throws CrazyCommandException
	{
		if (!(sender instanceof Player))
			throw new CrazyCommandExecutorException(false);
		Player player = (Player) sender;
		if (commandLabel.equalsIgnoreCase("cms") || commandLabel.equalsIgnoreCase("crazymobs") || commandLabel.equalsIgnoreCase("crazyspawn"))
		{
			if (!player.hasPermission("crazyspawner.spawn"))
				throw new CrazyCommandPermissionException();
			CreatureType type = null;
			int amount = 1;
			switch (args.length)
			{
				case 2:
					try
					{
						amount = Integer.parseInt(args[1]);
					}
					catch (NumberFormatException e)
					{
						throw new CrazyCommandParameterException(2, "Integer/Number");
					}
					if (amount <= 0)
						throw new CrazyCommandParameterException(2, "positive Numbers");
				case 1:
					try
					{
						type = CreatureType.valueOf(args[0].toUpperCase());// .fromName(args[0]);
					}
					catch (Exception e)
					{
						type = null;
					}
					if (type == null)
						throw new CrazyCommandParameterException(1, "Monstername", "sheep", "creeper", "zombie");
					try
					{
						Location location = player.getLocation();
						while (location.add(0, -1, 0).getBlock().isEmpty() && location.getBlockZ() > 0)
							;
						location.add(0, 1, 0);
						for (int i = 0; i < amount; i++)
							location.getWorld().spawnCreature(location, type);
					}
					catch (Exception e)
					{
						throw new CrazyCommandException();
					}
					sendLocaleMessage("COMMAND.SPAWN", sender, String.valueOf(amount), type.getName());
					return true;
				default:
					throw new CrazyCommandUsageException("/crazyspawn <Monstername> [Amount]");
			}
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
					catch (NumberFormatException e)
					{
						throw new CrazyCommandParameterException(1, "Integer/Number");
					}
					if (range <= 0)
						throw new CrazyCommandParameterException(1, "positive Numbers");
				case 0:
					int anz = 0;
					for (Entity entity : player.getWorld().getEntitiesByClass(Monster.class))
						if (entity.getLocation().distance(player.getLocation()) < range)
						{
							entity.remove();
							anz++;
						}
					sendLocaleMessage("COMMAND.KILL.MONSTERS", sender, String.valueOf(anz));
					anz = 0;
					if (killpets)
					{
						for (Entity entity : player.getWorld().getEntitiesByClass(Animals.class))
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

	@Override
	protected String getShortPluginName()
	{
		return "cms";
	}
}
