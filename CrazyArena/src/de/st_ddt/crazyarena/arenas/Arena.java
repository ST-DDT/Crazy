package de.st_ddt.crazyarena.arenas;

import java.io.IOException;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.exceptions.CrazyArenaCheckExcetion;
import de.st_ddt.crazyarena.exceptions.CrazyArenaException;
import de.st_ddt.crazyarena.participants.Participant;
import de.st_ddt.crazyarena.participants.ParticipantList;
import de.st_ddt.crazyarena.participants.ParticipantType;
import de.st_ddt.crazygeo.region.RealRoom;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.poly.room.Room;

public abstract class Arena
{

	protected String name;
	protected final World world;
	protected boolean enabled;
	protected boolean edit;
	protected RealRoom<Room> region;
	protected CrazyLocale locale;
	protected final ParticipantList participants = new ParticipantList(this);
	protected FileConfiguration config;
	protected final CrazyArena plugin = CrazyArena.getPlugin();
	private String chatHeader = null;

	public Arena(FileConfiguration config)
	{
		super();
		this.name = config.getString("name");
		this.world = Bukkit.getWorld(config.getString("world"));
		this.config = config;
		this.edit = false;
		this.region = RealRoom.load(config.getConfigurationSection("region"), world);
		this.locale = CrazyArena.getPlugin().getLocale().getSecureLanguageEntry("ARENA." + name.toUpperCase());
		CrazyLocale typeLocale = CrazyArena.getPlugin().getLocale().getSecureLanguageEntry("ARENA." + getArenaTypeLocaleDefault().toUpperCase());
		this.locale.setAlternative(typeLocale);
		CrazyLocale defaultLocale = CrazyArena.getPlugin().getLocale().getSecureLanguageEntry("ARENA.DEFAULT");
		typeLocale.setAlternative(defaultLocale);
		load();
	}

	public Arena(String name, World world)
	{
		super();
		this.name = name;
		this.world = world;
		this.config = new YamlConfiguration();
		this.enabled = false;
		this.edit = false;
	}

	public final String getChatHeader()
	{
		if (chatHeader == null)
			chatHeader = ChatColor.RED + "[" + ChatColor.GREEN + "Arena_" + getName() + ChatColor.RED + "] " + ChatColor.WHITE;
		return chatHeader;
	}

	public CrazyLocale getLocale()
	{
		return locale;
	}

	public Participant getParticipant(Player player)
	{
		return participants.getParticipant(player);
	}

	public ParticipantList getParticipants()
	{
		return participants;
	}

	public ParticipantList getParticipants(ParticipantType type)
	{
		return participants.getParticipants(type);
	}

	public boolean isParticipant(Player player)
	{
		return getParticipant(player) != null;
	}

	public boolean isParticipant(Player player, ParticipantType type)
	{
		Participant participant = getParticipant(player);
		if (participant == null)
			return false;
		return participant.getParticipantType() == type;
	}

	public final void join(Player player) throws CrazyCommandException
	{
		join(player, false);
	}

	public abstract void join(Player player, boolean rejoin) throws CrazyCommandException;

	public abstract void ready(Player player) throws CrazyCommandException;

	public void team(Player player, String... team) throws CrazyCommandException
	{
		plugin.sendLocaleMessage("ARENA.TEAM.UNSUPPORTED", player);
	}

	public void spectate(Player player) throws CrazyCommandException
	{
		plugin.sendLocaleMessage("ARENA.SPECTATOR.UNSUPPORTED", player);
	}

	public final void leave(Player player) throws CrazyCommandException
	{
		leave(player, false);
	}

	public abstract void leave(Player player, boolean kicked) throws CrazyCommandException;

	public void quitgame(Player player)
	{
		Participant participant = getParticipant(player);
		if (participant != null)
			participant.setParticipantType(ParticipantType.QUITER);
		stop(null, false);
	}

	protected abstract boolean checkStart();

	public abstract void start(CommandSender sender, boolean force);

	protected abstract boolean checkFinished();

	public abstract void stop(CommandSender sender, boolean force);

	public void load()
	{
		this.enabled = config.getBoolean("enabled", false);
		this.region = RealRoom.load(config.getConfigurationSection("area"), world);
	}

	public abstract void enable();

	public abstract void disable();

	public void save()
	{
		config.set("name", name);
		config.set("type", getClass().getName());
		config.set("world", world);
		config.set("enabled", enabled);
		region.save(config, "area.", true);
		try
		{
			config.save(plugin.getDataFolder().getPath() + "/Arenas/" + name + ".yml");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public final String getName()
	{
		return name;
	}

	public abstract String getArenaType();

	protected abstract String getArenaTypeLocaleDefault();

	public final World getWorld()
	{
		return world;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled) throws CrazyArenaException
	{
		if (enabled)
			if (!checkArena(null))
				throw new CrazyArenaCheckExcetion(this);
		this.enabled = enabled;
	}

	public boolean getEditMode()
	{
		return edit;
	}

	public void setEditMode(boolean edit)
	{
		this.edit = edit;
		if (edit)
			try
			{
				setEnabled(false);
			}
			catch (CrazyArenaException e)
			{}
	}

	public CrazyArena getPlugin()
	{
		return plugin;
	}

	public abstract boolean isRunning();

	public boolean command(Player player, String commandLabel, String[] args) throws CrazyCommandException
	{
		return false;
	}

	public void addSign(Player player, Block block)
	{
		plugin.sendLocaleMessage("ARENA.SIGN.UNSUPPORTED", player);
	}

	public void sendInfo(CommandSender sender)
	{
		plugin.sendLocaleMessage("ARENA.INFO.NAME", sender, name);
		plugin.sendLocaleMessage("MESSAGE.SEPERATOR", sender);
		plugin.sendLocaleMessage("ARENA.INFO.TYPE", sender, getArenaType());
		plugin.sendLocaleMessage("ARENA.INFO.WORLD", sender, world.getName());
	}

	/**
	 * Verify whether all nessesary setting have been done, and send any error to sender
	 * 
	 * @param sender
	 */
	public abstract boolean checkArena(CommandSender sender);

	/**
	 * @return Returns the current run number. (needed for rejoins)
	 */
	public abstract int getRunNumber();

	public final void sendLocaleMessage(final String localepath, final CommandSender target, final Object... args)
	{
		sendLocaleMessage(getLocale().getLanguageEntry(localepath), target, args);
	}

	public final void sendLocaleMessage(final CrazyLocale locale, final CommandSender target, final Object... args)
	{
		target.sendMessage(getChatHeader() + ChatHelper.putArgs(locale.getLanguageText(target), args));
	}

	public final void sendLocaleMessage(final String localepath, final CommandSender[] targets, final Object... args)
	{
		sendLocaleMessage(getLocale().getLanguageEntry(localepath), targets, args);
	}

	public final void sendLocaleMessage(final CrazyLocale locale, final CommandSender[] targets, final Object... args)
	{
		for (CommandSender target : targets)
			target.sendMessage(getChatHeader() + ChatHelper.putArgs(locale.getLanguageText(target), args));
	}

	public final void sendLocaleMessage(final String localepath, final Collection<CommandSender> targets, final Object... args)
	{
		sendLocaleMessage(getLocale().getLanguageEntry(localepath), targets, args);
	}

	public final void sendLocaleMessage(final CrazyLocale locale, final Collection<CommandSender> targets, final Object... args)
	{
		for (CommandSender target : targets)
			target.sendMessage(getChatHeader() + ChatHelper.putArgs(locale.getLanguageText(target), args));
	}
}
