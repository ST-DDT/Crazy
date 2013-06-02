package de.st_ddt.crazyplugin.exceptions;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.source.Localized;

public class CrazyCommandPermissionProtectedPlayerException extends CrazyCommandPermissionException
{

	private static final long serialVersionUID = 3689965652557137181L;
	protected final String player;

	public CrazyCommandPermissionProtectedPlayerException(final OfflinePlayer player)
	{
		super();
		this.player = player.getName();
	}

	public CrazyCommandPermissionProtectedPlayerException(final String player)
	{
		super();
		this.player = player;
	}

	@Override
	public String getLangPath()
	{
		return super.getLangPath() + ".PROTECTEDPLAYER";
	}

	@Override
	@Localized("CRAZYEXCEPTION.COMMAND.PERMISSION.PROTECTEDPLAYER $Command$ $Player$")
	public void print(final CommandSender sender, final String header)
	{
		ChatHelper.sendMessage(sender, header, locale, command, player);
	}
}
