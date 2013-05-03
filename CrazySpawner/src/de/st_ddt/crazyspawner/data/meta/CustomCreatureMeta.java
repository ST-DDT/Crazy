package de.st_ddt.crazyspawner.data.meta;

import org.bukkit.metadata.MetadataValue;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.data.CustomCreature;

public class CustomCreatureMeta implements MetadataValue
{

	public static final String METAHEADER = "CustomCreatureMeta";
	protected final CustomCreature customCreature;

	public CustomCreatureMeta(final CustomCreature customCreature)
	{
		super();
		this.customCreature = customCreature;
	}

	@Override
	public final CustomCreature value()
	{
		return customCreature;
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
		return customCreature.toString();
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
