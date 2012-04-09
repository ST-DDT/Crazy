package de.st_ddt.crazyarena.arenas;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;

public class ArenaList extends ArrayList<Arena>
{

	private static final long serialVersionUID = -2907903782945515247L;
	private final String arenaDir;

	public ArenaList(FileConfiguration config)
	{
		arenaDir = CrazyArena.getPlugin().getDataFolder().getPath();
		new File(arenaDir).mkdir();
		for (String name : config.getStringList("arenas"))
			loadArena(name);
	}

	public Arena loadArena(String name)
	{
		Arena arena = getArena(name);
		if (arena != null)
			return arena;
		YamlConfiguration config = new YamlConfiguration();
		try
		{
			config.load(arenaDir + "/" + name + ".yml");
		}
		catch (FileNotFoundException e)
		{
			CrazyArena.getPlugin().consoleLog("Arenaconfig not found for Arena " + name);
			return null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (InvalidConfigurationException e)
		{
			CrazyArena.getPlugin().consoleLog("Invalid Arenaconfig for Arena " + name);
			e.printStackTrace();
		}
		String type = config.getString("type", "-1");
		if (type == "-1")
		{
			CrazyArena.getPlugin().consoleLog("Invalid Arenaconfig (Missing type) for Arena " + name);
			return null;
		}
		type = CrazyArena.getArenaTypes().findDataVia1(type).getName();
		arena = ObjectSaveLoadHelper.load(type, Arena.class, new Class<?>[] { ConfigurationSection.class }, new Object[] { config }, "de.st_ddt.crazyarena.arenas");
		this.add(arena);
		return arena;
	}

	public Arena getArena(Player player)
	{
		for (Arena arena : this)
			if (arena.isParticipant(player))
				return arena;
		return null;
	}

	public Arena getArena(String name)
	{
		for (Arena arena : this)
			if (arena.getName().equalsIgnoreCase(name))
				return arena;
		return null;
	}
}
