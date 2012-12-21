package de.st_ddt.crazycaptcha.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazycaptcha.CrazyCaptcha;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class CrazyCaptchaCommandReverify extends CrazyCaptchaCommandExecutor
{

	public CrazyCaptchaCommandReverify(final CrazyCaptcha plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYCAPTCHA.COMMAND.REVERIFY $Name$")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length == 0)
			throw new CrazyCommandUsageException("<Player...>");
		final String name = ChatHelper.listingString(" ", args);
		if (name.equals("*"))
			for (final Player player : Bukkit.getOnlinePlayers())
				plugin.playerReverify(player);
		else
		{
			final Player player = Bukkit.getPlayer(name);
			if (player == null)
				throw new CrazyCommandNoSuchException("Player", name);
			plugin.playerReverify(player);
		}
		plugin.sendLocaleMessage("COMMAND.REVERIFY", sender, name);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazycaptcha.recaptcha");
	}
}
