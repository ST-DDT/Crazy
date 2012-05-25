package de.st_ddt.crazyplugin.exceptions;

public class CrazyNotImplementedException extends CrazyException
{

	private static final long serialVersionUID = 1L;

	@Override
	public String getLangPath()
	{
		return super.getLangPath() + ".NOTIMPLEMETED";
	}
}
