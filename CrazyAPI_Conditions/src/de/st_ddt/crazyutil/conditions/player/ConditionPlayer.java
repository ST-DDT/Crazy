package de.st_ddt.crazyutil.conditions.player;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.st_ddt.crazyutil.conditions.ConditionBase;

public abstract class ConditionPlayer extends ConditionBase<Player>
{

	public ConditionPlayer(ConfigurationSection config)
	{
		super(config);
	}

	public ConditionPlayer()
	{
		super();
	}

	@Override
	public String getTypeIdentifier()
	{
		return "Player";
	}
}
