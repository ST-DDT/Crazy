package de.st_ddt.crazyloginfilter.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyloginfilter.CrazyLoginFilter;
import de.st_ddt.crazyloginfilter.data.PlayerAccessFilter;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandExecutorException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class CrazyLoginFilterCommandShow extends CrazyLoginFilterCommandExecutor
{

	public CrazyLoginFilterCommandShow(final CrazyLoginFilter plugin)
	{
		super(plugin);
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (sender instanceof ConsoleCommandSender)
			throw new CrazyCommandExecutorException(false);
		show(sender, args, plugin.getPlayerData((Player) sender));
	}

	public void show(final CommandSender sender, final String[] args, final PlayerAccessFilter data) throws CrazyException
	{
		if (data == null)
			throw new CrazyCommandCircumstanceException("when AccessFilter is created!");
		data.show(sender, plugin.getChatHeader(), true);
	}
}
