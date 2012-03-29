package de.st_ddt.crazyonline;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.entity.Player;
import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyutil.PairList;

public class CrazyOnline extends CrazyPlugin
{

	private static CrazyOnline plugin;
	protected PairList<String, OnlinePlayerData> datas = new PairList<String, OnlinePlayerData>();
	private CrazyOnlinePlayerListener playerListener = null;
	public static final SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	private String saveType;

	public static CrazyOnline getPlugin()
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
		FileConfiguration config = getConfig();
		saveType = config.getString("saveType", "flat").toLowerCase();
		if (saveType.equals("flat"))
		{
			for (String name : config.getConfigurationSection("players").getKeys(false))
				datas.setDataVia1(name, new OnlinePlayerData(name, config.getConfigurationSection("players." + name)));
		}
	}

	@Override
	public void save()
	{
		FileConfiguration config = getConfig();
		config.set("saveType", saveType);
		if (saveType.equals("flat"))
		{
			for (OnlinePlayerData data : datas.getData2List())
				data.save(config, "players." + data.getName() + ".");
		}
		super.save();
	}

	public void registerHooks()
	{
		this.playerListener = new CrazyOnlinePlayerListener(this);
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(playerListener, this);
	}

	@Override
	public boolean Command(CommandSender sender, String commandLabel, String[] args) throws CrazyCommandException
	{
		if (commandLabel.equalsIgnoreCase("pinfo"))
		{
			switch (args.length)
			{
				case 0:
					if (!(sender instanceof Player))
						throw new CrazyCommandUsageException("/pinfo <Player>");
					CommandInfo(sender, (Player) sender);
					return true;
				case 1:
					OfflinePlayer info = getServer().getPlayer(args[0]);
					if (info == null)
						info = getServer().getOfflinePlayer(args[0]);
					if (info == null)
						throw new CrazyCommandNoSuchException("Player", args[0]);
					CommandInfo(sender, info);
					return true;
				default:
					throw new CrazyCommandUsageException("/pinfo <Player>");
			}
		}
		if (commandLabel.equalsIgnoreCase("ponlines"))
		{
			CommandOnlines(sender);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("psince"))
		{
			switch (args.length)
			{
				case 0:
					if (sender instanceof Player)
					{
						CommandSince((Player) sender);
						return true;
					}
					throw new CrazyCommandUsageException("/psince <yyyy.MM.dd>", "/psince <yyyy.MM.dd HH:mm:ss>");
				case 1:
					CommandSince(sender, args[0] + " 00:00:00");
					return true;
				case 2:
					CommandSince(sender, args[0] + " " + args[1]);
					return true;
				default:
					throw new CrazyCommandUsageException("/psince <yyyy.MM.dd>", "/psince <yyyy.MM.dd HH:mm:ss>");
			}
		}
		if (commandLabel.equalsIgnoreCase("pbefore"))
		{
			switch (args.length)
			{
				case 0:
					if (sender instanceof Player)
					{
						CommandBefore((Player) sender);
						return true;
					}
					throw new CrazyCommandUsageException("/pbefore <yyyy.MM.dd>", "/pbefore <yyyy.MM.dd HH:mm:ss>");
				case 1:
					CommandBefore(sender, args[0] + " 00:00:00");
					return true;
				case 2:
					CommandBefore(sender, args[0] + " " + args[1]);
					return true;
				default:
					throw new CrazyCommandUsageException("/pbefore <yyyy.MM.dd>", "/pbefore <yyyy.MM.dd HH:mm:ss>");
			}
		}
		return false;
	}

	private void CommandOnlines(CommandSender sender) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazyonline.online"))
			throw new CrazyCommandPermissionException();
		sendLocaleMessage("MESSAGE.ONLINES.HEADER", sender);
		sendLocaleMessage("MESSAGE.SEPERATOR", sender);
		for (Player player : getServer().getOnlinePlayers())
			sendLocaleMessage("MESSAGE.LIST", sender, player.getName(), getPlayerData(player).getLastLoginString());
	}

	public void CommandInfo(CommandSender sender, OfflinePlayer player) throws CrazyCommandException
	{
		if (sender == player)
		{
			if (!sender.hasPermission("crazyonline.info.self"))
				throw new CrazyCommandPermissionException();
		}
		else if (!sender.hasPermission("crazyonline.info.other"))
			throw new CrazyCommandPermissionException();
		OnlinePlayerData data = getPlayerData(player);
		if (data == null)
			throw new CrazyCommandNoSuchException("PlayerData", player.getName());
		sendLocaleMessage("MESSAGE.INFO.HEADER", sender, data.getName());
		sendLocaleMessage("MESSAGE.SEPERATOR", sender);
		sendLocaleMessage("MESSAGE.INFO.LOGIN.FIRST", sender, data.getFirstLoginString());
		sendLocaleMessage("MESSAGE.INFO.LOGIN.LAST", sender, data.getLastLoginString());
		sendLocaleMessage("MESSAGE.INFO.LOGOUT.LAST", sender, data.getLastLogoutString());
		sendLocaleMessage("MESSAGE.INFO.TIME.LAST", sender, timeOutputConverter(data.getTimeLast(), sender));
		sendLocaleMessage("MESSAGE.INFO.TIME.TOTAL", sender, timeOutputConverter(data.getTimeTotal(), sender));
	}

	public String timeOutputConverter(long time, CommandSender sender)
	{
		if (time > 2880)
		{
			long days = time / 60 / 24;
			long hours = time / 60 % 24;
			return days + " " + locale.getLocaleMessage(sender, "TIME.UNIT.DAYS") + " " + hours + " " + locale.getLocaleMessage(sender, "TIME.UNIT.HOURS");
		}
		else if (time > 120)
		{
			long hours = time / 60;
			long minutes = time % 60;
			return hours + " " + locale.getLocaleMessage(sender, "TIME.UNIT.HOURS") + " " + minutes + " " + locale.getLocaleMessage(sender, "TIME.UNIT.MINUTES");
		}
		else
			return time + " " + locale.getLocaleMessage(sender, "TIME.UNIT.MINUTES");
	}

	public void CommandSince(Player player) throws CrazyCommandException
	{
		if (getPlayerData(player) == null)
			throw new CrazyCommandCircumstanceException("when joined at least for the second time!");
		CommandSince(player, getPlayerData(player).getLastLogout());
	}

	public void CommandSince(CommandSender sender, String date) throws CrazyCommandException
	{
		try
		{
			CommandSince(sender, DateFormat.parse(date));
		}
		catch (ParseException e)
		{
			throw new CrazyCommandParameterException(1, "Date");
		}
	}

	public void CommandSince(CommandSender sender, Date date) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazyonline.since"))
			throw new CrazyCommandPermissionException();
		ArrayList<OnlinePlayerData> list = new ArrayList<OnlinePlayerData>();
		for (OnlinePlayerData data : datas.getData2List())
			if (data.getLastLogin().after(date))
				list.add(data);
		sendLocaleMessage("MESSAGE.SINCE.HEADER", sender, DateFormat.format(date));
		sendLocaleMessage("MESSAGE.SEPERATOR", sender);
		for (OnlinePlayerData data : list)
			sendLocaleMessage("MESSAGE.LIST", sender, data.getName(), data.getLastLoginString());
	}

	public void CommandBefore(Player player) throws CrazyCommandException
	{
		CommandSince(player, getPlayerData(player).getLastLogin());
	}

	public void CommandBefore(CommandSender sender, String date) throws CrazyCommandException
	{
		try
		{
			CommandBefore(sender, DateFormat.parse(date));
		}
		catch (ParseException e)
		{
			throw new CrazyCommandParameterException(1, "Date");
		}
	}

	public void CommandBefore(CommandSender sender, Date date) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazyonline.before"))
			throw new CrazyCommandPermissionException();
		ArrayList<OnlinePlayerData> list = new ArrayList<OnlinePlayerData>();
		for (OnlinePlayerData data : datas.getData2List())
			if (data.getLastLogin().after(date))
				list.add(data);
		sendLocaleMessage("MESSAGE.BEFORE.HEADER", sender, DateFormat.format(date));
		sendLocaleMessage("MESSAGE.SEPERATOR", sender);
		for (OnlinePlayerData data : list)
			sendLocaleMessage("MESSAGE.LIST", sender, data.getName(), data.getLastLoginString());
	}

	public OnlinePlayerData getPlayerData(OfflinePlayer player)
	{
		return datas.findDataVia1(player.getName().toLowerCase());
	}

	public PairList<String, OnlinePlayerData> getDatas()
	{
		return datas;
	}

	public static SimpleDateFormat getDateFormat()
	{
		return DateFormat;
	}
}
