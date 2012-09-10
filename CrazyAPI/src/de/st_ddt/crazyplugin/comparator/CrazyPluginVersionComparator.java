package de.st_ddt.crazyplugin.comparator;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyutil.VersionComparator;

public class CrazyPluginVersionComparator implements CrazyPluginComparator
{

	@Override
	public int compare(CrazyPluginInterface o1, CrazyPluginInterface o2)
	{
		return VersionComparator.compareVersions(o1.getVersion(), o2.getVersion());
	}
}
