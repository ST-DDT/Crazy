package de.st_ddt.crazyonline;

import java.util.Set;

import de.st_ddt.crazyonline.data.OnlineData;
import de.st_ddt.crazyplugin.CrazyPlayerDataPluginInterface;

public interface OnlinePlugin<S extends OnlineData> extends CrazyPlayerDataPluginInterface<OnlineData, S>
{

	public static final OnlinePluginProvider ONLINEPLUGINPROVIDER = new OnlinePluginProvider();

	public Set<S> getPlayerDatasPerIP(final String IP);

	public int getAutoDelete();

	public int dropInactiveAccounts();

	public class OnlinePluginProvider
	{

		private OnlinePlugin<? extends OnlineData> plugin;

		private OnlinePluginProvider()
		{
			super();
		}

		public OnlinePlugin<? extends OnlineData> getPlugin()
		{
			return plugin;
		}

		protected void setPlugin(final OnlinePlugin<? extends OnlineData> plugin)
		{
			this.plugin = plugin;
		}
	}
}
