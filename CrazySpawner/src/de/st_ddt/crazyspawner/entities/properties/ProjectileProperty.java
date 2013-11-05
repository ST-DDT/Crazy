package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class ProjectileProperty extends BasicProperty
{

	protected final Boolean bounce;

	public ProjectileProperty()
	{
		super();
		this.bounce = null;
	}

	public ProjectileProperty(final ConfigurationSection config)
	{
		super(config);
		if (config.getBoolean("bounce", false))
			this.bounce = true;
		else if (!config.getBoolean("bounce", true))
			this.bounce = false;
		else
			this.bounce = null;
	}

	public ProjectileProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final BooleanParamitrisable bounceParam = (BooleanParamitrisable) params.get("bounce");
		this.bounce = bounceParam.getValue();
	}

	@Override
	public boolean isApplicable(final Class<? extends Entity> clazz)
	{
		return Projectile.class.isAssignableFrom(clazz);
	}

	@Override
	public void apply(final Entity entity)
	{
		final Projectile projectile = (Projectile) entity;
		if (bounce != null)
			projectile.setBounce(bounce);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final BooleanParamitrisable bounceParam = new BooleanParamitrisable(bounce);
		params.put("bou", bounceParam);
		params.put("bounce", bounceParam);
		params.put("doesbounce", bounceParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		if (bounce == null)
			config.set(path + "bounce", "default");
		else
			config.set(path + "bounce", bounce);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "bounce", "Boolean (true/false/default)");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.PROJECTILE.BOUNCE $AllowDespawning$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.PROJECTILE.BOUNCE", target, bounce == null ? "Default" : bounce);
	}

	@Override
	public boolean equalsDefault()
	{
		return bounce == null;
	}
}
