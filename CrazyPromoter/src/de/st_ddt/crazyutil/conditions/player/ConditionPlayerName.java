package de.st_ddt.crazyutil.conditions.player;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ConditionPlayerName extends ConditionPlayer
{

	protected String name;

	public ConditionPlayerName(final String name)
	{
		super();
		this.name = name;
	}

	public ConditionPlayerName(final ConfigurationSection config)
	{
		super(config);
		this.name = config.getString("name", "Player");
	}

	@Override
	public String getTypeIdentifier()
	{
		return "PlayerName";
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		config.set(path + "name", name);
	}

	@Override
	public boolean match(final Player tester)
	{
		return tester.getName().matches(name);
	}
}
