package de.st_ddt.crazyspawner.entities.meta;

import org.bukkit.metadata.MetadataValue;

import de.st_ddt.crazyspawner.CrazySpawner;

public interface DetectionMeta extends MetadataValue
{

	public static final String METAHEADER = "DetectionMeta";

	public double getDetectionRange();

	public final static class FixedDetectionMeta implements DetectionMeta
	{

		private final double detectionRange;

		public FixedDetectionMeta(final double detectionRange)
		{
			super();
			this.detectionRange = detectionRange;
		}

		@Override
		public double getDetectionRange()
		{
			return detectionRange;
		}

		@Override
		public Double value()
		{
			return detectionRange;
		}

		@Override
		public int asInt()
		{
			return (int) Math.round(detectionRange);
		}

		@Override
		public float asFloat()
		{
			return (float) detectionRange;
		}

		@Override
		public double asDouble()
		{
			return detectionRange;
		}

		@Override
		public long asLong()
		{
			return Math.round(detectionRange);
		}

		@Override
		public short asShort()
		{
			return (short) Math.round(detectionRange);
		}

		@Override
		public byte asByte()
		{
			return (byte) Math.round(detectionRange);
		}

		@Override
		public boolean asBoolean()
		{
			return detectionRange > 0;
		}

		@Override
		public String asString()
		{
			return Double.toString(detectionRange);
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
