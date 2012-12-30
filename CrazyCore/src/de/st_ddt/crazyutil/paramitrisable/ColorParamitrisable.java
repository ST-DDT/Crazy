package de.st_ddt.crazyutil.paramitrisable;

import java.util.Collection;

import org.bukkit.ChatColor;

public class ColorParamitrisable extends EnumParamitrisable<ChatColor>
{

	public final static ChatColor[] COLORS = new ChatColor[] { ChatColor.BLACK, ChatColor.DARK_BLUE, ChatColor.DARK_GREEN, ChatColor.DARK_AQUA, ChatColor.DARK_RED, ChatColor.DARK_PURPLE, ChatColor.GOLD, ChatColor.GRAY, ChatColor.DARK_GRAY, ChatColor.BLUE, ChatColor.GREEN, ChatColor.AQUA, ChatColor.RED, ChatColor.LIGHT_PURPLE, ChatColor.YELLOW, ChatColor.WHITE };
	public final static ChatColor[] FORMATS = new ChatColor[] { ChatColor.MAGIC, ChatColor.BOLD, ChatColor.STRIKETHROUGH, ChatColor.UNDERLINE, ChatColor.ITALIC, ChatColor.RESET };

	public ColorParamitrisable(final ChatColor defaultValue, final ChatColor... values)
	{
		super("ColorCodes", defaultValue, values);
		for (final ChatColor color : values)
			this.values.put(Character.toString(color.getChar()).toUpperCase(), value);
	}

	public ColorParamitrisable(final ChatColor defaultValue, final Collection<ChatColor> values)
	{
		super("ColorCodes", defaultValue, values);
		for (final ChatColor color : values)
			this.values.put(Character.toString(color.getChar()).toUpperCase(), value);
	}

	public ColorParamitrisable(final ChatColor defaultValue, final boolean colors, final boolean formats)
	{
		super("ColorCodes", defaultValue);
		if (colors)
			for (final ChatColor color : COLORS)
			{
				values.put(color.toString().toUpperCase(), value);
				values.put(Character.toString(color.getChar()).toUpperCase(), value);
			}
		if (formats)
			for (final ChatColor color : FORMATS)
			{
				values.put(color.toString().toUpperCase(), value);
				values.put(Character.toString(color.getChar()).toUpperCase(), value);
			}
	}
}
