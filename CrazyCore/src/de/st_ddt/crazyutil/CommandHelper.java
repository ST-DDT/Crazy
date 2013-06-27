package de.st_ddt.crazyutil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;

public class CommandHelper
{

	@SuppressWarnings("unchecked")
	private static Map<String, Command> getCommandMap() throws ClassCastException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		final SimplePluginManager spm = (SimplePluginManager) Bukkit.getPluginManager();
		if (spm == null)
			return null;
		else
		{
			final Field commandMapField = spm.getClass().getDeclaredField("commandMap");
			commandMapField.setAccessible(true);
			final SimpleCommandMap commandMap = (SimpleCommandMap) commandMapField.get(spm);
			final Field knownCommandsField = commandMap.getClass().getDeclaredField("knownCommands");
			knownCommandsField.setAccessible(true);
			return (Map<String, Command>) knownCommandsField.get(commandMap);
		}
	}

	public static Map<String, Command> getSaveCommandMap()
	{
		try
		{
			final Map<String, Command> commands = getCommandMap();
			if (commands == null)
				return new HashMap<String, Command>(0);
			else
				return commands;
		}
		catch (final Exception e)
		{
			return new HashMap<String, Command>(0);
		}
	}

	public static Set<String> getCommandNames()
	{
		final Set<String> res = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		res.addAll(getSaveCommandMap().keySet());
		return res;
	}
}
