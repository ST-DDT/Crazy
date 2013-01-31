package de.st_ddt.crazyarena.score;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.utils.SignRotation;
import de.st_ddt.crazyplugin.data.PlayerData;
import de.st_ddt.crazyutil.ConfigurationSaveable;

public class Score implements ConfigurationSaveable
{

	protected final Collection<Location> signs = new ArrayList<Location>();
	protected final Map<String, ScoreEntry> scores = new TreeMap<String, ScoreEntry>();
	protected final Arena<?> arena;
	protected final String[] stringnames;
	protected final String[] valuenames;
	protected final Comparator<ScoreEntry> defaultComparator;
	protected final ScoreOutputModifier scoreOutputModifier;
	protected long expiringTime;

	public Score(final Arena<?> arena, final String[] stringnames, final String[] valuenames, final String compareField, final boolean reverse)
	{
		this(arena, stringnames, valuenames, compareField, reverse, ScoreOutputModifier.UNMODIFIEDSCOREOUTPUT);
	}

	public Score(final Arena<?> arena, final String[] stringnames, final String[] valuenames, final String compareField, final boolean reverse, final ScoreOutputModifier scoreOutputModifier)
	{
		super();
		this.arena = arena;
		this.stringnames = stringnames;
		this.valuenames = valuenames;
		if (reverse)
			this.defaultComparator = Collections.reverseOrder(getXComparator(compareField));
		else
			this.defaultComparator = getXComparator(compareField);
		this.scoreOutputModifier = scoreOutputModifier;
	}

	public Score(final Arena<?> arena, final String[] stringnames, final String[] valuenames, final Comparator<ScoreEntry> defaultComparator)
	{
		this(arena, stringnames, valuenames, defaultComparator, ScoreOutputModifier.UNMODIFIEDSCOREOUTPUT);
	}

	public Score(final Arena<?> arena, final String[] stringnames, final String[] valuenames, final Comparator<ScoreEntry> defaultComparator, final ScoreOutputModifier scoreOutputModifier)
	{
		super();
		this.arena = arena;
		this.stringnames = stringnames;
		this.valuenames = valuenames;
		this.defaultComparator = defaultComparator;
		this.scoreOutputModifier = scoreOutputModifier;
	}

	public Arena<?> getArena()
	{
		return arena;
	}

	public long getExpiringTime()
	{
		return expiringTime;
	}

	public void setExpiringTime(final long expiringTime)
	{
		this.expiringTime = expiringTime;
	}

	public ScoreEntry addScore(final String name)
	{
		final ScoreEntry score = new ScoreEntry(name);
		scores.put(name.toLowerCase(), score);
		return score;
	}

	public ScoreEntry getScore(final String name)
	{
		return scores.get(name.toLowerCase());
	}

	public ScoreEntry getOrAddScore(final String name)
	{
		final ScoreEntry score = getScore(name);
		if (score == null)
			return addScore(name);
		else
			return score;
	}

	public void clear()
	{
		scores.clear();
	}

	/**
	 * Get all stored score data
	 * 
	 * @return The score datas sorted by name.
	 */
	public Collection<ScoreEntry> getScores()
	{
		return scores.values();
	}

	public boolean hasStringColumn(final String name)
	{
		for (final String string : stringnames)
			if (string.equals(name))
				return true;
		return false;
	}

	public List<ScoreEntry> getXSortedScore(final String field, final boolean reverse)
	{
		return getSortedScore(new ArrayList<ScoreEntry>(scores.values()), getXComparator(field), reverse);
	}

	public List<ScoreEntry> getDefaultSortedScore(final boolean reverse)
	{
		return getSortedScore(new ArrayList<ScoreEntry>(scores.values()), getDefaultComparator(), reverse);
	}

	public List<ScoreEntry> getStringSortedScore(final String field, final boolean reverse)
	{
		return getSortedScore(new ArrayList<ScoreEntry>(scores.values()), getStringComparator(field), reverse);
	}

	public List<ScoreEntry> getDoubleSortedScore(final String field, final boolean reverse)
	{
		return getSortedScore(new ArrayList<ScoreEntry>(scores.values()), getDoubleComparator(field), reverse);
	}

	public List<ScoreEntry> getSortedScore(final List<ScoreEntry> entries, final Comparator<ScoreEntry> comparator, final boolean reverse)
	{
		if (reverse)
			Collections.sort(entries, Collections.reverseOrder(comparator));
		else
			Collections.sort(entries, comparator);
		return entries;
	}

	public Comparator<ScoreEntry> getXComparator(final String field)
	{
		if (field == null)
			return getDefaultComparator();
		if (hasStringColumn(field))
			return getStringComparator(field);
		else
			return getDoubleComparator(field);
	}

	public Comparator<ScoreEntry> getDefaultComparator()
	{
		return defaultComparator;
	}

	public Comparator<ScoreEntry> getStringComparator(final String field)
	{
		return new ScoreStringSorter(field);
	}

	public Comparator<ScoreEntry> getDoubleComparator(final String field)
	{
		return new ScoreDoubleSorter(field);
	}

	public String[][] getSignEntries(final String sort, final boolean reverse, final int entryCount, final String... columns)
	{
		final List<ScoreEntry> scores = getXSortedScore(sort, reverse);
		int scoreCount = scores.size();
		final int colCount = columns.length;
		final String[][] entries = new String[scoreCount][colCount];
		for (int i = scoreCount; i < entryCount; i++)
			Arrays.fill(entries[i], "");
		scoreCount = Math.min(scoreCount, entryCount);
		for (int i = 0; i < scoreCount; i++)
			entries[i] = scores.get(i).getSignRow(columns);
		return entries;
	}

	public String[][] getSignEntries(final String sort, final boolean reverse, final int entryCount, final List<String> columns)
	{
		final List<ScoreEntry> scores = getXSortedScore(sort, reverse);
		int scoreCount = scores.size();
		final int colCount = columns.size();
		final String[][] entries = new String[entryCount][];
		for (int i = scoreCount; i < entryCount; i++)
			entries[i] = new String[colCount];
		scoreCount = Math.min(scoreCount, entryCount);
		for (int i = 0; i < scoreCount; i++)
			entries[i] = scores.get(i).getSignRow(columns);
		return entries;
	}

	public boolean updateSign(final Location location)
	{
		final Block block = location.getBlock();
		if (block.getType() != Material.WALL_SIGN)
			return false;
		final SignRotation rotation = SignRotation.getByBytes(block.getData());
		final Vector vector = rotation.getTextVector();
		// Spalten suchen
		final List<String> columns = new ArrayList<String>();
		final Location search = location.clone();
		int depth = checkColumn(location.clone(), rotation);
		String sort = null;
		boolean reverse = false;
		columns.add("name");
		while (search.add(vector).getBlock().getType() == Material.WALL_SIGN && search.getBlock().getData() == rotation.getDirection())
		{
			final String[] lines = ((Sign) search.getBlock().getState()).getLines();
			if (!lines[0].equals(CrazyArena.ARENASIGNHEADER))
				break;
			if (lines[2].startsWith("sort"))
			{
				sort = lines[3];
				if (lines[2].equals("sortreverse"))
					reverse = true;
			}
			columns.add(lines[3]);
			depth = Math.min(depth, checkColumn(search.clone(), rotation));
		}
		// Einträge holen
		final String[][] entrylist = getSignEntries(sort, reverse, depth * 4, columns);
		// Einträge anzeigen
		final Location applier = location.clone();
		final int columnsAnz = columns.size();
		for (int i = 0; i < depth; i++)
		{
			applier.add(0, -1, 0);
			for (int j = 0; j < columnsAnz; j++)
			{
				final Sign sign = ((Sign) applier.clone().add(vector).getBlock().getState());
				sign.setLine(0, entrylist[i * 4][j]);
				sign.setLine(1, entrylist[i * 4 + 1][j]);
				sign.setLine(2, entrylist[i * 4 + 2][j]);
				sign.setLine(3, entrylist[i * 4 + 3][j]);
				sign.update();
			}
		}
		return true;
	}

	private int checkColumn(final Location location, final SignRotation rotation)
	{
		int anz = 0;
		while (location.add(0, -1, 0).getBlock().getType() == Material.WALL_SIGN && location.getBlock().getData() == rotation.getDirection())
			anz++;
		return anz;
	}

	public Collection<Location> getSigns()
	{
		return signs;
	}

	public void updateSigns()
	{
		final Iterator<Location> it = signs.iterator();
		while (it.hasNext())
			if (!updateSign(it.next()))
				it.remove();
	}

	public void updateSigns(final Collection<Location> locations)
	{
		for (final Location location : locations)
			updateSign(location);
	}

	public void updateSigns(final Location... locations)
	{
		for (final Location location : locations)
			updateSign(location);
	}

	public void load(final ConfigurationSection config)
	{
		if (config == null)
			return;
		loadScores(config.getConfigurationSection("datas"));
		final long expireTest = System.currentTimeMillis() - expiringTime;
		final Iterator<ScoreEntry> it = scores.values().iterator();
		while (it.hasNext())
			if (it.next().getLastAction() < expireTest)
				it.remove();
		loadSigns(config.getConfigurationSection("signs"));
	}

	private void loadScores(final ConfigurationSection config)
	{
		if (config == null)
			return;
		for (final String key : config.getKeys(false))
			scores.put(key, new ScoreEntry(key, config));
	}

	private void loadSigns(final ConfigurationSection config)
	{
		if (config == null)
			return;
		// EDIT loadSigns
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		final long expireTest = System.currentTimeMillis() - expiringTime;
		final Iterator<ScoreEntry> it = scores.values().iterator();
		while (it.hasNext())
			if (it.next().getLastAction() < expireTest)
				it.remove();
		if (config == null)
			return;
		for (final Entry<String, ScoreEntry> entry : scores.entrySet())
			entry.getValue().save(config, path + entry.getKey() + ".");
	}

	private class ScoreStringSorter implements Comparator<ScoreEntry>
	{

		private final String field;

		public ScoreStringSorter(final String field)
		{
			super();
			this.field = field;
		}

		@Override
		public int compare(final ScoreEntry score1, final ScoreEntry score2)
		{
			return score1.getString(field).compareTo(score2.getString(field));
		}
	}

	private class ScoreDoubleSorter implements Comparator<ScoreEntry>
	{

		private final String field;

		public ScoreDoubleSorter(final String field)
		{
			super();
			this.field = field;
		}

		@Override
		public int compare(final ScoreEntry score1, final ScoreEntry score2)
		{
			return score1.getValue(field).compareTo(score2.getValue(field));
		}
	}

	public class ScoreEntry extends PlayerData<ScoreEntry> implements Comparable<ScoreEntry>, ConfigurationSaveable
	{

		protected final Map<String, String> strings = new LinkedHashMap<String, String>();
		protected final Map<String, Double> values = new LinkedHashMap<String, Double>();
		protected long lastAction = System.currentTimeMillis();

		protected ScoreEntry(final String name)
		{
			super(name);
			for (final String string : stringnames)
				strings.put(string, "");
			strings.put("name", name);
			for (final String string : valuenames)
				values.put(string, 0d);
		}

		public ScoreEntry(final String name, final ConfigurationSection config)
		{
			super(name);
			for (final String string : stringnames)
				strings.put(string, config.getString(string, ""));
			strings.put("name", name);
			for (final String string : valuenames)
				values.put(string, config.getDouble(string, 0));
		}

		public Arena<?> getArena()
		{
			return arena;
		}

		// public ScoreList get
		public String getString(final String entry)
		{
			return strings.get(entry);
		}

		public void setString(final String entry, final String value)
		{
			lastAction = System.currentTimeMillis();
			strings.put(entry, value);
		}

		public Double getValue(final String entry)
		{
			return values.get(entry);
		}

		public void setValue(final String entry, final double value)
		{
			lastAction = System.currentTimeMillis();
			values.put(entry, value);
		}

		public boolean setValueIfHigher(final String entry, final double value)
		{
			lastAction = System.currentTimeMillis();
			if (values.get(entry) < value)
			{
				values.put(entry, value);
				return true;
			}
			else
				return false;
		}

		public boolean setValueIfLower(final String entry, final double value)
		{
			lastAction = System.currentTimeMillis();
			if (values.get(entry) > value)
			{
				values.put(entry, value);
				return true;
			}
			else
				return false;
		}

		public double addValue(final String entry, final double add)
		{
			lastAction = System.currentTimeMillis();
			double value = values.get(entry);
			value = value + add;
			values.put(entry, value);
			return value;
		}

		public String getEntry(final String column)
		{
			String string = getString(column);
			if (string == null)
				string = getValue(column).toString();
			return string;
		}

		public String getSignEntry(final String column)
		{
			String string = scoreOutputModifier.getStringOutput(column, getString(column));
			if (string == null)
				string = scoreOutputModifier.getDoubleOutput(string, getValue(column));
			return string;
		}

		public String[] getSignRow(final String[] columns)
		{
			final int length = columns.length;
			final String[] row = new String[length];
			for (int i = 0; i < length; i++)
				row[i] = getSignEntry(columns[i]);
			return row;
		}

		public String[] getSignRow(final List<String> columns)
		{
			final int length = columns.size();
			final String[] row = new String[length];
			for (int i = 0; i < length; i++)
				row[i] = getSignEntry(columns.get(i));
			return row;
		}

		@Override
		public int compareTo(final ScoreEntry score)
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

		long getLastAction()
		{
			return lastAction;
		}

		@Override
		public void save(final ConfigurationSection config, final String path)
		{
			saveScore(config, path + "datas.");
			saveSigns(config, path + "signs.");
		}

		private void saveScore(final ConfigurationSection config, final String path)
		{
			for (final Entry<String, String> entry : strings.entrySet())
				config.set(path + entry.getKey(), entry.getValue());
			for (final Entry<String, Double> entry : values.entrySet())
				config.set(path + entry.getKey(), entry.getValue());
		}

		private void saveSigns(final ConfigurationSection config, final String string)
		{
			// EDIT saveSigns
		}
	}
}
