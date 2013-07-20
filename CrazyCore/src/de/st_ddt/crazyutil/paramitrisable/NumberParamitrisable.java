package de.st_ddt.crazyutil.paramitrisable;

public abstract class NumberParamitrisable<S extends Number> extends TypedParamitrisable<S>
{

	public NumberParamitrisable(final S defaultValue)
	{
		super(defaultValue);
	}
}
