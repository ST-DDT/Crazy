package de.st_ddt.crazyarena.score;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.util.Vector;

import de.st_ddt.crazyarena.arenas.Arena;

public class ScoreList
{

	protected final HashMap<String, Score> scores = new HashMap<String, Score>();
	protected final Arena arena;
	protected final String[] stringnames;
	protected final String[] valuenames;
	protected boolean modded = false;

	public ScoreList(Arena arena, String[] stringnames, String[] valuenames)
	{
		super();
		this.arena = arena;
		this.stringnames = stringnames;
		this.valuenames = valuenames;
	}

	public Score addScore(String name)
	{
		Score score = new Score(name, arena, stringnames, valuenames);
		scores.put(name, score);
		return score;
	}

	public Score getScore(String name)
	{
		return scores.get(name);
	}

	public Score[] getScore()
	{
		return scores.values().toArray(new Score[0]);
	}

	public Score[] getSortedScore()
	{
		Score[] scores = getScore();
		Arrays.sort(scores);
		return scores;
	}

	public boolean hasStringColumn(String name)
	{
		for (String string : stringnames)
			if (string.equals(name))
				return true;
		return false;
	}

	public Score[] getXSortedScore(String name)
	{
		if (hasStringColumn(name))
			return getStringSortedScore(name);
		else
			return getDoubleSortedScore(name);
	}

	public Score[] getStringSortedScore(String name)
	{
		Score[] scores = getScore();
		Arrays.sort(scores, new ScoreStringSorter(name));
		return scores;
	}

	public Score[] getDoubleSortedScore(String name)
	{
		Score[] scores = getScore();
		Arrays.sort(scores, new ScoreDoubleSorter(name));
		return scores;
	}

	public String[][] getSignEntries(int anz, String sort, String[] columns)
	{
		String[][] entries = new String[anz][];
		Score[] scores = getXSortedScore(sort);
		for (int i = 0; i < anz; i++)
			entries[i] = scores[i].getSignRow(columns);
		return entries;
	}

	public void updateSign(Location location)
	{
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
		ArrayList<String> columns = new ArrayList<String>();
		Location search = location.clone();
		int depth = checkColumn(location);
		columns.add("name");
		while (search.add(vector).getBlock().getType() == Material.WALL_SIGN)
		{
			String[] lines = ((Sign) search.add(vector).getBlock()).getLines();
			columns.add(lines[3]);
			depth = Math.min(depth, checkColumn(search));
		}
		// EDIT Einträge anfprdern
		// EDIT EINTRÄGE anzeigen
	}

	private int checkColumn(Location location)
	{
		Location search = location.clone();
		int anz = -1;
		while (search.getBlock().getType() == Material.WALL_SIGN)
		{
			search.add(0, -1, 0);
			anz++;
		}
		return anz;
	}

	public void updateSignList(List<Location> locations)
	{
		for (Location location : locations)
			updateSign(location);
	}

	private class ScoreStringSorter implements Comparator<Score>
	{

		private final String sort;

		public ScoreStringSorter(String sort)
		{
			super();
			this.sort = sort;
		}

		@Override
		public int compare(Score score1, Score score2)
		{
			return score1.getString(sort).compareTo(score2.getString(sort));
		}
	}

	private class ScoreDoubleSorter implements Comparator<Score>
	{

		private final String sort;

		public ScoreDoubleSorter(String sort)
		{
			super();
			this.sort = sort;
		}

		@Override
		public int compare(Score score1, Score score2)
		{
			return score1.getValue(sort).compareTo(score2.getValue(sort));
		}
	}
}
