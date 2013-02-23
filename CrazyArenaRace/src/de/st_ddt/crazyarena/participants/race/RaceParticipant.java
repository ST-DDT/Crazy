package de.st_ddt.crazyarena.participants.race;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.arenas.race.RaceArena;
import de.st_ddt.crazyarena.arenas.race.RaceStage;
import de.st_ddt.crazyarena.participants.Participant;
import de.st_ddt.crazyarena.utils.ArenaChatHelper;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.source.Localized;

public class RaceParticipant extends Participant<RaceParticipant, RaceArena>
{

	protected final Location start;
	protected RaceStage stage;

	public RaceParticipant(final Player player, final RaceArena arena, final Location start, final RaceStage stage)
	{
		super(player, arena);
		this.start = start;
		this.stage = stage;
	}

	public Location getStart()
	{
		return start;
	}

	public RaceStage getStage()
	{
		return stage;
	}

	public void setStage(final RaceStage stage)
	{
		this.stage = stage;
	}

	public void reachStage()
	{
		arena.reachStage(this, stage);
		stage = stage.getNext();
	}

	@Override
	@Localized({ "CRAZYARENA.ARENA_RACE.PLAYERINFO.STAGE $Stage$", "CRAZYARENA.ARENA_RACE.PLAYERINFO.STAGELOCATION $Location$" })
	public void showDetailed(final CommandSender target, final String chatHeader)
	{
		super.showDetailed(target, chatHeader);
		if (stage == null)
			return;
		final CrazyLocale mainLocale = arena.getLocale().getSecureLanguageEntry("PLAYERINFO");
		ChatHelper.sendMessage(target, chatHeader, mainLocale.getLanguageEntry("STAGE"), stage.toShortString());
		ChatHelper.sendMessage(target, chatHeader, mainLocale.getLanguageEntry("STAGELOCATION"), ArenaChatHelper.locationConverter(stage.getZone().getBasis()));
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
			case 3:
				return stage.getName();
			case 4:
				return ArenaChatHelper.locationConverter(stage.getZone().getBasis());
			default:
				return "";
		}
	}

	@Override
	public int getParameterCount()
	{
		return 5;
	}

	@Override
	public String toString()
	{
		return "Race" + super.toString();
	}
}
