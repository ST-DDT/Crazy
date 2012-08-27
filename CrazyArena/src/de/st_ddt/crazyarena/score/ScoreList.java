package de.st_ddt.crazyarena.score;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.util.Vector;

import de.st_ddt.crazyarena.arenas.Arena;

public class ScoreList
{

	protected final TreeMap<String, Score> scores = new TreeMap<String, Score>();
	protected final Arena<?> arena;
	protected final String[] stringnames;
	protected final String[] valuenames;

	public ScoreList(final Arena<?> arena, final String[] stringnames, final String[] valuenames)
	{
		super();
		this.arena = arena;
		this.stringnames = stringnames;
		this.valuenames = valuenames;
	}

	public Arena<?> getArena()
	{
		return arena;
	}

	public Score addScore(final String name)
	{
		final Score score = new Score(name, arena, stringnames, valuenames);
		scores.put(name.toLowerCase(), score);
		return score;
	}

	public Score getScore(final String name)
	{
		return scores.get(name.toLowerCase());
	}

	/**
	 * Get all stored score data
	 * 
	 * @return The score datas sorted by name.
	 */
	public Collection<Score> getScores()
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

	public List<Score> getXSortedScore(final String name)
	{
		if (hasStringColumn(name))
			return getStringSortedScore(name);
		else
			return getDoubleSortedScore(name);
	}

	public List<Score> getStringSortedScore(final String name)
	{
		final ArrayList<Score> scores = new ArrayList<Score>(getScores());
		Collections.sort(scores, new ScoreStringSorter(name));
		return scores;
	}

	public List<Score> getDoubleSortedScore(final String name)
	{
		final ArrayList<Score> scores = new ArrayList<Score>(getScores());
		Collections.sort(scores, new ScoreDoubleSorter(name));
		return scores;
	}

	public String[][] getSignEntries(final String sort, final int entryCount, final String... columns)
	{
		final List<Score> scores = getXSortedScore(sort);
		int scoreCount = scores.size();
		final int colCount = columns.length;
		final String[][] entries = new String[entryCount][];
		for (int i = scoreCount; i < entryCount; i++)
			entries[i] = new String[colCount];
		scoreCount = Math.min(scoreCount, entryCount);
		for (int i = 0; i < scoreCount; i++)
			entries[i] = scores.get(i).getSignRow(columns);
		return entries;
	}

	public String[][] getSignEntries(final String sort, final int entryCount, final List<String> columns)
	{
		final List<Score> scores = getXSortedScore(sort);
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

	public void updateSign(final Location location)
	{
		// Ausrichtung suchen
		Vector vector = null;
		if (location.clone().add(1, 0, 0).getBlock().getType() == Material.WALL_SIGN)
			vector = new Vector(1, 0, 0);
		else if (location.clone().add(-1, 0, 0).getBlock().getType() == Material.WALL_SIGN)
			vector = new Vector(-1, 0, 0);
		else if (location.clone().add(0, 1, 0).getBlock().getType() == Material.WALL_SIGN)
			vector = new Vector(0, 1, 0);
		else if (location.clone().add(0, -1, 0).getBlock().getType() == Material.WALL_SIGN)
			vector = new Vector(0, -1, 0);
		else
			return;
		// Spalten suchen
		final ArrayList<String> columns = new ArrayList<String>();
		Location search = location.clone();
		int depth = checkColumn(location);
		String sort = "name";
		columns.add("name");
		while (search.add(vector).getBlock().getType() == Material.WALL_SIGN)
		{
			final Sign sign = (Sign) search.add(vector).getBlock().getState();
			if (sign.getLine(2).equals("sort"))
				sort = sign.getLine(3);
			columns.add(sign.getLine(3));
			depth = Math.min(depth, checkColumn(search));
		}
		// Einträge holen
		final String[][] entrylist = getSignEntries(sort, depth * 4, columns);
		// Einträge anzeigen
		final int columnsAnz = columns.size();
		location.add(0, -1, 0);
		for (int i = 0; i < depth; i++)
		{
			search = location.clone();
			search.add(0, -i, 0);
			for (int j = 0; j < columnsAnz; j++)
			{
				final Sign sign = ((Sign) search.add(vector).getBlock().getState());
				sign.setLine(0, entrylist[i * 4][j]);
				sign.setLine(1, entrylist[i * 4 + 1][j + 1]);
				sign.setLine(2, entrylist[i * 4 + 2][j + 2]);
				sign.setLine(3, entrylist[i * 4 + 3][j + 3]);
				sign.update();
			}
		}
	}

	private int checkColumn(final Location location)
	{
		final Location search = location.clone();
		int anz = -1;
		while (search.getBlock().getType() == Material.WALL_SIGN)
		{
			search.add(0, -1, 0);
			anz++;
		}
		return anz;
	}

	public void updateSignList(final List<Location> locations)
	{
		for (final Location location : locations)
			updateSign(location);
	}

	private class ScoreStringSorter implements Comparator<Score>
	{

		private final String sort;

		public ScoreStringSorter(final String sort)
		{
			super();
			this.sort = sort;
		}

		@Override
		public int compare(final Score score1, final Score score2)
		{
			return score1.getString(sort).compareTo(score2.getString(sort));
		}
	}

	private class ScoreDoubleSorter implements Comparator<Score>
	{

		private final String sort;

		public ScoreDoubleSorter(final String sort)
		{
			super();
			this.sort = sort;
		}

		@Override
		public int compare(final Score score1, final Score score2)
		{
			return score1.getValue(sort).compareTo(score2.getValue(sort));
		}
	}
}
