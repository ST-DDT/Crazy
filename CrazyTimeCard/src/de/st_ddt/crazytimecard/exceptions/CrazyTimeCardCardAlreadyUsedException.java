package de.st_ddt.crazytimecard.exceptions;

import de.st_ddt.crazyutil.source.Localized;

public class CrazyTimeCardCardAlreadyUsedException extends CrazyTimeCardCardException
{

	private static final long serialVersionUID = 1027639368506923650L;

	public CrazyTimeCardCardAlreadyUsedException()
	{
	}

	@Override
	@Localized("CRAZYTIMECARD.EXCEPTION.CARD.ALREADYUSED")
	public String getLangPath()
	{
		return super.getLangPath() + ".ALREADYUSED";
	}
}
