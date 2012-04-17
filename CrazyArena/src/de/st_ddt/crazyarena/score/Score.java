package de.st_ddt.crazyarena.score;

import java.util.HashMap;
import java.util.List;

import de.st_ddt.crazyarena.arenas.Arena;

public class Score implements Comparable<Score>
{

	protected final String name;
	protected final Arena arena;
	protected final HashMap<String, String> strings;
	protected final HashMap<String, Double> values;

	public Score(String name, Arena arena, String[] stringnames, String[] valuenames)
	{
		super();
		this.name = name;
		this.arena = arena;
		strings = new HashMap<String, String>();
		values = new HashMap<String, Double>();
		for (String string : stringnames)
			strings.put(string, "");
		for (String string : valuenames)
			values.put(string, 0d);
	}

	public String getName()
	{
		return name;
	}

	public Arena getArena()
	{
		return arena;
	}

	public String getString(String entry)
	{
		return strings.get(entry);
	}

	public void setString(String entry, String value)
	{
		strings.put(entry, value);
	}

	public Double getValue(String entry)
	{
		return values.get(entry);
	}

	public void setValue(String entry, double value)
	{
		values.put(entry, value);
	}

	public void addValue(String entry, double add)
	{
		double value = values.get(entry);
		value = value + add;
		values.put(entry, value);
	}

	public String getEntry(String column)
	{
		String string = getString(column);
		if (string == null)
			string = getValue(column).toString();
		return string;
	}

	public String[] getSignRow(String[] columns)
	{
		int length = columns.length;
		String[] row = new String[length];
		for (int i = 0; i < length; i++)
			row[i] = getEntry(columns[i]);
		return row;
	}

	public String[] getSignRow(List<String> columns)
	{
		int length = columns.size();
		String[] row = new String[length];
		for (int i = 0; i < length; i++)
			row[i] = getEntry(columns.get(i));
		return row;
	}

	@Override
	public int compareTo(Score score)
	{
		return getName().compareTo(score.getName());
	}
}
