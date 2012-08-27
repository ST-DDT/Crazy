package de.st_ddt.crazyarena.participants.comparators;

import de.st_ddt.crazyarena.participants.Participant;

public class ParticipantTypeComparator implements ParticipantComparator
{

	@Override
	public int compare(Participant<?, ?> o1, Participant<?, ?> o2)
	{
		return o1.getParticipantType().compareTo(o2.getParticipantType());
	}
}
