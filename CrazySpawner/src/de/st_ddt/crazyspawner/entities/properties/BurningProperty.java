package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.IntegerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public final class BurningProperty extends BasicProperty
{

	protected final int burning;

	public BurningProperty()
	{
		super();
		this.burning = -1;
	}

	public BurningProperty(final ConfigurationSection config)
	{
		super(config);
		this.burning = config.getInt("burning", -1);
	}

	public BurningProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final IntegerParamitrisable healthParam = (IntegerParamitrisable) params.get("burning");
		this.burning = healthParam.getValue();
	}

	@Override
	public void apply(final Entity entity)
	{
		if (burning == -1)
			return;
		entity.setFireTicks(burning);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final IntegerParamitrisable burning = new IntegerParamitrisable(this.burning);
		params.put("burn", burning);
		params.put("burning", burning);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "burning", burning);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "burning", "int (-1 = default)");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.BURNING $Burning$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.BURNING", target, burning);
	}
}
