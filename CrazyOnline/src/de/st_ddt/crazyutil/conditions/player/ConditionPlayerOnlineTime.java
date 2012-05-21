package de.st_ddt.crazyutil.conditions.player;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.st_ddt.crazyonline.CrazyOnline;

public class ConditionPlayerOnlineTime extends ConditionPlayer
{

	protected int time;

	public ConditionPlayerOnlineTime(ConfigurationSection config)
	{
		super(config);
		time = config.getInt("time", -1);
	}

	@Override
	public void save(ConfigurationSection config, String path)
	{
		super.save(config, path);
		config.set(path + "time", time);
	}

	@Override
	public String getTypeIdentifier()
	{
		return "OnlineTime";
	}

	@Override
	public boolean match(Player tester)
	{
		return CrazyOnline.getPlugin().getPlayerData(tester).getTimeTotal() >= time;
	}
}
