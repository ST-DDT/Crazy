package de.st_ddt.crazychats.commands;

import org.bukkit.entity.Player;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.data.ChatPlayerData;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.source.Localized;

public class CrazyChatsPlayerCommandMutePlayer extends CrazyChatsPlayerCommandExecutor
{

	public CrazyChatsPlayerCommandMutePlayer(final CrazyChats plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYCHATS.COMMAND.MUTEDPLAYER $Name$")
	public void command(final Player player, final String[] args) throws CrazyException
	{
		if (args.length == 0)
			throw new CrazyCommandUsageException("<Player...>");
		final ChatPlayerData data = plugin.getPlayerData(player);
		for (final String arg : args)
		{
			data.mute(arg);
			plugin.sendLocaleMessage("COMMAND.MUTEDPLAYER", player, arg);
		}
		plugin.getCrazyDatabase().save(data);
	}
}
