package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.IntegerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public final class HealthProperty extends BasicProperty
{

	protected final int maxHealth;

	public HealthProperty()
	{
		super();
		this.maxHealth = -1;
	}

	public HealthProperty(final ConfigurationSection config)
	{
		super(config);
		this.maxHealth = config.getInt("maxHealth", -1);
	}

	public HealthProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final IntegerParamitrisable healthParam = (IntegerParamitrisable) params.get("maxhealth");
		this.maxHealth = healthParam.getValue();
	}

	@Override
	public void apply(final Entity entity)
	{
		if (maxHealth == -1)
			return;
		final Damageable damageable = (Damageable) entity;
		damageable.setMaxHealth(maxHealth);
		damageable.setHealth(maxHealth);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final IntegerParamitrisable maxHealth = new IntegerParamitrisable(this.maxHealth);
		params.put("h", maxHealth);
		params.put("health", maxHealth);
		params.put("maxhealth", maxHealth);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "maxHealth", maxHealth);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "maxHealth", "int (-1 = default)");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.MAXHEALTH $MaxHealth$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.MAXHEALTH", target, maxHealth);
	}
}
