package de.st_ddt.crazyarena.score;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyplugin.data.PlayerData;

public class Score extends PlayerData<Score> implements Comparable<Score>
{

	protected final Arena<?> arena;
	protected final Map<String, String> strings = new LinkedHashMap<String, String>();
	protected final Map<String, Double> values = new LinkedHashMap<String, Double>();

	public Score(final String name, final Arena<?> arena, final String[] stringnames, final String[] valuenames)
	{
		super(name);
		this.arena = arena;
		strings.put("name", name);
		for (final String string : stringnames)
			strings.put(string, "");
		for (final String string : valuenames)
			values.put(string, 0d);
	}

	public Arena<?> getArena()
	{
		return arena;
	}

	public String getString(final String entry)
	{
		return strings.get(entry);
	}

	public void setString(final String entry, final String value)
	{
		strings.put(entry, value);
	}

	public Double getValue(final String entry)
	{
		return values.get(entry);
	}

	public void setValue(final String entry, final double value)
	{
		values.put(entry, value);
	}

	public void addValue(final String entry, final double add)
	{
		double value = values.get(entry);
		value = value + add;
		values.put(entry, value);
	}

	public String getEntry(final String column)
	{
		String string = getString(column);
		if (string == null)
			string = getValue(column).toString();
		return string;
	}

	public String[] getSignRow(final String[] columns)
	{
		final int length = columns.length;
		final String[] row = new String[length];
		for (int i = 0; i < length; i++)
			row[i] = getEntry(columns[i]);
		return row;
	}

	public String[] getSignRow(final List<String> columns)
	{
		final int length = columns.size();
		final String[] row = new String[length];
		for (int i = 0; i < length; i++)
			row[i] = getEntry(columns.get(i));
		return row;
	}

	@Override
	public int compareTo(final Score score)
	{
		return getName().compareTo(score.getName());
	}

	@Override
	public void showDetailed(final CommandSender target, final String chatHeader)
	{
		// EDIT Automatisch generierter Methodenstub
	}

	@Override
	public String getParameter(final CommandSender sender, final int index)
	{
		switch (index)
		{
			case 0:
				return getName();
			case 1:
				return arena.getName();
			default:
				return "";
		}
	}

	@Override
	public int getParameterCount()
	{
		return 2;
	}

	@Override
	protected String getChatHeader()
	{
		return arena.getChatHeader();
	}
}
