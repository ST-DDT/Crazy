package de.st_ddt.crazyutil;

import java.util.Comparator;

import de.st_ddt.crazyplugin.CrazyPluginInterface;

public class VersionComparator implements Comparator<String>
{

	@Override
	public int compare(final String version1, final String version2)
	{
		return compareVersions(version1, version2);
	}

	public static int compareVersions(final String version1, final String version2)
	{
		final String[] split1 = version1.split("\\.");
		final String[] split2 = version2.split("\\.");
		final int length1 = split1.length;
		final int length2 = split2.length;
		final int length = Math.max(length1, length2);
		final Integer[] v1 = new Integer[length];
		final Integer[] v2 = new Integer[length];
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

	public static int compareVersions(final CrazyPluginInterface plugin, final String version)
	{
		return compareVersions(plugin.getVersion(), version);
	}
}
