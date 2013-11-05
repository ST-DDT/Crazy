package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class IronGolemProperty extends BasicProperty
{

	protected final boolean playerCreated;

	public IronGolemProperty()
	{
		super();
		this.playerCreated = false;
	}

	public IronGolemProperty(final ConfigurationSection config)
	{
		super(config);
		this.playerCreated = config.getBoolean("playerCreated", false);
	}

	public IronGolemProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final BooleanParamitrisable playerCreatedParam = (BooleanParamitrisable) params.get("playercreated");
		this.playerCreated = playerCreatedParam.getValue();
	}

	@Override
	public boolean isApplicable(final Class<? extends Entity> clazz)
	{
		return IronGolem.class.isAssignableFrom(clazz);
	}

	@Override
	public void apply(final Entity entity)
	{
		final IronGolem golem = (IronGolem) entity;
		golem.setPlayerCreated(playerCreated);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final BooleanParamitrisable playerCreatedParam = new BooleanParamitrisable(playerCreated);
		params.put("plrc", playerCreatedParam);
		params.put("plrcreated", playerCreatedParam);
		params.put("playerc", playerCreatedParam);
		params.put("playercreated", playerCreatedParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "playerCreated", playerCreated);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "playerCreated", "boolean (true/false)");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.PLAYERCREATED $PlayerCreated$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.PLAYERCREATED", target, playerCreated);
	}

	@Override
	public boolean equalsDefault()
	{
		return playerCreated == false;
	}
}
