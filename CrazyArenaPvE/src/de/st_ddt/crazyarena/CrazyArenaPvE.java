package de.st_ddt.crazyarena;


public class CrazyArenaPvE extends CrazyArenaPlugin
{

	private static CrazyArenaPvE plugin;

	@Override
	public void onLoad()
	{
		super.onLoad();
		plugin = this;
	}

	public static CrazyArenaPvE getPlugin()
	{
		return plugin;
	}

	@Override
	protected void registerArenaTypes()
	{
	}

	@Override
	public String[] getArenaTypes()
	{
		return new String[] { "PvE", "Monster" };
	}
}
