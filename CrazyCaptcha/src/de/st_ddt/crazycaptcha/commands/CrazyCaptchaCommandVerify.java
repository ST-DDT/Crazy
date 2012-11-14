package de.st_ddt.crazycaptcha.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazycaptcha.CrazyCaptcha;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandExecutorException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;

public class CrazyCaptchaCommandVerify extends CrazyCaptchaCommandExecutor
{

	public CrazyCaptchaCommandVerify(final CrazyCaptcha plugin)
	{
		super(plugin);
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!(sender instanceof Player))
			throw new CrazyCommandExecutorException(false);
		final Player player = (Player) sender;
		if (args.length == 0)
			throw new CrazyCommandUsageException("<Captcha...>");
		final String captcha = ChatHelper.listingString(" ", args);
		plugin.playerVerify(player, captcha);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return true;
	}
}
