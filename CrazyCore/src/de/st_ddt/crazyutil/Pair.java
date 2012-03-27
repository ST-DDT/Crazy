package de.st_ddt.crazyutil;

public class Pair<S,T>
{
	
	private S data1;
	private T data2;
	
	public Pair(S data1, T data2)
	{
		super();
		this.data1 = data1;
		this.data2 = data2;
	}

	public S getData1()
	{
		return data1;
	}

	public void setData1(S data1)
	{
		this.data1 = data1;
	}

	public T getData2()
	{
		return data2;
	}

	public void setData2(T data2)
	{
		this.data2 = data2;
	}
	
	

}
