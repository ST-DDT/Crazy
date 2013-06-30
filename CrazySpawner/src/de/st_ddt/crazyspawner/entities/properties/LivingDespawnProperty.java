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

public class LivingDespawnProperty extends DespawnProperty
{

	protected final Boolean allowDespawn;

	public LivingDespawnProperty()
	{
		super();
		this.allowDespawn = null;
	}

	public LivingDespawnProperty(final ConfigurationSection config)
	{
		super(config);
		if (config.getBoolean("allowDespawn", false) || despawnAfter > 0)
			this.allowDespawn = true;
		else if (!config.getBoolean("allowDespawn", true))
			this.allowDespawn = false;
		else
			this.allowDespawn = null;
	}

	public LivingDespawnProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final BooleanParamitrisable allowDespawnParam = (BooleanParamitrisable) params.get("allowdespawn");
		if (despawnAfter > 0)
			this.allowDespawn = true;
		else
			this.allowDespawn = allowDespawnParam.getValue();
	}

	@Override
	public void apply(final Entity entity)
	{
		super.apply(entity);
		final LivingEntity living = (LivingEntity) entity;
		if (allowDespawn != null)
			living.setRemoveWhenFarAway(allowDespawn);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		super.getCommandParams(params, sender);
		final BooleanParamitrisable allowDespawnParam = new BooleanParamitrisable(allowDespawn);
		params.put("ad", allowDespawnParam);
		params.put("allowdespawn", allowDespawnParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		if (allowDespawn == null)
			config.set(path + "allowDespawn", "default");
		else
			config.set(path + "allowDespawn", allowDespawn);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		super.dummySave(config, path);
		config.set(path + "allowDespawn", "Boolean (true/false/default)");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.ALLOWDESPAWN $AllowDespawning$")
	public void show(final CommandSender target)
	{
		super.show(target);
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.ALLOWDESPAWN", target, allowDespawn == null ? "default" : allowDespawn);
	}
}
