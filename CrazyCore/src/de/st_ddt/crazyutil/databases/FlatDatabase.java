package de.st_ddt.crazyutil.databases;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.st_ddt.crazyutil.ChatHelper;

public class FlatDatabase<S extends FlatDatabaseEntry> extends BasicDatabase<S>
{

	protected final File file;
	protected HashMap<String, String[]> entries = new HashMap<String, String[]>();

	public FlatDatabase(final Class<S> clazz, final File file, final String[] columnNames)
	{
		super(DatabaseType.FLAT, clazz, columnNames, getConstructor(clazz));
		this.file = file;
		checkTable();
		loadFile();
	}

	private static <S> Constructor<S> getConstructor(final Class<S> clazz)
	{
		try
		{
			return clazz.getConstructor(String[].class);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void checkTable()
	{
		file.getAbsoluteFile().getParentFile().mkdirs();
	}

	@Override
	public String getTableName()
	{
		return file.getPath();
	}

	@Override
	public S getEntry(final String key)
	{
		final String[] data = entries.get(key);
		if (data == null)
			return null;
		try
		{
			return constructor.newInstance(new Object[] { data });
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<S> getEntries(final String key)
	{
		final List<S> list = new ArrayList<S>();
		final S entry = getEntry(key);
		if (entry != null)
			list.add(entry);
		return list;
	}

	@Override
	public List<S> getAllEntries()
	{
		final List<S> list = new ArrayList<S>();
		for (final String key : entries.keySet())
		{
			final S entry = getEntry(key);
			if (entry != null)
				list.add(entry);
		}
		return list;
	}

	@Override
	public void delete(final String key)
	{
		entries.put(key, null);
	}

	@Override
	public void save(final S entry)
	{
		entries.put(entry.getName(), entry.saveToFlatDatabase());
		if (!bulkOperation)
			saveFile();
	}

	private void loadFile()
	{
		FileInputStream fileInputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufreader = null;
		try
		{
			fileInputStream = new FileInputStream(file);
			inputStreamReader = new InputStreamReader(fileInputStream);
			bufreader = new BufferedReader(inputStreamReader);
			// ErsteZeile Skippen
			String zeile = bufreader.readLine();
			while ((zeile = bufreader.readLine()) != null)
			{
				if (zeile.equals(""))
					continue;
				final String[] split = zeile.split("\\|");
				if (split == null)
					continue;
				if (split.length == 0)
					continue;
				try
				{
					entries.put(split[0], split);
				}
				catch (final ArrayIndexOutOfBoundsException e)
				{
					System.err.println("Invalid line " + zeile);
				}
			}
		}
		catch (final FileNotFoundException e)
		{}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (bufreader != null)
				try
				{
					bufreader.close();
				}
				catch (final IOException e)
				{}
			if (inputStreamReader != null)
				try
				{
					inputStreamReader.close();
				}
				catch (final IOException e)
				{}
			if (fileInputStream != null)
				try
				{
					fileInputStream.close();
				}
				catch (final IOException e)
				{}
		}
	}

	private void saveFile()
	{
		FileWriter writer = null;
		try
		{
			writer = new FileWriter(file);
			writer.write(ChatHelper.listingString("|", columnNames) + System.getProperty("line.separator"));
			for (final String[] strings : entries.values())
				if (strings != null)
					writer.write(ChatHelper.listingString("|", strings) + System.getProperty("line.separator"));
			writer.flush();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (writer != null)
				try
				{
					writer.close();
				}
				catch (final IOException e)
				{
					e.printStackTrace();
				}
		}
	}

	@Override
	protected void saveDatabase()
	{
		saveFile();
	}
}
