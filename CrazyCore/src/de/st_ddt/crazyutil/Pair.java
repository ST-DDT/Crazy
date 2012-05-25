package de.st_ddt.crazyutil;

public class Pair<S, T>
{

	protected S data1;
	protected T data2;

	public Pair(final S data1, final T data2)
	{
		super();
		this.data1 = data1;
		this.data2 = data2;
	}

	public S getData1()
	{
		return data1;
	}

	public void setData1(final S data1)
	{
		this.data1 = data1;
	}

	public T getData2()
	{
		return data2;
	}

	public void setData2(final T data2)
	{
		this.data2 = data2;
	}
}
