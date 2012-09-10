package de.st_ddt.crazyutil;

import java.util.Comparator;

public class VersionComparator implements Comparator<String>
{

	@Override
	public int compare(String version1, String version2)
	{
		return compareVersions(version1, version2);
	}

	public static int compareVersions(String version1, String version2)
	{
		String[] split1 = version1.split("\\.");
		String[] split2 = version2.split("\\.");
		int length1 = split1.length;
		int length2 = split2.length;
		int length = Math.max(length1, length2);
		Integer[] v1 = new Integer[length];
		Integer[] v2 = new Integer[length];
		for (int i = 0; i < length1; i++)
			v1[i] = Integer.parseInt(split1[i]);
		for (int i = length1; i < length; i++)
			v1[i] = 0;
		for (int i = 0; i < length2; i++)
			v2[i] = Integer.parseInt(split2[i]);
		for (int i = length2; i < length; i++)
			v2[i] = 0;
		int i = 0;
		int res = 0;
		while (res == 0 && i < length)
		{
			res = v1[i].compareTo(v2[i]);
			i++;
		}
		return res;
	}
}
