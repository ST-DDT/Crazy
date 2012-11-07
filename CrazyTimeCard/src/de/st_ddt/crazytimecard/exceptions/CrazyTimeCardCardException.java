package de.st_ddt.crazytimecard.exceptions;

import de.st_ddt.crazyutil.locales.Localized;

public class CrazyTimeCardCardException extends CrazyTimeCardException
{

	private static final long serialVersionUID = 1027639368506923650L;

	public CrazyTimeCardCardException()
	{
	}

	@Override
	@Localized("CRAZYTIMECARD.EXCEPTION.CARD")
	public String getLangPath()
	{
		return super.getLangPath() + ".CARD";
	}
}
