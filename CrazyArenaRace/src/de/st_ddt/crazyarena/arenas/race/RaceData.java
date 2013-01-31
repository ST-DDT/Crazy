package de.st_ddt.crazyarena.arenas.race;

import de.st_ddt.crazyarena.participants.race.RaceParticipant;
import de.st_ddt.crazyarena.utils.ArenaChatHelper;

public class RaceData
{

	private final RaceArena arena;
	private final RaceStage stage;
	private final RaceParticipant participant;
	private final int position;
	private final long time;
	private final String timeString;

	public RaceData(final RaceArena arena, final RaceStage stage, final RaceParticipant participant, final int position, final long time)
	{
		super();
		this.arena = arena;
		this.stage = stage;
		this.participant = participant;
		this.position = position;
		this.time = time;
		this.timeString = ArenaChatHelper.timeConverter(time);
	}

	public RaceArena getArena()
	{
		return arena;
	}

	public RaceStage getStage()
	{
		return stage;
	}

	public String getName()
	{
		return participant.getName();
	}

	public RaceParticipant getParticipant()
	{
		return participant;
	}

	public int getPosition()
	{
		return position;
	}

	public long getTime()
	{
		return time;
	}

	public long getTime(final RaceData compare)
	{
		return time - compare.getTime();
	}

	public String getTimeString()
	{
		return timeString;
	}

	public String getTimeString(final RaceData compare)
	{
		return ArenaChatHelper.timeConverter(getTime(compare));
	}

	public Object[] getData()
	{
		return new Object[] { participant.getName(), position, timeString, "" };
	}

	public Object[] getData(final RaceData compare)
	{
		if (compare == null)
			return getData();
		else
			return new Object[] { participant.getName(), position, timeString, getTimeString(compare) };
	}
}
