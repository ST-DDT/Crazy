package de.st_ddt.crazyonline;

import java.util.Set;

import de.st_ddt.crazyonline.data.OnlineData;
import de.st_ddt.crazyplugin.CrazyPlayerDataPluginInterface;

public interface OnlinePlugin<S extends OnlineData> extends CrazyPlayerDataPluginInterface<OnlineData, S>
{

	static final OnlinePluginProvider ONLINEPLUGINPROVIDER = new OnlinePluginProvider();

	public Set<S> getPlayerDatasPerIP(String IP);

	public int getAutoDelete();

	public int dropInactiveAccounts();
}
