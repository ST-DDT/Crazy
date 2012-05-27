package de.st_ddt.crazycore;

import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazyutil.EntryDataGetter;

public class PluginDataGetter implements EntryDataGetter<JavaPlugin>
{

	@Override
	public String getEntryData(JavaPlugin entry)
	{
		return entry.getDescription().getName() + " - " + entry.getDescription().getVersion();
	}
}
