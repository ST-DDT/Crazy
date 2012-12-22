package de.st_ddt.crazycaptcha.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazycaptcha.CrazyCaptcha;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class CrazyCaptchaCommandMainGenerator extends CrazyCaptchaCommandExecutor
{

	public CrazyCaptchaCommandMainGenerator(final CrazyCaptcha plugin)
	{
		super(plugin);
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (sender instanceof Player)
			if (!plugin.isVerified((Player) sender))
				throw new CrazyCommandPermissionException();
		plugin.getGenerator().getCommands().command(sender, args);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		if (sender instanceof Player)
			if (!plugin.isVerified((Player) sender))
				return false;
		return plugin.getGenerator().getCommands().hasAccessPermission(sender);
	}
}
