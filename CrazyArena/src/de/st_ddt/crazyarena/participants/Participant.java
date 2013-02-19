package de.st_ddt.crazyarena.participants;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.utils.ArenaPlayerSaver;
import de.st_ddt.crazyplugin.data.PlayerData;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.locales.Localized;

public abstract class Participant<S extends Participant<S, T>, T extends Arena<S>> extends PlayerData<Participant<S, T>>
{

	protected final T arena;
	protected ParticipantType participantType = ParticipantType.SPECTATOR;
	protected ArenaPlayerSaver saver;

	public Participant(final Player player, final T arena)
	{
		super(player.getName());
		this.arena = arena;
		this.saver = new ArenaPlayerSaver(player);
	}

	public final T getArena()
	{
		return arena;
	}

	public ParticipantType getParticipantType()
	{
		return participantType;
	}

	public void setParticipantType(final ParticipantType type)
	{
		this.participantType = type;
	}

	protected final void setSaver(final ArenaPlayerSaver saver)
	{
		this.saver = saver;
	}

	public ArenaPlayerSaver getSaver()
	{
		return saver;
	}

	public final void sendLocaleMessage(final String localepath, final Object... args)
	{
		arena.sendLocaleMessage(localepath, getPlayer(), args);
	}

	public final void sendLocaleMessage(final CrazyLocale locale, final Object... args)
	{
		arena.sendLocaleMessage(locale, getPlayer(), args);
	}

	@Override
	@Localized({ "CRAZYARENA.PLAYERINFO.ARENA.NAME $Name$ $Type$", "CRAZYARENA.PLAYERINFO.ARENA.TYPE $Type$", "CRAZYARENA.PLAYERINFO.ARENA.STATUS $State$", "CRAZYARENA.PLAYERINFO.PARTICIPANT.TYPE $Type$" })
	public void showDetailed(final CommandSender target, final String chatHeader)
	{
		final CrazyLocale mainLocale = CrazyArena.getPlugin().getLocale().getSecureLanguageEntry("PLAYERINFO");
		ChatHelper.sendMessage(target, chatHeader, mainLocale.getLanguageEntry("ARENA.NAME"), arena.getName(), arena.getType());
		ChatHelper.sendMessage(target, chatHeader, mainLocale.getLanguageEntry("ARENA.TYPE"), arena.getType());
		ChatHelper.sendMessage(target, chatHeader, mainLocale.getLanguageEntry("ARENA.STATUS"), arena.getStatus().toString());
		ChatHelper.sendMessage(target, chatHeader, mainLocale.getLanguageEntry("PARTICIPANT.TYPE"), participantType.toString());
	}

	@Override
	public String getParameter(final CommandSender sender, final int index)
	{
		switch (index)
		{
			case 0:
				return getName();
			case 1:
				return arena.getName();
			case 2:
				return participantType.toString();
			default:
				return "";
		}
	}

	@Override
	public int getParameterCount()
	{
		return 3;
	}

	@Override
	protected String getChatHeader()
	{
		return arena.getChatHeader();
	}

	public boolean isPlayer()
	{
		return participantType.isPlayer();
	}

	public boolean isWaiting()
	{
		return participantType.isWaiting();
	}

	public boolean isReady()
	{
		return participantType == ParticipantType.READY;
	}

	public boolean isPlaying()
	{
		return participantType.isPlaying();
	}

	public boolean isSpectator()
	{
		return participantType == ParticipantType.SPECTATOR;
	}

	public boolean isJudge()
	{
		return participantType.isJudge();
	}

	public boolean isQuited()
	{
		return participantType.isQuited();
	}

	public boolean isDead()
	{
		return participantType.isDead();
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj instanceof Participant)
			return equals((Participant<?, ?>) obj);
		return false;
	}

	public boolean equals(final Participant<?, ?> obj)
	{
		return name.equals(obj.getName()) && arena.equals(obj.getArena()) && participantType == obj.getParticipantType();
	}

	@Override
	public int compareTo(final Participant<S, T> o)
	{
		return getName().compareTo(o.getName());
	}

	@Override
	public String toString()
	{
		return "Participant " + name + " {Arena: " + arena.getName() + "; Type: " + participantType.toString() + "}";
	}
}
