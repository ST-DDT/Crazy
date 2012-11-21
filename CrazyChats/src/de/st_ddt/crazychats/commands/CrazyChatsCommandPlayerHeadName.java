package de.st_ddt.crazychats.commands;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kitteh.tag.TagAPI;
import org.kitteh.tag.TagAPIException;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.data.ChatPlayerData;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandErrorException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class CrazyChatsCommandPlayerHeadName extends CrazyChatsCommandExecutor
{

	public CrazyChatsCommandPlayerHeadName(final CrazyChats plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYCHATS.COMMAND.PLAYER.HEADNAME.WARNLENGTH $ListName$ $Length$", "CRAZYCHATS.COMMAND.PLAYER.HEADNAME.DONE $Player$ $ListName$", "CRAZYCHATS.COMMAND.PLAYER.HEADNAME.REMOVED $Player$" })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length < 1 || args.length > 2)
			throw new CrazyCommandUsageException("<Player> [HeadName]");
		final String name = args[0];
		final ChatPlayerData data = plugin.getPlayerData(name);
		if (data == null)
			throw new CrazyCommandNoSuchException("Player", name);
		if (!PermissionModule.hasPermission(sender, "crazychats.player.headname." + (data.getPlayer().equals(sender) ? "self" : "other")))
			throw new CrazyCommandPermissionException();
		if (args.length == 2)
		{
			final String headName = ChatHelper.colorise(args[1]);
			if (headName.length() < 3 || headName.length() > 16)
				plugin.sendLocaleMessage("COMMAND.PLAYER.HEADNAME.WARNLENGTH", sender, headName, headName.length());
			data.setHeadName(headName);
			plugin.sendLocaleMessage("COMMAND.PLAYER.HEADNAME.DONE", sender, data.getName(), headName);
		}
		else
		{
			data.setHeadName(null);
			plugin.sendLocaleMessage("COMMAND.PLAYER.HEADNAME.REMOVED", sender, data.getName());
		}
		final Player player = data.getPlayer();
		if (player.isOnline())
			try
			{
				TagAPI.refreshPlayer(player);
			}
			catch (final TagAPIException e)
			{
				throw new CrazyCommandErrorException(e);
			}
		plugin.getCrazyDatabase().save(data);
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		if (args.length != -1)
			return null;
		final String arg = args[0].toLowerCase();
		final List<String> res = new LinkedList<String>();
		for (final Player player : Bukkit.getOnlinePlayers())
			if (player.getName().toLowerCase().startsWith(arg))
				res.add(arg);
		return res;
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazychats.player.headname.self") || PermissionModule.hasPermission(sender, "crazychats.player.headname.other");
	}
}
