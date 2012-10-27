package de.st_ddt.crazyutil;

import java.util.List;
import java.util.Map;

import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;

public interface ListOptionsModder<T>
{

	public void modListPreOptions(Map<String, Paramitrisable> params, List<T> datas);

	public String[] modListPostOptions(List<T> datas, String[] pipeArgs);
}
