package de.st_ddt.crazytimecard.exceptions;

import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.source.Localized;

public class CrazyTimeCardException extends CrazyException
{

	private static final long serialVersionUID = 1027639368506923650L;

	public CrazyTimeCardException()
	{
	}

	@Override
	@Localized("CRAZYTIMECARD.EXCEPTION")
	public String getLangPath()
	{
		return "CRAZYTIMECARD.EXCEPTION";
	}
}
