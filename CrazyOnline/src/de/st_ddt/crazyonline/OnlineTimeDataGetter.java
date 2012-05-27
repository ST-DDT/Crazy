package de.st_ddt.crazyonline;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyutil.EntryDataGetter;

public class OnlineTimeDataGetter implements EntryDataGetter<OnlinePlayerData>
{

	protected final CommandSender target;

	public OnlineTimeDataGetter(CommandSender target)
	{
		super();
		this.target = target;
	}

	@Override
	public String getEntryData(OnlinePlayerData entry)
	{
		return entry.getName() + " - " + CrazyOnline.getPlugin().timeOutputConverter(entry.getTimeTotal(), target);
	}
}
