package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.meta.CustomDamage;
import de.st_ddt.crazyutil.paramitrisable.DoubleParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public final class DamageProperty extends MetadataProperty implements CustomDamage
{

	protected final double minDamage;
	protected final double maxDamage;

	public DamageProperty()
	{
		super();
		this.minDamage = -1;
		this.maxDamage = -1;
	}

	public DamageProperty(final ConfigurationSection config)
	{
		super(config);
		final double minDamage = config.getDouble("minDamage", -1);
		final double maxDamage = config.getDouble("maxDamage", -1);
		this.minDamage = Math.max(Math.min(minDamage, maxDamage), -1);
		this.maxDamage = Math.max(Math.max(minDamage, maxDamage), -1);
	}

	public DamageProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final DoubleParamitrisable minDamageParam = (DoubleParamitrisable) params.get("mindamage");
		final DoubleParamitrisable maxDamageParam = (DoubleParamitrisable) params.get("maxdamage");
		this.minDamage = Math.max(Math.min(minDamageParam.getValue(), maxDamageParam.getValue()), -1);
		this.maxDamage = Math.max(Math.max(minDamageParam.getValue(), maxDamageParam.getValue()), -1);
	}

	@Override
	public boolean isApplicable(final Class<? extends Entity> clazz)
	{
		return LivingEntity.class.isAssignableFrom(clazz) || Projectile.class.isAssignableFrom(clazz);
	}

	@Override
	public void apply(final Entity entity)
	{
		if (minDamage >= 0)
			entity.setMetadata(CustomDamage.METAHEADER, this);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final DoubleParamitrisable minDamageParam = new DoubleParamitrisable(minDamage);
		params.put("mindamage", minDamageParam);
		final DoubleParamitrisable maxDamageParam = new DoubleParamitrisable(maxDamage);
		params.put("maxdamage", maxDamageParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "minDamage", minDamage);
		config.set(path + "maxDamage", maxDamage);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "minDamage", "double (0.0 - x.y; -1.0 = default)");
		config.set(path + "maxDamage", "double (0.0 - x.y; -1.0 = default)");
	}

	@Override
	public double getMinDamage()
	{
		return minDamage;
	}

	@Override
	public double getMaxDamage()
	{
		return maxDamage;
	}

	@Override
	public double getDamage()
	{
		return getRandom(minDamage, maxDamage);
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.DAMAGE $minDamage$ $maxDamage$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.DAMAGE", target, minDamage, maxDamage);
	}

	@Override
	public boolean equalsDefault()
	{
		return minDamage == -1;
	}
}
