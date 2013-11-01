package de.st_ddt.crazyutil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public abstract class PreSetList implements Named
{

	public final static Map<String, PreSetList> PRESETLISTS = new TreeMap<String, PreSetList>(String.CASE_INSENSITIVE_ORDER);
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
		new PreSetList("online_ops")
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
		new PreSetList("offline_ops")
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
		new PreSetList("all_ops")
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
		new PreSetList("not_ops")
		{

			@Override
			public List<String> getList()
			{
				final List<String> list = new ArrayList<String>();
				for (final OfflinePlayer player : Bukkit.getOnlinePlayers())
					if (!player.isOp())
						list.add(player.getName());
				return list;
			}
		};
		new PreSetList("online_players")
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
		new PreSetList("offline_players")
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
		new PreSetList("all_players")
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
		new PreSetList("online_whitelisted")
		{

			@Override
			public List<String> getList()
			{
				final List<String> list = new ArrayList<String>();
				for (final OfflinePlayer player : Bukkit.getWhitelistedPlayers())
					if (player.isOnline())
						list.add(player.getName());
				return list;
			}
		};
		new PreSetList("offline_whitelisted")
		{

			@Override
			public List<String> getList()
			{
				final List<String> list = new ArrayList<String>();
				for (final OfflinePlayer player : Bukkit.getWhitelistedPlayers())
					if (!player.isOnline())
						list.add(player.getName());
				return list;
			}
		};
		new PreSetList("all_whitelisted")
		{

			@Override
			public List<String> getList()
			{
				final List<String> list = new ArrayList<String>();
				for (final OfflinePlayer player : Bukkit.getWhitelistedPlayers())
					list.add(player.getName());
				return list;
			}
		};
		new PreSetList("not_whitelisted")
		{

			@Override
			public List<String> getList()
			{
				final List<String> list = new ArrayList<String>();
				for (final OfflinePlayer player : Bukkit.getOnlinePlayers())
					if (!player.isWhitelisted())
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
