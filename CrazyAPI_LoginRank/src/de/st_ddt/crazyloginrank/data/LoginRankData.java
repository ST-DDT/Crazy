package de.st_ddt.crazyloginrank.data;

import de.st_ddt.crazyplugin.data.PlayerDataInterface;

public interface LoginRankData extends PlayerDataInterface, Comparable<LoginRankData>
{

	public Integer getRank();
}
