package de.st_ddt.crazyloginrank.data;

import de.st_ddt.crazyplugin.data.PlayerDataInterface;

public interface LoginRankData<S extends LoginRankData<S>> extends PlayerDataInterface<S>, Comparable<LoginRankData<?>>
{

	public Integer getRank();
}
