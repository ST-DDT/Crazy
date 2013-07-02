package de.st_ddt.crazyutil.paramitrisable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Color;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class RGBColorParamitrisable extends TypedParamitrisable<Color>
{

	protected final static Pattern PATTERN_SPACE = Pattern.compile(" ");
	protected final static int COLOR_BIT_MASK = 0xff;
	protected final static int COLORS_BIT_MASK = 0xffffff;

	public RGBColorParamitrisable(final Color defaultValue)
	{
		super(defaultValue);
	}

	@Override
	public void setParameter(String parameter) throws CrazyException
	{
		parameter = parameter.toLowerCase();
		if (parameter.contains(" "))
		{
			final String[] split = PATTERN_SPACE.split(parameter, 3);
			if (split.length != 3)
				throw new CrazyCommandUsageException("<Red> <Green> <Blue>", "<RGB_Hex_Code>", "<ColorName>");
			try
			{
				value = Color.fromBGR(Integer.parseInt(split[0]) & COLOR_BIT_MASK, Integer.parseInt(split[1]) & COLOR_BIT_MASK, Integer.parseInt(split[2]) & COLOR_BIT_MASK);
			}
			catch (final NumberFormatException e)
			{
				try
				{
					value = Color.fromBGR(Integer.parseInt(split[0], 16), Integer.parseInt(split[1], 16), Integer.parseInt(split[2], 16));
				}
				catch (final NumberFormatException e1)
				{
					throw new CrazyCommandUsageException("<Red> <Green> <Blue>", "<RGBCode>", "<ColorName>");
				}
			}
		}
		else
			try
			{
				value = Color.fromRGB(Integer.parseInt(parameter) & COLORS_BIT_MASK);
			}
			catch (final NumberFormatException e)
			{
				try
				{
					value = Color.fromRGB(Integer.parseInt(parameter, 16) & COLORS_BIT_MASK);
				}
				catch (final NumberFormatException e1)
				{
					if (parameter.equals("aqua"))
						this.value = Color.AQUA;
					else if (parameter.equals("black"))
						this.value = Color.BLACK;
					else if (parameter.equals("blue"))
						this.value = Color.BLUE;
					else if (parameter.equals("fuchsia"))
						this.value = Color.FUCHSIA;
					else if (parameter.equals("gray"))
						this.value = Color.GRAY;
					else if (parameter.equals("green"))
						this.value = Color.GREEN;
					else if (parameter.equals("lime"))
						this.value = Color.LIME;
					else if (parameter.equals("maroon"))
						this.value = Color.MAROON;
					else if (parameter.equals("navy"))
						this.value = Color.NAVY;
					else if (parameter.equals("olive"))
						this.value = Color.OLIVE;
					else if (parameter.equals("orange"))
						this.value = Color.ORANGE;
					else if (parameter.equals("purple"))
						this.value = Color.PURPLE;
					else if (parameter.equals("red"))
						this.value = Color.RED;
					else if (parameter.equals("silver"))
						this.value = Color.SILVER;
					else if (parameter.equals("teal"))
						this.value = Color.TEAL;
					else if (parameter.equals("white"))
						this.value = Color.WHITE;
					else if (parameter.equals("yellow"))
						this.value = Color.YELLOW;
					else
						throw new CrazyCommandUsageException("<Red> <Green> <Blue>", "<RGBCode>", "<ColorName>");
				}
			}
	}

	@Override
	public List<String> tab(String parameter)
	{
		final List<String> res = new ArrayList<String>();
		parameter = parameter.toLowerCase();
		if (parameter.contains(" "))
		{
			final String[] split = PATTERN_SPACE.split(parameter, 3);
			if (split.length > 3)
				return res;
			final String current = split[split.length - 1];
			if ("255".startsWith(current))
				res.add("255");
			else if ("ff".startsWith(current))
				res.add("ff");
			else if ("191".startsWith(current))
				res.add("191");
			else if ("bf".startsWith(current))
				res.add("bf");
			else if ("127".startsWith(current))
				res.add("127");
			else if ("7f".startsWith(current))
				res.add("7f");
			else if ("63".startsWith(current))
				res.add("63");
			else if ("3f".startsWith(current))
				res.add("3f");
			else if ("0".startsWith(current))
				res.add("0");
		}
		else
		{
			if ("255".startsWith(parameter))
				res.add("255");
			else if ("ff".startsWith(parameter))
				res.add("ff");
			else if ("191".startsWith(parameter))
				res.add("191");
			else if ("bf".startsWith(parameter))
				res.add("bf");
			else if ("127".startsWith(parameter))
				res.add("127");
			else if ("7f".startsWith(parameter))
				res.add("7f");
			else if ("63".startsWith(parameter))
				res.add("63");
			else if ("3f".startsWith(parameter))
				res.add("3f");
			else if ("0".startsWith(parameter))
				res.add("0");
			if ("ffffff".startsWith(parameter))
				res.add("ffffff");
			if ("aqua".startsWith(parameter))
				res.add("AQUA");
			if ("black".startsWith(parameter))
				res.add("BLACK");
			if ("blue".startsWith(parameter))
				res.add("BLUE");
			if ("fuchsia".startsWith(parameter))
				res.add("FUCHSIA");
			if ("gray".startsWith(parameter))
				res.add("GRAY");
			if ("green".startsWith(parameter))
				res.add("GREEN");
			if ("lime".startsWith(parameter))
				res.add("LIME");
			if ("maroon".startsWith(parameter))
				res.add("MAROON");
			if ("navy".startsWith(parameter))
				res.add("NAVY");
			if ("olive".startsWith(parameter))
				res.add("OLIVE");
			if ("orange".startsWith(parameter))
				res.add("ORANGE");
			if ("purple".startsWith(parameter))
				res.add("PURPLE");
			if ("red".startsWith(parameter))
				res.add("RED");
			if ("silver".startsWith(parameter))
				res.add("SILVER");
			if ("teal".startsWith(parameter))
				res.add("TEAL");
			if ("white".startsWith(parameter))
				res.add("WHITE");
			if ("yellow".startsWith(parameter))
				res.add("YELLOW");
		}
		return res;
	}
}
