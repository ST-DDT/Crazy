package de.st_ddt.crazyutil;

import java.util.ArrayList;

public class PairList<S, T> extends ArrayList<Pair<S, T>>
{

	private static final long serialVersionUID = -2484546998957955050L;

	public void add(S data1, T data2)
	{
		super.add(new Pair<S, T>(data1, data2));
	}

	public Pair<S, T> findPairVia1(S data)
	{
		for (Pair<S, T> pair : this)
			if (pair.getData1().equals(data))
				return pair;
		return null;
	}

	public Pair<S, T> findPairVia2(T data)
	{
		for (Pair<S, T> pair : this)
			if (pair.getData2().equals(data))
				return pair;
		return null;
	}

	public T findDataVia1(S data)
	{
		for (Pair<S, T> pair : this)
			if (pair.getData1().equals(data))
				return pair.getData2();
		return null;
	}

	public S findDataVia2(T data)
	{
		for (Pair<S, T> pair : this)
			if (pair.getData2().equals(data))
				return pair.getData1();
		return null;
	}

	public Pair<S, T> setDataVia1(S data1, T data2)
	{
		Pair<S, T> pair = findPairVia1(data1);
		if (pair != null)
		{
			pair.setData2(data2);
			return pair;
		}
		Pair<S, T> neu = new Pair<S, T>(data1, data2);
		add(neu);
		return neu;
	}

	public Pair<S, T> setDataVia2(S data1, T data2)
	{
		Pair<S, T> pair = findPairVia2(data2);
		if (pair != null)
		{
			pair.setData1(data1);
			return pair;
		}
		Pair<S, T> neu = new Pair<S, T>(data1, data2);
		add(neu);
		return neu;
	}

	public void removeDataVia1(S data1)
	{
		for (int i = 0; i < size(); i++)
			if (get(i).getData1().equals(data1))
			{
				remove(i);
				return;
			}
	}

	public void removeDataVia2(T data2)
	{
		for (int i = 0; i < size(); i++)
			if (get(i).getData2().equals(data2))
			{
				remove(i);
				return;
			}
	}

	public ArrayList<S> getData1List()
	{
		int length = size();
		ArrayList<S> list = new ArrayList<S>();
		for (int i = 0; i < length; i++)
			list.add(get(i).getData1());
		return list;
	}

	public ArrayList<T> getData2List()
	{
		int length = size();
		ArrayList<T> list = new ArrayList<T>();
		for (int i = 0; i < length; i++)
			list.add(get(i).getData2());
		return list;
	}
}
