package de.st_ddt.crazyutil.paramitrisable;

import de.st_ddt.crazyplugin.exceptions.CrazyException;

public interface Paramitrisable
{

	public void setParameter(String parameter) throws CrazyException;

	public abstract class TypedParamitrisable<S> implements Paramitrisable
	{

		protected S value;

		public TypedParamitrisable(final S defaultValue)
		{
			super();
			this.value = defaultValue;
		}

		@Override
		public abstract void setParameter(String parameter) throws CrazyException;

		public final S getValue()
		{
			return value;
		}

		@Override
		public String toString()
		{
			return getValue().toString();
		}
	}
}
