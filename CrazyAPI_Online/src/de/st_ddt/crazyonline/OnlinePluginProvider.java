package de.st_ddt.crazyonline;

import de.st_ddt.crazyonline.data.OnlineData;

public class OnlinePluginProvider
{

	private OnlinePlugin<? extends OnlineData> plugin;

	public OnlinePlugin<? extends OnlineData> getPlugin()
	{
		return plugin;
	}

	protected void setPlugin(OnlinePlugin<? extends OnlineData> plugin)
	{
		this.plugin = plugin;
	}
}
