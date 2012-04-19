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

public class FlatDatabase<S extends FlatDatabaseEntry> extends Database<S>
{

	protected final File file;
	protected HashMap<String, String[]> entries = new HashMap<String, String[]>();

	public FlatDatabase(Class<S> clazz, File file, String[] columnNames)
	{
		super(DatabaseTypes.FLAT, clazz, columnNames, getConstructor(clazz));
		this.file = file;
		checkTable();
		loadFile();
	}

	private static <S> Constructor<S> getConstructor(Class<S> clazz)
	{
		try
		{
			return clazz.getConstructor(String[].class);
		}
		catch (Exception e)
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
	public S getEntry(String key)
	{
		String[] data = entries.get(key);
		if (data == null)
			return null;
		try
		{
			return constructor.newInstance(new Object[] { data });
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<S> getEntries(String key)
	{
		List<S> list = new ArrayList<S>();
		list.add(getEntry(key));
		return list;
	}

	@Override
	public List<S> getAllEntries()
	{
		List<S> list = new ArrayList<S>();
		for (String key : entries.keySet())
			list.add(getEntry(key));
		return list;
	}

	@Override
	public void delete(String key)
	{
		entries.put(key, null);
	}

	@Override
	public void save(S entry)
	{
		entries.put(entry.getName(), entry.saveToFlatDatabase());
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
				String[] split = zeile.split("\\|");
				try
				{
					entries.put(split[0], split);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
					System.err.println("Invalid line " + zeile);
				}
			}
		}
		catch (FileNotFoundException e)
		{}
		catch (IOException e)
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
				catch (IOException e)
				{}
			if (inputStreamReader != null)
				try
				{
					inputStreamReader.close();
				}
				catch (IOException e)
				{}
			if (fileInputStream != null)
				try
				{
					fileInputStream.close();
				}
				catch (IOException e)
				{}
		}
	}

	private void saveFile()
	{
		FileWriter writer = null;
		try
		{
			writer = new FileWriter(file);
			writer.write(ChatHelper.listToString(columnNames, "|") + System.getProperty("line.separator"));
			for (String[] strings : entries.values())
				writer.write(ChatHelper.listToString(strings, "|") + System.getProperty("line.separator"));
			writer.flush();
		}
		catch (IOException e)
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
				catch (IOException e)
				{
					e.printStackTrace();
				}
		}
	}
}
