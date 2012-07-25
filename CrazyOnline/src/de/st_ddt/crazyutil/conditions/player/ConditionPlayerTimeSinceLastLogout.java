package de.st_ddt.crazyutil.conditions.player;

import java.util.Date;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.st_ddt.crazyonline.CrazyOnline;

public class ConditionPlayerTimeSinceLastLogout extends ConditionPlayer
{

	protected long time;
	protected boolean above;

	public ConditionPlayerTimeSinceLastLogout(final ConfigurationSection config)
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
		return "TimeSinceLastLogout";
	}

	@Override
	public boolean match(final Player tester)
	{
		if (above)
			return CrazyOnline.getPlugin().getPlayerData(tester).getLastLogout().getTime() - new Date().getTime() >= time;
		else
			return CrazyOnline.getPlugin().getPlayerData(tester).getLastLogout().getTime() - new Date().getTime() < time;
	}
}
