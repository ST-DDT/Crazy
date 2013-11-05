package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Sheep;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class SheepProperty extends BasicProperty
{

	protected final boolean sheared;

	public SheepProperty()
	{
		super();
		this.sheared = false;
	}

	public SheepProperty(final ConfigurationSection config)
	{
		super(config);
		this.sheared = config.getBoolean("sheared", false);
	}

	public SheepProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final BooleanParamitrisable shearedParam = (BooleanParamitrisable) params.get("sheared");
		this.sheared = shearedParam.getValue();
	}

	@Override
	public boolean isApplicable(final Class<? extends Entity> clazz)
	{
		return Sheep.class.isAssignableFrom(clazz);
	}

	@Override
	public void apply(final Entity entity)
	{
		final Sheep sheep = (Sheep) entity;
		sheep.setSheared(sheared);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final BooleanParamitrisable shearedParam = new BooleanParamitrisable(sheared);
		params.put("s", shearedParam);
		params.put("sheared", shearedParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "sheared", sheared);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "sheared", "boolean (true/false)");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.SHEARED $Sheared$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.SHEARED", target, sheared);
	}

	@Override
	public boolean equalsDefault()
	{
		return sheared == false;
	}
}
