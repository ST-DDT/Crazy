package de.st_ddt.crazylogin.data;

public class LoginDataNameComparator implements LoginDataComparator
{

	@Override
	public int compare(final LoginData o1, final LoginData o2)
	{
		return o1.getName().compareTo(o2.getName());
	}
}
