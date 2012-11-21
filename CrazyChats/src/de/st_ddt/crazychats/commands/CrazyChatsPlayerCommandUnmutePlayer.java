package de.st_ddt.crazychats.commands;

import org.bukkit.entity.Player;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.data.ChatPlayerData;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazyChatsPlayerCommandUnmutePlayer extends CrazyChatsPlayerCommandExecutor
{

	public CrazyChatsPlayerCommandUnmutePlayer(final CrazyChats plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYCHATS.COMMAND.UNMUTEDPLAYER $Name$")
	public void command(final Player player, final String[] args) throws CrazyException
	{
		if (args.length == 0)
			throw new CrazyCommandUsageException("<Player...>");
		final ChatPlayerData data = plugin.getPlayerData(player);
		if (args.length == 1)
			if (args[0].equals("*"))
			{
				data.getMutedPlayers().clear();
				plugin.sendLocaleMessage("COMMAND.UNMUTEDPLAYER", player, "*ALL*");
			}
		for (final String arg : args)
		{
			data.unmute(arg);
			plugin.sendLocaleMessage("COMMAND.UNMUTEDPLAYER", player, arg);
		}
		plugin.getCrazyDatabase().save(data);
	}
}
