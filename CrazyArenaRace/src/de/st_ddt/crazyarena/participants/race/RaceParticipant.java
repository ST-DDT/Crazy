package de.st_ddt.crazyarena.participants.race;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.arenas.race.RaceArena;
import de.st_ddt.crazyarena.arenas.race.RaceStage;
import de.st_ddt.crazyarena.participants.Participant;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.locales.Localized;

public class RaceParticipant extends Participant<RaceParticipant, RaceArena>
{

	protected final Location start;
	protected RaceStage stage;

	public RaceParticipant(final Player player, final RaceArena arena, final Location start, final RaceStage target)
	{
		super(player, arena);
		this.start = start;
		this.stage = target;
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
		stage = stage.getNext();
		// EDIT show a message here
		if (stage == null)
			arena.reachFinish(this);
	}

	@Override
	@Localized("CRAZYARENA.ARENA_RACE.PLAYERINFO.STAGE $Stage$")
	public void showDetailed(final CommandSender target, final String chatHeader)
	{
		super.showDetailed(target, chatHeader);
		final CrazyLocale mainLocale = arena.getLocale().getSecureLanguageEntry("PLAYERINFO");
		ChatHelper.sendMessage(target, chatHeader, mainLocale.getLanguageEntry("STAGE"), stage.toShortString());
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
				// EDIT this thing is missing
				return "target.location";
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
