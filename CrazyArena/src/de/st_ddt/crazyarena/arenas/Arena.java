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
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.poly.room.Room;

public abstract class Arena
{

	protected String name;
	protected final World world;
	protected boolean enabled;
	protected boolean edit;
	protected RealRoom<? extends Room> region;
	protected CrazyLocale locale;
	protected final ParticipantList participants = new ParticipantList(this);
	protected FileConfiguration config;
	protected final CrazyArena plugin = CrazyArena.getPlugin();
	private String chatHeader = null;

	public static Arena loadArena(FileConfiguration config)
	{
		return ObjectSaveLoadHelper.load(config, Arena.class, new Class[] { FileConfiguration.class }, new Object[] { config });
	}

	private Arena(final World world)
	{
		super();
		// World
		this.world = world;
		// Locale
		this.locale = CrazyArena.getPlugin().getLocale().getSecureLanguageEntry("ARENA." + name.toUpperCase());
		final CrazyLocale typeLocale = CrazyArena.getPlugin().getLocale().getSecureLanguageEntry("ARENA_" + getArenaTypeLocaleDefault().toUpperCase());
		this.locale.setAlternative(typeLocale);
		final CrazyLocale defaultLocale = CrazyArena.getPlugin().getLocale().getSecureLanguageEntry("ARENA_DEFAULT");
		typeLocale.setAlternative(defaultLocale);
		// ChatHeader
		chatHeader = ChatColor.RED + "[" + ChatColor.GREEN + "Arena_" + getName() + ChatColor.RED + "] " + ChatColor.WHITE;
	}

	public Arena(final FileConfiguration config)
	{
		this(Bukkit.getWorld(config.getString("world")));
		this.name = config.getString("name");
		this.config = config;
		load();
	}

	public Arena(final String name, final World world)
	{
		this(world);
		this.name = name;
		this.config = new YamlConfiguration();
		this.enabled = false;
		this.edit = false;
	}

	public String getChatHeader()
	{
		return chatHeader;
	}

	public CrazyLocale getLocale()
	{
		return locale;
	}

	public Participant getParticipant(final Player player)
	{
		return participants.getParticipant(player);
	}

	public ParticipantList getParticipants()
	{
		return participants;
	}

	public ParticipantList getParticipants(final ParticipantType type)
	{
		return participants.getParticipants(type);
	}

	public boolean isParticipant(final Player player)
	{
		return getParticipant(player) != null;
	}

	public boolean isParticipant(final Player player, final ParticipantType type)
	{
		final Participant participant = getParticipant(player);
		if (participant == null)
			return false;
		return participant.getParticipantType() == type;
	}

	public final void join(final Player player) throws CrazyCommandException
	{
		join(player, false);
	}

	public abstract void join(Player player, boolean rejoin) throws CrazyCommandException;

	public abstract void ready(Player player) throws CrazyCommandException;

	public void team(final Player player, final String... team) throws CrazyCommandException
	{
		plugin.sendLocaleMessage("ARENA.TEAM.UNSUPPORTED", player);
	}

	public void spectate(final Player player) throws CrazyCommandException
	{
		plugin.sendLocaleMessage("ARENA.SPECTATOR.UNSUPPORTED", player);
	}

	public final void leave(final Player player) throws CrazyCommandException
	{
		leave(player, false);
	}

	public abstract void leave(Player player, boolean kicked) throws CrazyCommandException;

	public void quitgame(final Player player)
	{
		final Participant participant = getParticipant(player);
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
		this.edit = config.getBoolean("edit", true);
		this.region = RealRoom.load(config.getConfigurationSection("region"), world);
	}

	public abstract void enable();

	public abstract void disable();

	public void save()
	{
		config.set("name", name);
		config.set("type", getClass().getName());
		config.set("world", world);
		config.set("enabled", enabled);
		config.set("edit", edit);
		config.set("region", null);
		region.save(config, "region.", true);
	}

	public final void saveConfig()
	{
		try
		{
			config.save(plugin.getDataFolder().getPath() + "/Arenas/" + name + ".yml");
		}
		catch (final IOException e)
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

	public void setEnabled(final boolean enabled) throws CrazyArenaException
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

	public void setEditMode(final boolean edit)
	{
		this.edit = edit;
		if (edit)
			try
			{
				setEnabled(false);
			}
			catch (final CrazyArenaException e)
			{}
	}

	public CrazyArena getPlugin()
	{
		return plugin;
	}

	public abstract boolean isRunning();

	public boolean command(final Player player, final String commandLabel, final String[] args) throws CrazyCommandException
	{
		return false;
	}

	public void addSign(final Player player, final Block block)
	{
		plugin.sendLocaleMessage("ARENA.SIGN.UNSUPPORTED", player);
	}

	public void sendInfo(final CommandSender sender)
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
		ChatHelper.sendMessage(target, chatHeader, locale, args);
	}

	public final void sendLocaleMessage(final String localepath, final CommandSender[] targets, final Object... args)
	{
		sendLocaleMessage(getLocale().getLanguageEntry(localepath), targets, args);
	}

	public final void sendLocaleMessage(final CrazyLocale locale, final CommandSender[] targets, final Object... args)
	{
		ChatHelper.sendMessage(targets, getChatHeader(), locale, args);
	}

	public final void sendLocaleMessage(final String localepath, final Collection<CommandSender> targets, final Object... args)
	{
		sendLocaleMessage(getLocale().getLanguageEntry(localepath), targets, args);
	}

	public final void sendLocaleMessage(final CrazyLocale locale, final Collection<CommandSender> targets, final Object... args)
	{
		ChatHelper.sendMessage(targets, getChatHeader(), locale, args);
	}
}
