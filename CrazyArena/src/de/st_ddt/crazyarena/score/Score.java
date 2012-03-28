package de.st_ddt.crazyarena.score;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.configuration.ConfigurationSection;

public class Score
{

	private final ArrayList<ScoreEntry> list = new ArrayList<ScoreEntry>();
	private final String comparator;
	private final String[] stringnames;
	private final String[] valuenames;

	public Score(String comparator, String[] stringnames, String[] valuenames)
	{
		super();
		this.comparator = comparator;
		this.stringnames = stringnames;
		this.valuenames = valuenames;
	}

	/**
	 * Garantiert das dieser Eintrag existiert
	 * 
	 * @param name
	 *            Name des Spielers/Eintrages
	 * @return
	 */
	public ScoreEntry addNewEntry(String name)
	{
		name = name.toLowerCase();
		for (ScoreEntry entry : list)
			if (entry.getName().equals(name))
				return entry;
		ScoreEntry add = new ScoreEntry(name, stringnames, valuenames, this);
		list.add(add);
		return add;
	}

	private void sort(String sort)
	{
		Collections.sort(list);
	}

	protected final String getComparator()
	{
		return comparator;
	}

	public void clear()
	{
		list.clear();
	}

	public ScoreEntry getEntry(String name)
	{
		for (ScoreEntry entry : list)
			if (entry.getName().equalsIgnoreCase(name))
				return entry;
		return null;
	}

	public ArrayList<String[]> getEntries(String[] args, String sort)
	{
		ArrayList<String[]> entries = new ArrayList<String[]>();
		sort(sort);
		for (ScoreEntry entry : list)
			entries.add(entry.getData(args));
		return entries;
	}

	public void save(ConfigurationSection config)
	{
		for (ScoreEntry entry : list)
			entry.save(config.createSection(entry.getName()));
	}

	public void load(ConfigurationSection config)
	{
		clear();
		for (String name : config.getKeys(false))
			addNewEntry(name).load(config.getConfigurationSection(name));
	}

	public boolean hasEntry(String variable)
	{
		for (String string : stringnames)
			if (string.equals(variable))
				return true;
		for (String string : valuenames)
			if (string.equals(variable))
				return true;
		return false;
	}
}
