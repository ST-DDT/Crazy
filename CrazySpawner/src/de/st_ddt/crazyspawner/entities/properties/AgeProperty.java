package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public final class AgeProperty extends BasicProperty
{

	protected final boolean baby;

	public AgeProperty()
	{
		super();
		this.baby = false;
	}

	public AgeProperty(final ConfigurationSection config)
	{
		super(config);
		this.baby = config.getBoolean("baby", false);
	}

	public AgeProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final BooleanParamitrisable babyParam = (BooleanParamitrisable) params.get("baby");
		if (babyParam == null)
			this.baby = false;
		else
			this.baby = babyParam.getValue();
	}

	@Override
	public void apply(final Entity entity)
	{
		final Ageable ageable = (Ageable) entity;
		if (baby)
			ageable.setBaby();
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final BooleanParamitrisable babyParam = new BooleanParamitrisable(baby);
		params.put("b", babyParam);
		params.put("baby", babyParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "baby", baby);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "baby", "boolean (true/false)");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.BABY $Baby$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.BABY", target, baby);
	}
}
