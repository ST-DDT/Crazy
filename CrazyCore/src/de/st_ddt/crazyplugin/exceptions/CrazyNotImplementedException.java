package de.st_ddt.crazyplugin.exceptions;

import de.st_ddt.crazyutil.locales.Localized;

public class CrazyNotImplementedException extends CrazyException
{

	private static final long serialVersionUID = 673456693904122650L;

	@Override
	@Localized("CRAZYEXCEPTION.NOTIMPLEMENTED")
	public String getLangPath()
	{
		return super.getLangPath() + ".NOTIMPLEMENTED";
	}
}
