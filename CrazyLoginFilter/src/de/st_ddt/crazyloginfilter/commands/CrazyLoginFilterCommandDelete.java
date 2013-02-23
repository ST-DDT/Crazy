package de.st_ddt.crazyloginfilter.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyloginfilter.CrazyLoginFilter;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandExecutorException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.source.Localized;

public class CrazyLoginFilterCommandDelete extends CrazyLoginFilterCommandExecutor
{

	public CrazyLoginFilterCommandDelete(final CrazyLoginFilter plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYLOGINFILTER.COMMAND.DELETED $Name$")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!(sender instanceof Player))
			throw new CrazyCommandExecutorException(false);
		final Player player = (Player) sender;
		plugin.getCrazyDatabase().deleteEntry(player);
		plugin.sendLocaleMessage("COMMAND.DELETED", sender, player.getName());
	}
}
