package de.st_ddt.crazyspawner.data.meta;

import org.bukkit.metadata.MetadataValue;

import de.st_ddt.crazyspawner.CrazySpawner;

public class PeacefulMeta implements MetadataValue
{

	public final static String METAHEADER = "PeacefulMeta";
	public final static PeacefulMeta INSTANCE = new PeacefulMeta();

	public PeacefulMeta()
	{
		super();
	}

	@Override
	public Object value()
	{
		return Boolean.TRUE;
	}

	@Override
	public int asInt()
	{
		return 0;
	}

	@Override
	public float asFloat()
	{
		return 0;
	}

	@Override
	public double asDouble()
	{
		return 0;
	}

	@Override
	public long asLong()
	{
		return 0;
	}

	@Override
	public short asShort()
	{
		return 0;
	}

	@Override
	public byte asByte()
	{
		return 0;
	}

	@Override
	public boolean asBoolean()
	{
		return true;
	}

	@Override
	public String asString()
	{
		return null;
	}

	@Override
	public CrazySpawner getOwningPlugin()
	{
		return CrazySpawner.getPlugin();
	}

	@Override
	public void invalidate()
	{
	}
}
