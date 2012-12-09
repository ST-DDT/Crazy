package de.st_ddt.crazyarena.participants.comparators;

import de.st_ddt.crazyarena.participants.Participant;

public class ParticipantTypeAndNameComparator extends ParticipantTypeComparator
{

	@Override
	public int compare(final Participant<?, ?> o1, final Participant<?, ?> o2)
	{
		int res = super.compare(o1, o2);
		if (res == 0)
			res = o1.getName().compareTo(o2.getName());
		return res;
	}
}
