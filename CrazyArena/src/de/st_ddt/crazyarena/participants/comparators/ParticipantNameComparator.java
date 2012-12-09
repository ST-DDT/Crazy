package de.st_ddt.crazyarena.participants.comparators;

import de.st_ddt.crazyarena.participants.Participant;

public class ParticipantNameComparator implements ParticipantComparator
{

	@Override
	public int compare(final Participant<?, ?> o1, final Participant<?, ?> o2)
	{
		return o1.getName().compareTo(o2.getName());
	}
}
