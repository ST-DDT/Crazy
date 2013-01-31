package de.st_ddt.crazyarena.score;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.util.Vector;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.utils.SignRotation;

public class ScoreList
{

	protected final Collection<Location> signs = new ArrayList<Location>();
	protected final Map<String, Score> scores = new TreeMap<String, Score>();
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

	public Score getOrAddScore(final String name)
	{
		final Score score = getScore(name);
		if (score == null)
			return addScore(name);
		else
			return score;
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
		final List<Score> scores = new ArrayList<Score>(getScores());
		Collections.sort(scores, new ScoreStringSorter(name));
		return scores;
	}

	public List<Score> getDoubleSortedScore(final String name)
	{
		final List<Score> scores = new ArrayList<Score>(getScores());
		Collections.sort(scores, new ScoreDoubleSorter(name));
		return scores;
	}

	public String[][] getSignEntries(final String sort, final int entryCount, final String... columns)
	{
		final List<Score> scores = getXSortedScore(sort);
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
		String sort = "name";
		columns.add("name");
		while (search.add(vector).getBlock().getType() == Material.WALL_SIGN && search.getBlock().getData() == rotation.getDirection())
		{
			final String[] lines = ((Sign) search.getBlock().getState()).getLines();
			if (!lines[0].equals(CrazyArena.ARENASIGNHEADER))
				break;
			if (lines[2].equals("sort"))
				sort = lines[3];
			columns.add(lines[3]);
			depth = Math.min(depth, checkColumn(search.clone(), rotation));
		}
		// Einträge holen
		final String[][] entrylist = getSignEntries(sort, depth * 4, columns);
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
