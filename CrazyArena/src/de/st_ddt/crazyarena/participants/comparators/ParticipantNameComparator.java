package de.st_ddt.crazyarena.participants.comparators;

import de.st_ddt.crazyarena.participants.Participant;

public class ParticipantNameComparator implements ParticipantComparator
{

	public int compare(Participant<?, ?> o1, Participant<?, ?> o2)
	{
		return o1.getName().compareTo(o2.getName());
	}
}
