package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;
import java.util.Random;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;

public abstract class BasicProperty implements EntityPropertyInterface
{

	protected final static Random RANDOM = new Random();

	// reqiured
	public BasicProperty()
	{
	}

	// required
	public BasicProperty(final ConfigurationSection config)
	{
	}

	// required
	public BasicProperty(final Map<String, ? extends Paramitrisable> params)
	{
	}

	@Override
	public abstract void apply(Entity entity);

	@Override
	public abstract void show(CommandSender target);

	@Override
	public abstract void getCommandParams(Map<String, ? super TabbedParamitrisable> params, CommandSender sender);

	@Override
	public abstract void save(ConfigurationSection config, String path);

	@Override
	public abstract void dummySave(ConfigurationSection config, String path);

	protected final ItemStack saveClone(final ItemStack item)
	{
		if (item == null)
			return null;
		else
			return item.clone();
	}

	protected final int getRandom(final int min, final int max)
	{
		if (min == -1)
			return -1;
		else if (min == max)
			return min;
		else
			return RANDOM.nextInt(max - min + 1) + min;
	}

	protected final double getRandom(final double min, final double max)
	{
		if (min == -1)
			return -1;
		else if (min == max)
			return min;
		else
			return RANDOM.nextDouble() * (max - min + 1) + min;
	}

	protected final int getSecureValue(final int value)
	{
		if (value < 0)
			return -1;
		else
			return value;
	}

	protected final double getSecureValue(final double value)
	{
		if (value < 0)
			return -1;
		else
			return value;
	}
}
