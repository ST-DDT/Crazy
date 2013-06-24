package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class DespawnProperty extends BasicProperty
{

	protected final Boolean allowDespawn;

	public DespawnProperty()
	{
		super();
		this.allowDespawn = null;
	}

	public DespawnProperty(final ConfigurationSection config)
	{
		super(config);
		if (config.getBoolean("allowDespawn", false))
			this.allowDespawn = true;
		else if (!config.getBoolean("allowDespawn", true))
			this.allowDespawn = false;
		else
			this.allowDespawn = null;
	}

	public DespawnProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final BooleanParamitrisable allowDespawnParam = (BooleanParamitrisable) params.get("allowdespawn");
		this.allowDespawn = allowDespawnParam.getValue();
	}

	@Override
	public void apply(final Entity entity)
	{
		final LivingEntity living = (LivingEntity) entity;
		if (allowDespawn != null)
			living.setRemoveWhenFarAway(allowDespawn);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final BooleanParamitrisable allowDespawnParam = new BooleanParamitrisable(allowDespawn);
		params.put("ad", allowDespawnParam);
		params.put("allowdespawn", allowDespawnParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		if (allowDespawn == null)
			config.set(path + "allowDespawn", "default");
		else
			config.set(path + "allowDespawn", allowDespawn);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "allowDespawn", "Boolean (true/false/default)");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.DESPAWN $AllowDespawning$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.DESPAWN", target, allowDespawn == null ? "default" : allowDespawn);
	}
}
