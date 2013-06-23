package de.st_ddt.crazyspawner.entities.meta;

import org.bukkit.metadata.MetadataValue;

import de.st_ddt.crazyspawner.CrazySpawner;

public interface AlarmMeta extends MetadataValue
{

	public static final String METAHEADER = "AlarmMeta";

	public double getAlarmRange();

	public final static class FixedAlarmMeta implements AlarmMeta
	{

		private final double alarmRange;

		public FixedAlarmMeta(final double alarmRange)
		{
			super();
			this.alarmRange = alarmRange;
		}

		@Override
		public double getAlarmRange()
		{
			return alarmRange;
		}

		@Override
		public Double value()
		{
			return alarmRange;
		}

		@Override
		public int asInt()
		{
			return (int) Math.round(alarmRange);
		}

		@Override
		public float asFloat()
		{
			return (float) alarmRange;
		}

		@Override
		public double asDouble()
		{
			return alarmRange;
		}

		@Override
		public long asLong()
		{
			return Math.round(alarmRange);
		}

		@Override
		public short asShort()
		{
			return (short) Math.round(alarmRange);
		}

		@Override
		public byte asByte()
		{
			return (byte) Math.round(alarmRange);
		}

		@Override
		public boolean asBoolean()
		{
			return alarmRange > 0;
		}

		@Override
		public String asString()
		{
			return Double.toString(alarmRange);
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
