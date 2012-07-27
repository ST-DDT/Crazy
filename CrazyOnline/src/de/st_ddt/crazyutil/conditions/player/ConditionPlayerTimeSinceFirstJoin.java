package de.st_ddt.crazyutil.conditions.player;

import java.util.Date;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.st_ddt.crazyonline.CrazyOnline;

public class ConditionPlayerTimeSinceFirstJoin extends ConditionPlayer
{

	protected long time;
	protected boolean above;

	public ConditionPlayerTimeSinceFirstJoin(final ConfigurationSection config)
	{
		super(config);
		time = config.getLong("time", -1);
		above = config.getBoolean("above", true);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		config.set(path + "time", time);
		config.set(path + "above", above);
	}

	@Override
	public String getTypeIdentifier()
	{
		return "TimeSinceFirstJoin";
	}

	@Override
	public boolean match(final Player tester)
	{
		if (above)
			return new Date().getTime() - CrazyOnline.getPlugin().getPlayerData(tester).getFirstLogin().getTime() >= time;
		else
			return new Date().getTime() - CrazyOnline.getPlugin().getPlayerData(tester).getFirstLogin().getTime() < time;
	}
}
