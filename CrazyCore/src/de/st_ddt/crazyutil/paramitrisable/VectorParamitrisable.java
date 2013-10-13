package de.st_ddt.crazyutil.paramitrisable;

import java.util.regex.Pattern;

import org.bukkit.util.Vector;

import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatConverter;

public class VectorParamitrisable extends TypedParamitrisable<Vector>
{

	protected final static Pattern PATTERN_SPACE = Pattern.compile(" ");

	public VectorParamitrisable(final Vector defaultValue)
	{
		super(defaultValue);
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		value = ChatConverter.stringToVector(PATTERN_SPACE.split(parameter));
	}
}
