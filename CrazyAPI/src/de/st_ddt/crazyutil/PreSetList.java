package de.st_ddt.crazyutil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public abstract class PreSetList implements Named
{

	public final static Map<String, PreSetList> PRESETLISTS = new TreeMap<String, PreSetList>();
	private final String name;

	public PreSetList(final String name)
	{
		super();
		this.name = name;
		PRESETLISTS.put(name, this);
	}

	@Override
	public String getName()
	{
		return name;
	}

	public abstract List<String> getList();

	static
	{
		new PreSetList("op")
		{

			@Override
			public List<String> getList()
			{
				final List<String> list = new ArrayList<String>();
				for (final OfflinePlayer player : Bukkit.getOperators())
					list.add(player.getName());
				return list;
			}
		};
		new PreSetList("onlines")
		{

			@Override
			public List<String> getList()
			{
				final List<String> list = new ArrayList<String>();
				for (final OfflinePlayer player : Bukkit.getOnlinePlayers())
					list.add(player.getName());
				return list;
			}
		};
		new PreSetList("offlines")
		{

			@Override
			public List<String> getList()
			{
				final List<String> list = new ArrayList<String>();
				for (final OfflinePlayer player : Bukkit.getOfflinePlayers())
					if (!player.isOnline())
						list.add(player.getName());
				return list;
			}
		};
		new PreSetList("players")
		{

			@Override
			public List<String> getList()
			{
				final List<String> list = new ArrayList<String>();
				for (final OfflinePlayer player : Bukkit.getOfflinePlayers())
					list.add(player.getName());
				return list;
			}
		};
		new PreSetList("banned")
		{

			@Override
			public List<String> getList()
			{
				final List<String> list = new ArrayList<String>();
				for (final OfflinePlayer player : Bukkit.getBannedPlayers())
					list.add(player.getName());
				return list;
			}
		};
		new PreSetList("banned-ips")
		{

			@Override
			public List<String> getList()
			{
				return new ArrayList<String>(Bukkit.getIPBans());
			}
		};
	}
}
