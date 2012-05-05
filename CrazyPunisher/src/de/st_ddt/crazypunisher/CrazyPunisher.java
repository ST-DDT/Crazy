package de.st_ddt.crazypunisher;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.dynmap.DynmapAPI;

import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandExecutorException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazypunisher.events.CrazyPunisherHardbanEvent;
import de.st_ddt.crazypunisher.events.CrazyPunisherJailEvent;
import de.st_ddt.crazypunisher.events.CrazyPunisherUnjailEvent;
import de.st_ddt.crazypunisher.events.CrazyPunisherVisibilityChangeEvent;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;
import de.st_ddt.crazyutil.Pair;
import de.st_ddt.crazyutil.PairList;
import de.st_ddt.crazyutil.geo.Sphere;

public class CrazyPunisher extends CrazyPlugin
{

	private static CrazyPunisher plugin;
	private final ArrayList<OfflinePlayer> banned = new ArrayList<OfflinePlayer>();
	private final PairList<OfflinePlayer, Date> jailed = new PairList<OfflinePlayer, Date>();
	private final ArrayList<String> hidden = new ArrayList<String>();
	private CrazyPunisherPlayerListener playerListener = null;
	public Sphere jailsphere = null;
	public Location jailcenter = null;
	private World jailworld = null;
	private int jailrange = 1;
	private boolean autoBanIP;
	private DynmapAPI dynmap = null;
	private boolean dynmapEnabled;

	public static CrazyPunisher getPlugin()
	{
		return plugin;
	}

	@Override
	protected String getShortPluginName()
	{
		return "cpunisher";
	}

	@Override
	public void onEnable()
	{
		plugin = this;
		registerHooks();
		super.onEnable();
	}

	public void load()
	{
		super.load();
		FileConfiguration config = getConfig();
		jailworld = getServer().getWorld(config.getString("jail.world", "world"));
		if (jailworld == null)
		{
			consoleLog(ChatColor.RED + "Error getting jailworld, using default");
			jailworld = getServer().getWorlds().get(0);
		}
		jailcenter = ObjectSaveLoadHelper.loadLocation(config.getConfigurationSection("jail"), jailworld);
		if (jailcenter == null)
			jailcenter = jailworld.getSpawnLocation();
		jailrange = config.getInt("jail.range", 5);
		jailsphere = new Sphere(jailcenter, jailrange);
		autoBanIP = config.getBoolean("autoBanIP", true);
		dynmapEnabled = config.getBoolean("dynmapEnabled", true);
		if (dynmapEnabled)
			dynmap = (DynmapAPI) Bukkit.getPluginManager().getPlugin("dynmap");
		Set<String> list = null;
		if (config.getConfigurationSection("player.banned") != null)
			list = config.getConfigurationSection("player.banned").getKeys(false);
		if (list != null)
			for (String ban : list)
			{
				if (!config.getBoolean("player.banned." + ban, false))
					continue;
				OfflinePlayer player = getServer().getOfflinePlayer(ban);
				if (player != null)
				{
					banned.add(player);
					player.setBanned(true);
					if (player instanceof Player)
						((Player) player).kickPlayer("You are banned on this server!");
				}
			}
		Date now = new Date();
		if (config.getConfigurationSection("player.jailed") != null)
			list = config.getConfigurationSection("player.jailed").getKeys(false);
		if (list != null)
			for (String jail : list)
			{
				OfflinePlayer player = getServer().getOfflinePlayer(jail);
				if (player == null)
					continue;
				Date until;
				try
				{
					try
					{
						until = DateFormat.parse(config.getString("player.jailed." + player.getName()));
					}
					catch (NullPointerException e)
					{
						until = DateFormat.parse(config.getString("player.jailed." + jail));
						config.set("player.jailed." + jail, null);
					}
				}
				catch (ParseException e)
				{
					until = now;
				}
				if (until.after(now))
					jailed.setDataVia1(player, until);
			}
	}

	@Override
	public void save()
	{
		YamlConfiguration config = (YamlConfiguration) getConfig();
		config.set("player.banned", null);
		ObjectSaveLoadHelper.saveLocation(config, "jail.", jailcenter);
		config.set("jail.range", jailrange);
		config.set("autoBanIP", autoBanIP);
		if (dynmap != null)
			config.set("dynmapEnabled", dynmapEnabled);
		for (OfflinePlayer player : banned)
			config.set("player.banned." + player.getName(), true);
		for (Pair<OfflinePlayer, Date> pair : jailed)
			config.set("player.jailed." + pair.getData1().getName(), DateFormat.format(pair.getData2()));
		super.save();
	}

	public void registerHooks()
	{
		this.playerListener = new CrazyPunisherPlayerListener(this);
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(this.playerListener, this);
	}

	@Override
	public boolean command(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyCommandException
	{
		if (commandLabel.equalsIgnoreCase("hardban"))
		{
			commandHardban(sender, args);
			return true;
		}
		else if (commandLabel.equalsIgnoreCase("ban"))
		{
			commandBan(sender, args);
			return true;
		}
		else if (commandLabel.equalsIgnoreCase("unban") || commandLabel.equalsIgnoreCase("pardon"))
		{
			commandUnban(sender, args);
			return true;
		}
		else if (commandLabel.equalsIgnoreCase("jail"))
		{
			commandJail(sender, args);
			return true;
		}
		else if (commandLabel.equalsIgnoreCase("unjail") || commandLabel.equalsIgnoreCase("free"))
		{
			commandUnjail(sender, args);
			return true;
		}
		else if (commandLabel.equalsIgnoreCase("jailtp") || commandLabel.equalsIgnoreCase("tpjail"))
		{
			commandJailTeleport(sender, args);
			return true;
		}
		else if (commandLabel.equalsIgnoreCase("kick"))
		{
			commandKick(sender, args);
			return true;
		}
		else if (commandLabel.equalsIgnoreCase("show"))
		{
			commandShow(sender, args);
			return true;
		}
		else if (commandLabel.equalsIgnoreCase("hide"))
		{
			commandHide(sender, args);
			return true;
		}
		return false;
	}

	private void commandHardban(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazypunisher.hardban"))
			throw new CrazyCommandPermissionException();
		switch (args.length)
		{
			case 1:
				String name = args[0];
				OfflinePlayer player = getServer().getPlayerExact(name);
				if (player == null)
				{
					player = getServer().getPlayer(name);
					if (player == null)
					{
						player = getServer().getOfflinePlayer(name);
						if (player == null)
							throw new CrazyCommandNoSuchException("Player", name);
					}
				}
				hardBan(player);
				save();
				return;
			default:
				throw new CrazyCommandUsageException("/hardban <Name>");
		}
	}

	public void hardBan(final OfflinePlayer player)
	{
		if (player == null)
			return;
		jail(player, 60 * 60 * 24 * 365 * 10);
		banned.add(player);
		save();
		player.setBanned(true);
		player.setOp(false);
		player.setWhitelisted(false);
		if (player instanceof Player)
		{
			Player p = (Player) player;
			if (autoBanIP)
				getServer().banIP(p.getAddress().getAddress().getHostAddress());
			p.getInventory().clear();
			p.setGameMode(GameMode.SURVIVAL);
			p.getWorld().strikeLightning(p.getLocation());
			p.setHealth(0);
			kick(p, locale.getLocaleMessage(p, "MESSAGE.HARDBAN"));
		}
		broadcastLocaleMessage("BROADCAST.HARDBAN", player.getName());
		getServer().getPluginManager().callEvent(new CrazyPunisherHardbanEvent(player));
	}

	private void commandBan(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazypunisher.ban"))
			throw new CrazyCommandPermissionException();
		switch (args.length)
		{
			case 1:
				String name = args[0];
				OfflinePlayer player = getServer().getPlayerExact(name);
				if (player == null)
				{
					player = getServer().getPlayer(name);
					if (player == null)
					{
						player = getServer().getOfflinePlayer(name);
						if (player == null)
							throw new CrazyCommandNoSuchException("Player", name);
					}
				}
				ban(player);
				return;
			default:
				throw new CrazyCommandUsageException("/ban <Name>");
		}
	}

	public void ban(final OfflinePlayer player)
	{
		if (player == null)
			return;
		banned.add(player);
		save();
		player.setBanned(true);
		if (player instanceof Player)
		{
			Player p = (Player) player;
			if (autoBanIP)
				getServer().banIP(p.getAddress().getAddress().getHostAddress());
			kick(p, locale.getLocaleMessage(p, "MESSAGE.BAN"));
		}
		broadcastLocaleMessage("BROADCAST.BAN", player.getName());
	}

	private void commandUnban(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazypunisher.unban"))
			throw new CrazyCommandPermissionException();
		switch (args.length)
		{
			case 1:
				String name = args[0];
				OfflinePlayer player = getServer().getPlayerExact(name);
				if (player == null)
				{
					player = getServer().getPlayer(name);
					if (player == null)
					{
						player = getServer().getOfflinePlayer(name);
						if (player == null)
							throw new CrazyCommandNoSuchException("Player", name);
					}
				}
				unban(player);
				return;
			default:
				throw new CrazyCommandUsageException("/unban <Name>");
		}
	}

	public void unban(final OfflinePlayer player)
	{
		if (player == null)
			return;
		banned.remove(player);
		save();
		player.setBanned(false);
		broadcastLocaleMessage("BROADCAST.UNBAN", player.getName());
	}

	private void commandJail(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazypunisher.jail"))
			throw new CrazyCommandPermissionException();
		int duration = 10;
		int interval = 60;
		switch (args.length)
		{
			case 3:
				interval = 1;
				String unit = args[2];
				if (unit.equalsIgnoreCase("s") || unit.equalsIgnoreCase("sec") || unit.equalsIgnoreCase("second") || unit.equalsIgnoreCase("seconds"))
					interval = 1;
				else if (unit.equalsIgnoreCase("m") || unit.equalsIgnoreCase("min") || unit.equalsIgnoreCase("minute") || unit.equalsIgnoreCase("minutes"))
					interval = 60;
				else if (unit.equalsIgnoreCase("h") || unit.equalsIgnoreCase("hour") || unit.equalsIgnoreCase("hours"))
					interval = 60 * 60;
				else if (unit.equalsIgnoreCase("d") || unit.equalsIgnoreCase("day") || unit.equalsIgnoreCase("days"))
					interval = 60 * 60 * 24;
				else if (unit.equalsIgnoreCase("w") || unit.equalsIgnoreCase("week") || unit.equalsIgnoreCase("weeks"))
					interval = 60 * 60 * 24 * 7;
				else if (unit.equalsIgnoreCase("month") || unit.equalsIgnoreCase("months"))
					interval = 60 * 60 * 24 * 30;
				else if (unit.equalsIgnoreCase("y") || unit.equalsIgnoreCase("year") || unit.equalsIgnoreCase("years"))
					interval = 60 * 60 * 24 * 365;
				else
					throw new CrazyCommandParameterException(2, "TimeUnit", "second", "minute", "hour", "day", "week");
			case 2:
				try
				{
					duration = Integer.parseInt(args[1]);
				}
				catch (NumberFormatException e)
				{
					throw new CrazyCommandParameterException(1, "small Integer");
				}
			case 1:
				String name = args[0];
				OfflinePlayer player = getServer().getPlayerExact(name);
				if (player == null)
				{
					player = getServer().getPlayer(name);
					if (player == null)
					{
						player = getServer().getOfflinePlayer(name);
						if (player == null)
							throw new CrazyCommandNoSuchException("Player", name);
					}
				}
				jail(player, duration * interval);
				return;
			default:
				throw new CrazyCommandUsageException("/jail <Name> [Duration [TimeUnit]]");
		}
	}

	public void jail(final OfflinePlayer player, final long duration)
	{
		if (player == null)
			return;
		Date time = new Date();
		time.setTime(time.getTime() + duration * 1000);
		jailed.setDataVia1(player, time);
		save();
		if (player instanceof Player)
			keepJailed((Player) player);
		broadcastLocaleMessage("BROADCAST.JAIL", player.getName());
		getServer().getPluginManager().callEvent(new CrazyPunisherJailEvent(player, time));
	}

	private void commandUnjail(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazypunisher.unjail"))
			throw new CrazyCommandPermissionException();
		switch (args.length)
		{
			case 1:
				String name = args[0];
				OfflinePlayer player = getServer().getPlayerExact(name);
				if (player == null)
				{
					player = getServer().getPlayer(name);
					if (player == null)
					{
						player = getServer().getOfflinePlayer(name);
						if (player == null)
							throw new CrazyCommandNoSuchException("Player", name);
					}
				}
				unjail(player);
				return;
			default:
				throw new CrazyCommandUsageException("/unjail <Name>");
		}
	}

	public void unjail(final OfflinePlayer player)
	{
		if (player == null)
			return;
		jailed.removeDataVia1(player);
		getConfig().set("player.jailed." + player.getName(), DateFormat.format(new Date()));
		save();
		if (player instanceof Player)
		{
			Player p = (Player) player;
			Location target = p.getBedSpawnLocation();
			if (target == null)
				target = p.getWorld().getSpawnLocation();
			if (target != null)
				p.teleport(target, TeleportCause.PLUGIN);
		}
		broadcastLocaleMessage("BROADCAST.UNJAIL", player.getName());
		getServer().getPluginManager().callEvent(new CrazyPunisherUnjailEvent(player));
	}

	private void commandJailTeleport(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazypunisher.jailtp"))
			throw new CrazyCommandPermissionException();
		Player player = null;
		switch (args.length)
		{
			case 1:
				String name = args[0];
				player = getServer().getPlayerExact(name);
				if (player == null)
				{
					player = getServer().getPlayer(name);
					if (player == null)
						throw new CrazyCommandNoSuchException("Player", name);
				}
			case 0:
				if (player == null)
					if (sender instanceof Player)
						player = (Player) sender;
					else
						throw new CrazyCommandUsageException("/jailtp <Player>");
				jailTeleport(player);
				return;
			default:
				throw new CrazyCommandUsageException("/jailtp [Player]");
		}
	}

	public void jailTeleport(final Player player)
	{
		if (player == null)
			return;
		player.teleport(jailcenter);
	}

	private void commandKick(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazypunisher.kick"))
			throw new CrazyCommandPermissionException();
		switch (args.length)
		{
			case 1:
				String name = args[0];
				if (name.equalsIgnoreCase("*"))
				{
					if (!sender.hasPermission("crazypunisher.kick.all"))
						throw new CrazyCommandPermissionException();
					for (Player player : getServer().getOnlinePlayers())
						if (player != sender)
							kick(player);
					return;
				}
				Player player = getServer().getPlayerExact(name);
				if (player == null)
				{
					player = getServer().getPlayer(name);
					if (player == null)
						throw new CrazyCommandNoSuchException("Player", name);
				}
				kick(player);
				return;
			default:
				throw new CrazyCommandUsageException("/kick <Name>");
		}
	}

	public void kick(final Player player)
	{
		kick(player, locale.getLocaleMessage(player, "MESSAGE.KICK"));
	}

	public void kick(final Player player, String message)
	{
		if (player == null)
			return;
		player.kickPlayer(message);
		broadcastLocaleMessage("BROADCAST.KICK", player.getName());
	}

	private void commandShow(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazypunisher.show"))
			throw new CrazyCommandPermissionException();
		ArrayList<Player> players = new ArrayList<Player>();
		if (args.length == 0)
		{
			if (sender instanceof ConsoleCommandSender)
				throw new CrazyCommandUsageException("/show <Player>");
			players.add((Player) sender);
		}
		for (String name : args)
		{
			Player player = getServer().getPlayer(name);
			if (player == null)
				throw new CrazyCommandNoSuchException("Player", name);
			players.add(player);
		}
		for (Player player : players)
		{
			CrazyPunisherVisibilityChangeEvent event = new CrazyPunisherVisibilityChangeEvent(player, true);
			getServer().getPluginManager().callEvent(event);
			if (event.isCancelled())
				continue;
			hidden.remove(player.getName());
			if (dynmap != null)
				dynmap.setPlayerVisiblity(player, true);
			for (Player plr : getServer().getOnlinePlayers())
				plr.showPlayer(player);
			sendLocaleMessage("COMMAND.SHOW", player);
		}
		sendLocaleMessage("COMMAND.SHOW.DONE", sender);
	}

	private void commandHide(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazypunisher.show"))
			throw new CrazyCommandPermissionException();
		ArrayList<Player> players = new ArrayList<Player>();
		if (args.length == 0)
		{
			if (sender instanceof ConsoleCommandSender)
				throw new CrazyCommandUsageException("/show <Player>");
			players.add((Player) sender);
		}
		for (String name : args)
		{
			Player player = getServer().getPlayer(name);
			if (player == null)
				throw new CrazyCommandNoSuchException("Player", name);
			players.add(player);
		}
		for (Player player : players)
		{
			CrazyPunisherVisibilityChangeEvent event = new CrazyPunisherVisibilityChangeEvent(player, false);
			getServer().getPluginManager().callEvent(event);
			if (event.isCancelled())
				continue;
			hidden.add(player.getName());
			if (dynmap != null)
				dynmap.setPlayerVisiblity(player, false);
			for (Player plr : getServer().getOnlinePlayers())
				if (plr.hasPermission("crazypunisher.showall") || isHidden(plr))
				{
					player.showPlayer(plr);
					plr.showPlayer(player);
				}
				else
					plr.hidePlayer(player);
			sendLocaleMessage("COMMAND.HIDE", player);
		}
		sendLocaleMessage("COMMAND.HIDE.DONE", sender);
	}

	@Override
	public boolean commandMain(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyCommandException
	{
		if (commandLabel.equalsIgnoreCase("jail") || commandLabel.equalsIgnoreCase("jailloc") || commandLabel.equalsIgnoreCase("jaillocation"))
		{
			commandMainJailLocation(sender, args);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("jailrange"))
		{
			commandMainJailRange(sender, args);
			return true;
		}
		return false;
	}

	private void commandMainJailLocation(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazypunisher.admin.jail") && !sender.hasPermission("crazypunisher.admin.jail.set"))
			throw new CrazyCommandPermissionException();
		World world = null;
		Location loc = null;
		switch (args.length)
		{
			case 0:
				sendLocaleMessage("JAILLOCATION", sender, jailcenter.getWorld().getName(), String.valueOf(jailcenter.getBlockX()), String.valueOf(jailcenter.getBlockY()), String.valueOf(jailcenter.getBlockZ()));
				return;
			case 4:
				if (!sender.hasPermission("crazypunisher.admin.jail.set"))
					throw new CrazyCommandPermissionException();
				world = getServer().getWorld(args[3]);
				if (world == null)
					throw new CrazyCommandNoSuchException("World", args[3]);
			case 3:
				if (!sender.hasPermission("crazypunisher.admin.jail.set"))
					throw new CrazyCommandPermissionException();
				if (world == null)
					if (sender instanceof ConsoleCommandSender)
						throw new CrazyCommandExecutorException(false);
					else
						world = ((Player) sender).getWorld();
				int x,
				y,
				z;
				try
				{
					x = Integer.parseInt(args[0]);
				}
				catch (NumberFormatException e)
				{
					throw new CrazyCommandParameterException(1, "Integer");
				}
				try
				{
					y = Integer.parseInt(args[1]);
				}
				catch (NumberFormatException e)
				{
					throw new CrazyCommandParameterException(2, "Integer");
				}
				try
				{
					z = Integer.parseInt(args[2]);
				}
				catch (NumberFormatException e)
				{
					throw new CrazyCommandParameterException(3, "Integer");
				}
				loc = new Location(world, x, y, z);
			case 1:
				if (!sender.hasPermission("crazypunisher.admin.jail.change"))
					throw new CrazyCommandPermissionException();
				if (args[0].equalsIgnoreCase("here") || args[0].equalsIgnoreCase("set"))
				{
					if (loc == null)
						if (sender instanceof ConsoleCommandSender)
							throw new CrazyCommandExecutorException(false);
						else
							loc = ((Player) sender).getLocation();
					jailcenter = loc;
					jailworld = jailcenter.getWorld();
					jailsphere.setCenter(loc);
					save();
					sendLocaleMessage("JAILLOCATION.CHANGED", sender);
					sendLocaleMessage("JAILLOCATION", sender, jailcenter.getWorld().getName(), String.valueOf(jailcenter.getBlockX()), String.valueOf(jailcenter.getBlockY()), String.valueOf(jailcenter.getBlockZ()));
					return;
				}
			default:
				throw new CrazyCommandUsageException("/crazypunisher jail", "/crazypunisher jail <set>", "/crazypunisher jail <X> <Y> <Z>", "/crazypunisher jail <X> <Y> <Z> <World>");
		}
	}

	private void commandMainJailRange(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazypunisher.admin.jailrange") && !sender.hasPermission("crazypunisher.admin.jailrange.set"))
			throw new CrazyCommandPermissionException();
		switch (args.length)
		{
			case 1:
				int range;
				try
				{
					range = Integer.parseInt(args[0]);
				}
				catch (NumberFormatException e)
				{
					throw new CrazyCommandParameterException(1, "Integer");
				}
				jailrange = range;
				jailsphere.setRadius(range);
				save();
				sendLocaleMessage("JAILRANGE.CHANGED", sender);
			case 0:
				sendLocaleMessage("JAILRANGE", sender, String.valueOf(jailrange));
				return;
			default:
				throw new CrazyCommandUsageException("/crazypunisher jailrange [Range]");
		}
	}

	public boolean isAutoBanIPEnabled()
	{
		return autoBanIP;
	}

	public boolean isBanned(final Player player)
	{
		return banned.contains(player);
	}

	public boolean isJailed(final Player player)
	{
		Date time = jailed.findDataVia1(player);
		if (time == null)
			return false;
		return time.after(new Date());
	}

	public boolean isHidden(final Player player)
	{
		return hidden.contains(player.toString());
	}

	public String getJailTime(final Player player)
	{
		return DateFormat.format(jailed.findDataVia1(player));
	}

	public boolean isInsideJail(final Location loc)
	{
		return jailsphere.isInside(loc);
	}

	public void keepJailed(final Player player)
	{
		sendLocaleMessage("MESSAGE.JAILEDUNTIL", player, getJailTime(player));
		if (player.getVehicle() != null)
			player.leaveVehicle();
		player.teleport(jailcenter);
	}
}
