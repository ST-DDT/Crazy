package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Explosive;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.DoubleParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public final class ExplosiveProperty extends BasicProperty
{

	protected final boolean incendary;
	protected final float yield;

	public ExplosiveProperty()
	{
		super();
		this.incendary = false;
		this.yield = -1F;
	}

	public ExplosiveProperty(final ConfigurationSection config)
	{
		super(config);
		this.incendary = config.getBoolean("incendary", false);
		this.yield = (float) config.getDouble("yield", -1D);
	}

	public ExplosiveProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final BooleanParamitrisable incendaryParam = (BooleanParamitrisable) params.get("incendary");
		this.incendary = incendaryParam.getValue();
		final DoubleParamitrisable yieldParam = (DoubleParamitrisable) params.get("yield");
		this.yield = yieldParam.getValue().floatValue();
	}

	@Override
	public void apply(final Entity entity)
	{
		final Explosive explosive = (Explosive) entity;
		explosive.setIsIncendiary(incendary);
		explosive.setYield(yield);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params)
	{
		final BooleanParamitrisable incendaryParam = new BooleanParamitrisable(incendary);
		params.put("inc", incendaryParam);
		params.put("incendary", incendaryParam);
		final DoubleParamitrisable yieldParam = new DoubleParamitrisable((double) yield);
		params.put("er", yieldParam);
		params.put("erange", yieldParam);
		params.put("explosionrange", yieldParam);
		params.put("explosion", yieldParam);
		params.put("yield", yieldParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "incendary", incendary);
		config.set(path + "yield", yield);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "incendary", "boolean (true/false)");
		config.set(path + "yield", "float (0.0-x.y)");
	}

	@Override
	@Localized({ "CRAZYSPAWNER.ENTITY.PROPERTY.INCENDARY $Incendary$", "CRAZYSPAWNER.ENTITY.PROPERTY.YIELD $Yield$" })
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.INCENDARY", target, incendary);
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.YIELD", target, yield);
	}
}
