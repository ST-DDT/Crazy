package de.st_ddt.crazylogin;

import de.st_ddt.crazylogin.data.LoginData;

public class LoginPluginProvider
{

	private LoginPlugin<? extends LoginData> plugin;

	public LoginPlugin<? extends LoginData> getPlugin()
	{
		return plugin;
	}

	protected void setPlugin(final LoginPlugin<? extends LoginData> plugin)
	{
		this.plugin = plugin;
	}
}
