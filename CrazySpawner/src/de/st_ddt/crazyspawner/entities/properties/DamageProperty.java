package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.meta.CustomDamage;
import de.st_ddt.crazyutil.paramitrisable.IntegerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public final class DamageProperty extends MetadataProperty implements CustomDamage
{

	protected final int minDamage;
	protected final int maxDamage;

	public DamageProperty()
	{
		super();
		this.minDamage = -1;
		this.maxDamage = -1;
	}

	public DamageProperty(final ConfigurationSection config)
	{
		super(config);
		final int minDamage = config.getInt("minDamage", -1);
		final int maxDamage = config.getInt("maxDamage", -1);
		this.minDamage = Math.max(Math.min(minDamage, maxDamage), -1);
		this.maxDamage = Math.max(Math.max(minDamage, maxDamage), -1);
	}

	public DamageProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final IntegerParamitrisable minDamageParam = (IntegerParamitrisable) params.get("minDamage");
		final IntegerParamitrisable maxDamageParam = (IntegerParamitrisable) params.get("maxDamage");
		this.minDamage = Math.max(Math.min(minDamageParam.getValue(), maxDamageParam.getValue()), -1);
		this.maxDamage = Math.max(Math.max(minDamageParam.getValue(), maxDamageParam.getValue()), -1);
	}

	@Override
	public void apply(final Entity entity)
	{
		if (minDamage >= -1)
			entity.setMetadata(CustomDamage.METAHEADER, this);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params)
	{
		final IntegerParamitrisable minDamageParam = new IntegerParamitrisable(minDamage);
		params.put("minDamage", minDamageParam);
		final IntegerParamitrisable maxDamageParam = new IntegerParamitrisable(maxDamage);
		params.put("maxDamage", maxDamageParam);
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
		config.set(path + "minDamage", "int (-1 = default)");
		config.set(path + "maxDamage", "int (-1 = default)");
	}

	@Override
	public int getMinDamage()
	{
		return minDamage;
	}

	@Override
	public int getMaxDamage()
	{
		return maxDamage;
	}

	@Override
	public int getDamage()
	{
		return getRandom(minDamage, maxDamage);
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.DAMAGE $minDamage$ $maxDamage$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.DAMAGE", target, minDamage, maxDamage);
	}
}
