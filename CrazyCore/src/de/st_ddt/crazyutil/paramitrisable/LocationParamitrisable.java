package de.st_ddt.crazyutil.paramitrisable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatConverter;

public class LocationParamitrisable extends TypedParamitrisable<Location>
{

	protected final static Pattern PATTERN_SPACE = Pattern.compile(" ");
	protected final CommandSender sender;
	protected TabbedParamitrisable[] subParam;

	public LocationParamitrisable(final CommandSender sender)
	{
		this(sender instanceof Player ? simplyfyLocation(((Player) sender).getLocation()) : null, sender);
	}

	public LocationParamitrisable(final Location defaultValue, final CommandSender sender)
	{
		super(defaultValue == null ? new Location(null, 0, 0, 0) : defaultValue);
		this.sender = sender;
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		value = ChatConverter.stringToLocation(sender, PATTERN_SPACE.split(parameter));
	}

	public void addFullParams(final Map<String, ? super TabbedParamitrisable> params, final String... prefixes)
	{
		for (final String prefix : prefixes)
			params.put(prefix, this);
		addAdvancedParams(params, prefixes);
	}

	public void addAdvancedParams(final Map<String, ? super TabbedParamitrisable> params, final String... prefixes)
	{
		if (subParam == null)
			subParam = createSubParams();
		for (final String prefix : prefixes)
		{
			params.put(prefix + "w", subParam[0]);
			params.put(prefix + "world", subParam[0]);
			params.put(prefix + "x", subParam[1]);
			params.put(prefix + "y", subParam[2]);
			params.put(prefix + "z", subParam[3]);
		}
	}

	public TabbedParamitrisable[] getSubParams()
	{
		if (subParam == null)
			subParam = createSubParams();
		return subParam;
	}

	public TabbedParamitrisable[] createSubParams()
	{
		final TabbedParamitrisable[] res = new TabbedParamitrisable[4];
		res[0] = new TabbedParamitrisable()
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				value.setWorld(Bukkit.getWorld(parameter));
				if (value == null)
					throw new CrazyCommandNoSuchException("World", parameter, getWorldNames());
			}

			@Override
			public List<String> tab(final String parameter)
			{
				final List<String> res = new LinkedList<String>();
				for (final World world : Bukkit.getWorlds())
					if (world.getName().startsWith(parameter))
						res.add(world.getName());
				return res;
			}
		};
		res[1] = new TabbedParamitrisable()
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				try
				{
					value.setX(Double.parseDouble(parameter));
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(0, "Number (Double)");
				}
			}

			@Override
			public List<String> tab(final String parameter)
			{
				return new LinkedList<String>();
			}
		};
		res[2] = new TabbedParamitrisable()
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				try
				{
					value.setY(Double.parseDouble(parameter));
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(0, "Number (Double)");
				}
			}

			@Override
			public List<String> tab(final String parameter)
			{
				return new LinkedList<String>();
			}
		};
		res[3] = new TabbedParamitrisable()
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				try
				{
					value.setZ(Double.parseDouble(parameter));
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(0, "Number (Double)");
				}
			}

			@Override
			public List<String> tab(final String parameter)
			{
				return new LinkedList<String>();
			}
		};
		return res;
	}

	private String[] getWorldNames()
	{
		final List<World> worlds = Bukkit.getWorlds();
		final int length = worlds.size();
		final String[] res = new String[length];
		for (int i = 0; i < length; i++)
			res[i] = worlds.get(i).getName();
		return res;
	}

	public static Location simplyfyLocation(final Location location)
	{
		return simplyfyLocation(location, 10, 1);
	}

	public static Location simplyfyLocation(final Location location, final int precCoords, final int trimAngles)
	{
		if (precCoords > 0)
		{
			location.setX(((double) Math.round(location.getX() * precCoords)) / precCoords);
			location.setY(((double) Math.round(location.getY() * precCoords)) / precCoords);
			location.setZ(((double) Math.round(location.getZ() * precCoords)) / precCoords);
		}
		if (trimAngles > 0)
		{
			location.setYaw(Math.round(location.getYaw() / trimAngles) * trimAngles);
			location.setPitch(Math.round(location.getPitch() / trimAngles) * trimAngles);
		}
		return location;
	}
}
