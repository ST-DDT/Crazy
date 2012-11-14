package de.st_ddt.crazyutil.modules.login;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import de.st_ddt.crazyutil.modules.Module.Named;

@Named(name = "NoLogin")
public class NoLoginSystem implements LoginSystem
{

	@Override
	public String getName()
	{
		return "NoLogin";
	}

	@Override
	public boolean hasAccount(OfflinePlayer player)
	{
		return false;
	}

	@Override
	public boolean isLoggedIn(Player player)
	{
		return false;
	}
}
