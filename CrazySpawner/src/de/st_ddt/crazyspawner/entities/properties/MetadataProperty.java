package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.MetadataValue;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;

public abstract class MetadataProperty extends BasicProperty implements MetadataValue
{

	public MetadataProperty()
	{
		super();
	}

	public MetadataProperty(final ConfigurationSection config)
	{
		super(config);
	}

	public MetadataProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
	}

	@Override
	public abstract void apply(final Entity entity);

	@Override
	public abstract void getCommandParams(final Map<String, ? super TabbedParamitrisable> params);

	@Override
	public abstract void save(final ConfigurationSection config, final String path);

	@Override
	public abstract void dummySave(final ConfigurationSection config, final String path);

	@Override
	public final MetadataProperty value()
	{
		return this;
	}

	@Override
	public final int asInt()
	{
		return 0;
	}

	@Override
	public final float asFloat()
	{
		return 0;
	}

	@Override
	public final double asDouble()
	{
		return 0;
	}

	@Override
	public final long asLong()
	{
		return 0;
	}

	@Override
	public final short asShort()
	{
		return 0;
	}

	@Override
	public final byte asByte()
	{
		return 0;
	}

	@Override
	public final boolean asBoolean()
	{
		return false;
	}

	@Override
	public final String asString()
	{
		return this.toString();
	}

	@Override
	public final CrazySpawner getOwningPlugin()
	{
		return CrazySpawner.getPlugin();
	}

	@Override
	public final void invalidate()
	{
	}
}
