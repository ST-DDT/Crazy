package de.st_ddt.crazyspawner.entities.meta;

import org.bukkit.metadata.MetadataValue;

import de.st_ddt.crazyspawner.CrazySpawner;

public interface NameMeta extends MetadataValue
{

	public static final String METAHEADER = "NameMeta";

	public String getCustomName();

	public final static class FixedNameMeta implements NameMeta
	{

		private final String name;

		public FixedNameMeta(final String name)
		{
			super();
			this.name = name;
		}

		@Override
		public String getCustomName()
		{
			return name;
		}

		@Override
		public String value()
		{
			return name;
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
			return false;
		}

		@Override
		public String asString()
		{
			return name;
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
}
