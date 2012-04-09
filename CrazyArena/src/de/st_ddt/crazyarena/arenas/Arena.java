package de.st_ddt.crazyarena.arenas;

import java.io.IOException;
import org.bukkit.Bukkit;
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
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyutil.geo.Geo;

public abstract class Arena
{

	protected String name;
	protected final World world;
	protected boolean enabled;
	protected boolean edit;
	protected Geo region;
	protected final ParticipantList participants = new ParticipantList(this);
	protected FileConfiguration config;
	protected final CrazyArena plugin = CrazyArena.getPlugin();

	public Arena(FileConfiguration config)
	{
		super();
		this.name = config.getString("name");
		this.world = Bukkit.getWorld(config.getString("world"));
		this.config = config;
		this.edit = false;
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
		this.region = Geo.load(config.getConfigurationSection("area"), world);
	}

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
			{
			}
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
}
