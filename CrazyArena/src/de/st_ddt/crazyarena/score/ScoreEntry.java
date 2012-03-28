package de.st_ddt.crazyarena.score;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.Pair;
import de.st_ddt.crazyutil.PairList;

public class ScoreEntry implements Comparable<ScoreEntry>
{

	private final Score list;
	private final String name;
	private final PairList<String, String> strings;
	private final PairList<String, Double> values;

	public ScoreEntry(String name, String[] stringnames, String[] valuenames, Score list)
	{
		super();
		this.name = name;
		strings = new PairList<String, String>();
		for (String string : stringnames)
			strings.add(string, null);
		values = new PairList<String, Double>();
		for (String string : valuenames)
			values.add(string, 0d);
		this.list = list;
	}

	public String getName()
	{
		return name;
	}

	public void add(String variable, double value)
	{
		Pair<String, Double> pair = values.findPairVia1(variable.toLowerCase());
		pair.setData2(pair.getData2() + value);
	}

	public void set(String variable, double value)
	{
		values.setDataVia1(variable, value);
	}

	public void set(String variable, String value)
	{
		strings.setDataVia1(variable, value);
	}

	public Double getValue(String variable)
	{
		return values.findDataVia1(variable);
	}

	public String getString(String variable)
	{
		return strings.findDataVia1(variable);
	}

	public String getAsString(String variable)
	{
		Object object = values.findDataVia1(variable.toLowerCase());
		if (object == null)
			object = strings.findDataVia1(variable.toLowerCase());
		if (object == null)
			return "";
		return object.toString();
	}

	public String[] getData(String[] args)
	{
		int length = args.length;
		String[] data = new String[length];
		for (int i = 0; i < length; i++)
			data[i] = getAsString(args[i]);
		return data;
	}

	@Override
	public int compareTo(ScoreEntry entry)
	{
		String compare = list.getComparator();
		return getValue(compare).compareTo(entry.getValue(compare));
	}

	public void save(ConfigurationSection config)
	{
		for (Pair<String, String> pair : strings)
			config.set("strings." + pair.getData1(), pair.getData2());
		for (Pair<String, Double> pair : values)
			config.set("double." + pair.getData1(), pair.getData2());
	}

	public void load(ConfigurationSection config)
	{
		for (String name : config.getConfigurationSection("strings").getKeys(false))
			strings.setDataVia1(name, config.getString("strings." + name, ""));
		for (String name : config.getConfigurationSection("double").getKeys(false))
			values.setDataVia1(name, config.getDouble("double." + name, 0));
	}
}
