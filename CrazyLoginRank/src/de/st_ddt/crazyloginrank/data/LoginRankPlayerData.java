package de.st_ddt.crazyloginrank.data;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyloginrank.CrazyLoginRank;
import de.st_ddt.crazyloginrank.data.LoginRankData;
import de.st_ddt.crazyplugin.data.PlayerData;

public class LoginRankPlayerData extends PlayerData<LoginRankPlayerData> implements LoginRankData
{

	protected int rank;

	public LoginRankPlayerData(String name)
	{
		super(name);
		rank = 0;
	}

	public LoginRankPlayerData(String name, int rank)
	{
		super(name);
		this.rank = rank;
	}

	@Override
	public String getParameter(int index)
	{
		switch (index)
		{
			case 0:
				return getName();
			case 1:
				return getRank().toString();
			default:
				return "";
		}
	}

	@Override
	public int getParameterCount()
	{
		return 2;
	}

	@Override
	public int compareTo(LoginRankData o)
	{
		return getRank().compareTo(o.getRank());
	}

	@Override
	public Integer getRank()
	{
		return rank;
	}

	@Override
	public String toString()
	{
		return getName() + " Rank " + getRank().toString();
	}

	public CrazyLoginRank getPlugin()
	{
		return CrazyLoginRank.getPlugin();
	}

	@Override
	protected String getChatHeader()
	{
		return getPlugin().getChatHeader();
	}

	@Override
	public void showDetailed(CommandSender target, String chatHeader)
	{
		// EDIT showDetailed
	}
}
