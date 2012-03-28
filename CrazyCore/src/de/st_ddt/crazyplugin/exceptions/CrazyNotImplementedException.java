package de.st_ddt.crazyplugin.exceptions;

public class CrazyNotImplementedException extends CrazyException
{

	private static final long serialVersionUID = 1L;

	public String getLangPath()
	{
		return super.getLangPath() + ".NOTIMPLEMETED";
	}
}
