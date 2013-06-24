package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Slime;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.IntegerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class SlimeProperty extends BasicProperty
{

	protected final int size;

	public SlimeProperty()
	{
		super();
		this.size = -1;
	}

	public SlimeProperty(final ConfigurationSection config)
	{
		super(config);
		this.size = Math.max(config.getInt("size", -1), -1);
	}

	public SlimeProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final IntegerParamitrisable sizeParam = (IntegerParamitrisable) params.get("size");
		this.size = sizeParam.getValue();
	}

	@Override
	public void apply(final Entity entity)
	{
		final Slime slime = (Slime) entity;
		if (size > 0)
			slime.setSize(size);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final IntegerParamitrisable sizeParam = new IntegerParamitrisable(size);
		params.put("s", sizeParam);
		params.put("size", sizeParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "size", size);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "size", "int (1-x)");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.SIZE $Size$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.SIZE", target, size);
	}
}
