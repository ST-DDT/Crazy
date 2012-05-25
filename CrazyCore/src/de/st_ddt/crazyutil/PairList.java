package de.st_ddt.crazyutil;

import java.util.ArrayList;
import java.util.Iterator;

public class PairList<S, T> extends ArrayList<Pair<S, T>>
{

	private static final long serialVersionUID = -2484546998957955050L;

	public void add(final S data1, final T data2)
	{
		super.add(new Pair<S, T>(data1, data2));
	}

	public Pair<S, T> findPairVia1(final S data)
	{
		for (final Pair<S, T> pair : this)
			if (pair.getData1().equals(data))
				return pair;
		return null;
	}

	public Pair<S, T> findPairVia2(final T data)
	{
		for (final Pair<S, T> pair : this)
			if (pair.getData2().equals(data))
				return pair;
		return null;
	}

	public T findDataVia1(final S data)
	{
		for (final Pair<S, T> pair : this)
			if (pair.getData1().equals(data))
				return pair.getData2();
		return null;
	}

	public S findDataVia2(final T data)
	{
		for (final Pair<S, T> pair : this)
			if (pair.getData2().equals(data))
				return pair.getData1();
		return null;
	}

	public Pair<S, T> setDataVia1(final S data1, final T data2)
	{
		final Pair<S, T> pair = findPairVia1(data1);
		if (pair != null)
		{
			pair.setData2(data2);
			return pair;
		}
		final Pair<S, T> neu = new Pair<S, T>(data1, data2);
		add(neu);
		return neu;
	}

	public Pair<S, T> setDataVia2(final S data1, final T data2)
	{
		final Pair<S, T> pair = findPairVia2(data2);
		if (pair != null)
		{
			pair.setData1(data1);
			return pair;
		}
		final Pair<S, T> neu = new Pair<S, T>(data1, data2);
		add(neu);
		return neu;
	}

	public void removeDataVia1(final S data1)
	{
		final Iterator<Pair<S, T>> it = this.iterator();
		while (it.hasNext())
			if (it.next().getData1().equals(data1))
				it.remove();
	}

	public void removeDataVia2(final T data2)
	{
		final Iterator<Pair<S, T>> it = this.iterator();
		while (it.hasNext())
			if (it.next().getData2().equals(data2))
				it.remove();
	}

	public ArrayList<S> getData1List()
	{
		final int length = size();
		final ArrayList<S> list = new ArrayList<S>();
		for (int i = 0; i < length; i++)
			list.add(get(i).getData1());
		return list;
	}

	public ArrayList<T> getData2List()
	{
		final int length = size();
		final ArrayList<T> list = new ArrayList<T>();
		for (int i = 0; i < length; i++)
			list.add(get(i).getData2());
		return list;
	}
}
