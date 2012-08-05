package de.st_ddt.crazyutil;

import java.util.Comparator;

import org.bukkit.plugin.java.JavaPlugin;

public class DepenciesComparator implements Comparator<JavaPlugin>
{

	@Override
	public int compare(final JavaPlugin o1, final JavaPlugin o2)
	{
		if (isDependent(o1, o2))
			return 1;
		if (isDependent(o2, o1))
			return -1;
		Integer o1s = null;
		if (o1.getDescription().getDepend() == null)
			o1s = 0;
		else
			o1s = o1.getDescription().getDepend().size();
		Integer o2s = null;
		if (o2.getDescription().getDepend() == null)
			o2s = 0;
		else
			o2s = o2.getDescription().getDepend().size();
		return o1s.compareTo(o2s);
	}

	public static boolean isDependent(final JavaPlugin o1, final JavaPlugin o2)
	{
		if (o1.getDescription().getDepend() == null)
			return false;
		return o1.getDescription().getDepend().contains(o2.getName());
	}
}
