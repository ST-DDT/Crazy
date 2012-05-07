package de.st_ddt.crazyarena.pve;

import java.util.HashSet;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.classes.ParticipantClass;
import de.st_ddt.crazyarena.participants.Participant;
import de.st_ddt.crazyarena.participants.ParticipantPvE;
import de.st_ddt.crazyarena.participants.ParticipantType;
import de.st_ddt.crazyarena.rounds.RoundTree;
import de.st_ddt.crazyarena.utils.SpawnList;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;
import de.st_ddt.crazyutil.geo.Geo;

public class ArenaPvE extends Arena
{

	protected Geo arena;
	protected Geo lobby;
	protected final SpawnList monsterspawns = new SpawnList(world);
	protected final SpawnList arenaspawns = new SpawnList(world);
	protected final SpawnList lobbyspawns = new SpawnList(world);
	protected final SpawnList spectatorspawns = new SpawnList(world);
	protected final HashSet<Creature> monsters = new HashSet<Creature>();
	protected final RoundTree rounds = new RoundTree(this);
	protected ParticipantClass autoPartipantClass;
	protected boolean running;
	protected int round;
	private long delayTime;
	private ArenaPvEDelayed delayTimer;
	private boolean clearWaves;
	private long roundTime;
	private boolean autoStartNext;
	protected int run;
	// Listener
	private ArenaPvEEntityListener entityListener;

	public ArenaPvE(FileConfiguration config)
	{
		super(config);
		arena = Geo.load(config.getConfigurationSection("arena"), world);
		lobby = Geo.load(config.getConfigurationSection("lobby"), world);
		for (String name : config.getConfigurationSection("monsterspawns").getKeys(false))
			arenaspawns.add(ObjectSaveLoadHelper.loadLocation(config.getConfigurationSection("monsterspawns." + name), world));
		for (String name : config.getConfigurationSection("arenaspawns").getKeys(false))
			arenaspawns.add(ObjectSaveLoadHelper.loadLocation(config.getConfigurationSection("arenaspawns." + name), world));
		for (String name : config.getConfigurationSection("lobbyspawns").getKeys(false))
			arenaspawns.add(ObjectSaveLoadHelper.loadLocation(config.getConfigurationSection("lobbyspawns." + name), world));
		for (String name : config.getConfigurationSection("spectatorspawns").getKeys(false))
			arenaspawns.add(ObjectSaveLoadHelper.loadLocation(config.getConfigurationSection("spectatorspawns." + name), world));
		rounds.load(config.getConfigurationSection("rounds"));
	}

	public ArenaPvE(String name, World world)
	{
		super(name, world);
	}

	@Override
	public void join(Player player, boolean rejoin) throws CrazyCommandException
	{
		if (!rejoin && isRunning())
			throw new CrazyCommandCircumstanceException("when arena is idle");
		plugin.sendLocaleMessage("ARENAMONSTERFIGHT.JOIN.WELCOME", player, name);
		plugin.sendLocaleMessage("ARENAMONSTERFIGHT.JOIN.WELCOME.OTHERS", participants.getPlayers(), player.getName());
		ParticipantPvE participant = (ParticipantPvE) participants.getParticipant(player);
		if (participant == null)
		{
			participant = new ParticipantPvE(player, this);
			participants.add(participant);
		}
		participant.getSaver().backup();
		participant.getSaver().clear();
		if (participant.getParticipantClass() == null)
			if (autoPartipantClass == null)
				plugin.sendLocaleMessage("ARENAMONSTERFIGHT.JOIN.SELECTCLASS", player);
			else
				participant.setParticipantClass(autoPartipantClass);
		if (participant.getParticipantType() == ParticipantType.NONE || !isRunning())
			participant.setParticipantType(ParticipantType.WAITING);
		else
			participant.setParticipantType(ParticipantType.PARTICIPANT);
	}

	@Override
	public void ready(Player player) throws CrazyCommandException
	{
		ParticipantPvE participant = (ParticipantPvE) participants.getParticipant(player);
		if (participant.getParticipantClass() == null)
			throw new CrazyCommandCircumstanceException("when a Class is selected!");
		participant.setParticipantType(ParticipantType.PARTICIPANT);
		start(null, false);
	}

	@Override
	public void leave(Player player, boolean kicked) throws CrazyCommandException
	{
		if (isRunning() && !kicked)
			throw new CrazyCommandCircumstanceException("when match is over");
		ParticipantPvE participant = (ParticipantPvE) participants.getParticipant(player);
		participant.getSaver().restore();
		participants.remove(participant);
		player.teleport(spectatorspawns.get(0));
		stop(null, false);
	}

	@Override
	protected boolean checkStart()
	{
		if (running)
			return false;
		if (participants.getParticipants(ParticipantType.WAITING).size() != 0)
			return false;
		return true;
	}

	@Override
	public void start(CommandSender sender, boolean force)
	{
		if (isRunning())
			return;
		if (!checkStart() && !force)
			return;
		for (Participant participant : participants.getParticipants(ParticipantType.WAITING))
			try
			{
				leave(participant.getPlayer(), true);
			}
			catch (CrazyCommandException e)
			{}
		running = true;
		round = 0;
		registerGameHooks();
		run++;
		startRound();
	}

	private void registerGameHooks()
	{
		unregisterGameHooks();
		PluginManager pm = plugin.getServer().getPluginManager();
		entityListener = new ArenaPvEEntityListener(this);
		pm.registerEvents(entityListener, plugin);
	}

	private void unregisterGameHooks()
	{
		if (entityListener != null)
			HandlerList.unregisterAll(entityListener);
		entityListener = null;
	}

	public void startRound()
	{
		if (delayTimer instanceof ArenaPvEDelayedNext)
			((ArenaPvEDelayedNext) delayTimer).cancel();
		if (clearWaves)
			for (Creature entity : monsters)
				entity.remove();
		if (delayTime == 0)
		{
			delayedStartRound();
			return;
		}
		delayTimer = new ArenaPvEDelayedRun(this);
		plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, delayTimer, delayTime);
		if (round != 0)
			plugin.sendLocaleMessage("ARENAMONSTERFIGHT.ROUND.FINSIHED", participants.getPlayers(), round);
	}

	public void delayedStartRound()
	{
		round++;
		rounds.getRound(round).activate(round);
		plugin.sendLocaleMessage("ARENAMONSTERFIGHT.ROUND.STARTED", participants.getPlayers(), round);
		if (autoStartNext)
		{
			delayTimer = new ArenaPvEDelayedNext(this);
			plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, delayTimer, roundTime);
		}
		else
			delayTimer = null;
	}

	@Override
	protected boolean checkFinished()
	{
		if (isRunning() && participants.getParticipants(ParticipantType.PARTICIPANT).size() == 0)
			return true;
		return false;
	}

	@Override
	public void stop(CommandSender sender, boolean force)
	{
		if (!checkFinished() && !force)
			return;
		running = false;
		unregisterGameHooks();
		for (Participant participant : participants)
		{
			if (participant.getParticipantType() != ParticipantType.PARTICIPANT)
				continue;
			ParticipantPvE participantPvE = (ParticipantPvE) participant;
			participantPvE.getSaver().restore();
			participantPvE.giveRewards();
			participant.setParticipantType(ParticipantType.SPECTATOR);
			spectatorspawns.teleport(participant.getPlayer());
		}
		for (Creature creature : monsters)
			creature.remove();
	}

	@Override
	public String getArenaType()
	{
		return "Monster Fight (PvE)";
	}

	@Override
	public boolean isRunning()
	{
		return running;
	}

	@Override
	public boolean checkArena(CommandSender sender)
	{
		if (arena == null)
		{
			plugin.sendLocaleMessage("ARENAMONSTERFIGHT.CHECK.NOARENAREGION", sender);
			return false;
		}
		if (lobby == null)
		{
			plugin.sendLocaleMessage("ARENAMONSTERFIGHT.CHECK.NOLOBBYREGION", sender);
			return false;
		}
		if (arenaspawns.size() == 0)
		{
			plugin.sendLocaleMessage("ARENAMONSTERFIGHT.CHECK.NOARENASPAWNS", sender);
			return false;
		}
		if (lobbyspawns.size() == 0)
		{
			plugin.sendLocaleMessage("ARENAMONSTERFIGHT.CHECK.NOLOBBYSPAWNS", sender);
			return false;
		}
		return true;
	}

	public SpawnList getActiveMonsterSpawns()
	{
		return monsterspawns;
	}

	public void addEnemy(Creature entity)
	{
		monsters.add(entity);
	}

	public boolean containsEnemy(Entity entity)
	{
		return monsters.contains(entity);
	}

	public void removeEnemy(Entity entity)
	{
		if (monsters.remove(entity))
			if (monsters.size() == 0)
				startRound();
	}

	public void PlayerDeath(final Player player)
	{
		ParticipantPvE participant = (ParticipantPvE) getParticipants().getParticipant(player);
		if (participant == null)
			return;
		if (participant.getParticipantType() != ParticipantType.PARTICIPANT)
			return;
		participant.setParticipantType(ParticipantType.SPECTATOR);
		spectatorspawns.teleport(player);
		stop(null, false);
	}

	@Override
	public void enable()
	{
		// EDIT registerArenaHooks();
	}

	@Override
	public void disable()
	{
		unregisterGameHooks();
		// unregisterArenaHooks();
	}

	@Override
	public int getRunNumber()
	{
		return run;
	}
}
