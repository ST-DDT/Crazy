package de.st_ddt.crazyutil.conditions.player;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.st_ddt.crazyonline.CrazyOnline;

public class ConditionPlayerCurrentOnlineTime extends ConditionPlayer
{

	protected int time;

	public ConditionPlayerCurrentOnlineTime(final ConfigurationSection config)
	{
		super(config);
		time = config.getInt("time", -1);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		config.set(path + "time", time);
	}

	@Override
	public String getTypeIdentifier()
	{
		return "CurrentOnlineTime";
	}

	@Override
	public boolean match(final Player tester)
	{
		return CrazyOnline.getPlugin().getPlayerData(tester).getTimeLast() >= time;
	}
}
