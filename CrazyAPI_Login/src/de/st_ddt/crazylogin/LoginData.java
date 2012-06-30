package de.st_ddt.crazylogin;

import java.util.Date;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;

public interface LoginData
{

	public String getName();

	public Player getPlayer();

	public OfflinePlayer getOfflinePlayer();

	public void setPassword(String password) throws CrazyCommandException;

	public boolean isPassword(String password);

	public boolean isPasswordHash(String passwordHash);

	public void addIP(String ip);

	public boolean hasIP(String ip);

	public String getLatestIP();

	public Date getLastActionTime();

	public boolean isOnline();

	public boolean isPlayerOnline();

	public boolean login(String password);

	public void logout();

	public void logout(boolean removeIPs);

	public void notifyAction();

	public boolean equals(LoginData data);
}
