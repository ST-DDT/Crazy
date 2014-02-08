package de.st_ddt.crazyutil.paramitrisable;

import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.poly.room.Room;

public class PolyRoomParamitrisable extends TypedParamitrisable<Room>
{

	public PolyRoomParamitrisable(final Room defaultValue)
	{
		super(defaultValue);
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		// EDIT Implementiere TypedParamitrisable<Room>.setParameter()
	}
}
