package de.st_ddt.crazypromoter;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.conditions.ConditionList;
import de.st_ddt.crazyutil.conditions.Condition_AND;
import de.st_ddt.crazyutil.conditions.Condition_FALSE;
import de.st_ddt.crazyutil.conditions.player.ConditionPlayerPermBukkitGroup;

public class CrazyPromoter extends CrazyPlugin
{

	private static CrazyPromoter plugin;
	protected ArrayList<Promotion> promotions = new ArrayList<Promotion>();
	private CrazyPromoterPlayerListener playerListener = null;
	private int checkInterval;

	public static CrazyPromoter getPlugin()
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
		ConfigurationSection config = getConfig();
		checkInterval = config.getInt("checkInterval", 60);
		promotions.clear();
		config = config.getConfigurationSection("promotions");
		if (config == null)
		{
			Promotion promotion = new Promotion("default");
			promotions.add(promotion);
			ConditionList<Player> condition = new Condition_AND<Player>();
			promotion.setCondition(condition);
			condition.getConditions().add(new Condition_FALSE<Player>());
			condition.getConditions().add(new ConditionPlayerPermBukkitGroup("default"));
		}
		else
			for (String name : config.getKeys(false))
				promotions.add(new Promotion(config.getConfigurationSection(name)));
		getServer().getScheduler().scheduleAsyncRepeatingTask(this, new ScheduledCheckTask(plugin), 100, checkInterval * 20 * 60);
	}

	@Override
	public void save()
	{
		ConfigurationSection config = getConfig();
		config.set("promotions", null);
		for (Promotion promotion : promotions)
			promotion.save(config, "promotions." + promotion.getName() + ".");
		saveConfiguration();
	}

	public void saveConfiguration()
	{
		ConfigurationSection config = getConfig();
		config.set("checkInterval", checkInterval);
		saveConfig();
	}

	public void registerHooks()
	{
		this.playerListener = new CrazyPromoterPlayerListener(this);
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(playerListener, this);
	}

	@Override
	public boolean command(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyCommandException
	{
		if (commandLabel.equalsIgnoreCase("promotioncheck"))
		{
			commandCheck(sender, args);
			return true;
		}
		return false;
	}

	private void commandCheck(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		switch (args.length)
		{
			case 0:
				if (sender instanceof ConsoleCommandSender)
					throw new CrazyCommandUsageException("/promotioncheck <Player>");
				commandCheck(sender, (Player) sender);
				return;
			case 1:
				String name = args[0];
				Player player = getServer().getPlayerExact(name);
				if (player == null)
				{
					player = getServer().getPlayer(name);
					if (player == null)
						throw new CrazyCommandNoSuchException("OnlinePlayer", name);
				}
				commandCheck(sender, player);
				return;
			default:
				throw new CrazyCommandUsageException("/promotioncheck <Player>");
		}
	}

	private void commandCheck(final CommandSender sender, final Player player) throws CrazyCommandException
	{
		if (!sender.hasPermission(sender == player ? "crazypromoter.check.self" : "crazypromoter.check.other"))
			throw new CrazyCommandPermissionException();
		boolean update = checkStatus(player);
		if (update)
			return;
		sendLocaleMessage("COMMAND.CHECK.FAIL", sender, player.getName());
	}

	public void checkStatus()
	{
		for (Player player : Bukkit.getOnlinePlayers())
			checkStatus(player);
	}

	public boolean checkStatus(final Player player)
	{
		for (Promotion promotion : promotions)
			if (promotion.isApplyable(player))
			{
				promotion.apply(player);
				broadcastLocaleMessage("COMMAND.CHECK.SUCCESS", player.getName(), promotion.getName());
				return true;
			}
		return false;
	}

	@Override
	public boolean commandMain(CommandSender sender, String commandLabel, String[] args) throws CrazyException
	{
		if (commandLabel.equalsIgnoreCase("mode"))
		{
			commandMainMode(sender, args);
			return true;
		}
		return false;
	}

	private void commandMainMode(CommandSender sender, String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazylogin.mode"))
			throw new CrazyCommandPermissionException();
		switch (args.length)
		{
			case 2:
				if (args[0].equalsIgnoreCase("checkInterval"))
				{
					int time = checkInterval;
					try
					{
						time = Integer.parseInt(args[1]);
					}
					catch (NumberFormatException e)
					{
						throw new CrazyCommandParameterException(1, "Integer", "-1 = disabled", "Time in Seconds > 60");
					}
					checkInterval = time;
					sendLocaleMessage("MODE.CHANGE", sender, "checkInterval", checkInterval == -1 ? "disabled" : checkInterval + " minutes");
					saveConfiguration();
					return;
				}
				throw new CrazyCommandNoSuchException("Mode", args[0]);
			case 1:
				if (args[0].equalsIgnoreCase("checkInterval"))
				{
					sendLocaleMessage("MODE.CHANGE", sender, "checkInterval", checkInterval == -1 ? "disabled" : checkInterval + " minutes");
					return;
				}
				throw new CrazyCommandNoSuchException("Mode", args[0]);
			default:
				throw new CrazyCommandUsageException("/crazylogin mode <Mode> [Value]");
		}
	}
}
