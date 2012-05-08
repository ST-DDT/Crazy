package de.st_ddt.crazyarena.participants;

import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.utils.PlayerSaver;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public class Participant
{

	protected final Player player;
	protected final Arena arena;
	protected ParticipantType participantType = ParticipantType.NONE;
	protected PlayerSaver saver;

	public Participant(Player player, Arena arena)
	{
		super();
		this.player = player;
		this.arena = arena;
		this.saver = new PlayerSaver(player);
	}

	public final Player getPlayer()
	{
		return player;
	}

	public final Arena getArena()
	{
		return arena;
	}

	public ParticipantType getParticipantType()
	{
		return participantType;
	}

	public void setParticipantType(ParticipantType type)
	{
		this.participantType = type;
	}

	protected final void setSaver(PlayerSaver saver)
	{
		this.saver = saver;
	}

	public PlayerSaver getSaver()
	{
		return saver;
	}

	public final void sendLocaleMessage(final CrazyLocale locale, final Object... args)
	{
		player.sendMessage(arena.getChatHeader() + ChatHelper.putArgs(locale.getLanguageText(player), args));
	}
}
