package de.st_ddt.crazycaptcha.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazycaptcha.CrazyCaptcha;
import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.commands.CrazyCommandExecutor;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class CrazyCommandVerifiedCheck extends CrazyCaptchaCommandExecutor
{

	private final CrazyCommandExecutor<? extends CrazyPluginInterface> command;

	public CrazyCommandVerifiedCheck(final CrazyCaptcha plugin, final CrazyCommandExecutor<? extends CrazyPluginInterface> command)
	{
		super(plugin);
		this.command = command;
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (sender instanceof Player)
			if (!plugin.isVerified((Player) sender))
				throw new CrazyCommandPermissionException();
		command.command(sender, args);
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		return command.tab(sender, args);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		if (sender instanceof Player)
			if (!plugin.isVerified((Player) sender))
				return false;
		return command.hasAccessPermission(sender);
	}
}
