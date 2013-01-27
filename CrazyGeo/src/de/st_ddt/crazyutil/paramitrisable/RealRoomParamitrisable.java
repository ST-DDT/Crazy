package de.st_ddt.crazyutil.paramitrisable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazygeo.region.RealRoom;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.poly.room.Room;

public class RealRoomParamitrisable extends TypedParamitrisable<RealRoom<? extends Room>>
{

	private final static Pattern PATTERN_SPACE = Pattern.compile(" ");
	private final CommandSender sender;
	private final Room defaultRoom;

	public RealRoomParamitrisable(final CommandSender sender, final Room defaultRoom)
	{
		super(null);
		this.sender = sender;
		if (sender instanceof Player)
			value = new RealRoom<Room>(defaultRoom, ((Player) sender).getLocation());
		this.defaultRoom = defaultRoom;
	}

	public RealRoomParamitrisable(final CommandSender sender, final RealRoom<? extends Room> defaultValue)
	{
		this(sender, defaultValue, defaultValue == null ? null : defaultValue.getRoom());
	}

	public RealRoomParamitrisable(final CommandSender sender, final RealRoom<? extends Room> defaultValue, final Room defaultRoom)
	{
		super(defaultValue);
		this.sender = sender;
		this.defaultRoom = defaultRoom;
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		final Map<String, TabbedParamitrisable> params = new HashMap<String, TabbedParamitrisable>();
		final PolyRoomParamitrisable room = new PolyRoomParamitrisable(defaultRoom);
		params.put("r", room);
		params.put("room", room);
		final LocationParamitrisable location = new LocationParamitrisable(sender);
		location.addFullParams(params, "", "b", "basis");
		ChatHelperExtended.readParameters(PATTERN_SPACE.split(parameter), params, room, location);
		value = new RealRoom<Room>(room.getValue(), location.getValue());
	}

	@Override
	public List<String> tab(final String parameter)
	{
		final Map<String, TabbedParamitrisable> params = new HashMap<String, TabbedParamitrisable>();
		final PolyRoomParamitrisable room = new PolyRoomParamitrisable(defaultRoom);
		params.put("r", room);
		params.put("room", room);
		final LocationParamitrisable location = new LocationParamitrisable(sender);
		location.addFullParams(params, "", "b", "basis");
		return ChatHelperExtended.tabHelp(PATTERN_SPACE.split(parameter), params, room, location);
	}
}
