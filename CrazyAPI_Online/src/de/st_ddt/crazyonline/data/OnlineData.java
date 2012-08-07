package de.st_ddt.crazyonline.data;

import java.util.Date;

import de.st_ddt.crazyplugin.data.PlayerDataInterface;

public interface OnlineData<S extends OnlineData<S>> extends PlayerDataInterface<S>
{

	public Date getFirstLogin();

	public String getFirstLoginString();

	public Date getLastLogin();

	public String getLastLoginString();

	public Date getLastLogout();

	public String getLastLogoutString();

	public long getTimeLast();

	public long getTimeTotal();

	public void resetOnlineTime();

	public String getLatestIP();
}
