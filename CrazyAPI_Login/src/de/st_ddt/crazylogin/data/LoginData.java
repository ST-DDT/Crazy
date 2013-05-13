package de.st_ddt.crazylogin.data;

import java.util.Date;

import de.st_ddt.crazylogin.exceptions.PasswordRejectedException;
import de.st_ddt.crazyplugin.data.PlayerDataInterface;

public interface LoginData extends PlayerDataInterface
{

	public void setPassword(String password) throws PasswordRejectedException;

	public boolean isPassword(String password);

	public boolean isPasswordHash(String passwordHash);

	public void addIP(String ip);

	public boolean hasIP(String ip);

	public boolean isLatestIP(String ip);

	public String getLatestIP();

	public Date getLastActionTime();

	public boolean isLoggedIn();

	public boolean login(String password);

	public void logout();

	public void logout(boolean removeIPs);

	public void notifyAction();

	public boolean equals(LoginData data);
}
