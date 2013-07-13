package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.CompatibilityHelper;
import de.st_ddt.crazyutil.paramitrisable.DoubleParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class HealthProperty extends BasicProperty
{

	protected final double maxHealth;

	protected HealthProperty()
	{
		super();
		this.maxHealth = -1;
	}

	public HealthProperty(final ConfigurationSection config)
	{
		super(config);
		this.maxHealth = config.getDouble("maxHealth", -1);
	}

	public HealthProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final DoubleParamitrisable healthParam = (DoubleParamitrisable) params.get("maxhealth");
		this.maxHealth = healthParam.getValue();
	}

	@Override
	public void apply(final Entity entity)
	{
		if (maxHealth < 0)
			return;
		final LivingEntity living = (LivingEntity) entity;
		CompatibilityHelper.setMaxHealth(living, maxHealth);
		CompatibilityHelper.setHealth(living, maxHealth);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final DoubleParamitrisable maxHealth = new DoubleParamitrisable(this.maxHealth);
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
		config.set(path + "maxHealth", "double (-1 = default)");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.MAXHEALTH $MaxHealth$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.MAXHEALTH", target, maxHealth == -1 ? "Default" : maxHealth);
	}

	@Override
	public boolean equalsDefault()
	{
		return maxHealth == -1;
	}
}
