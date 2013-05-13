package de.st_ddt.crazycore.data;

import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.data.PlayerData;
import de.st_ddt.crazyplugin.events.CrazyPlayerAssociatesEvent;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.source.Localized;

public class PseudoPlayerData extends PlayerData<PseudoPlayerData>
{

	public PseudoPlayerData(final String name)
	{
		super(name);
	}

	@Override
	public String getParameter(final CommandSender sender, final int index)
	{
		switch (index)
		{
			case 0:
				return name;
			default:
				return "";
		}
	}

	@Override
	public int getParameterCount()
	{
		return 1;
	}

	public CrazyCore getPlugin()
	{
		return CrazyCore.getPlugin();
	}

	@Override
	protected String getChatHeader()
	{
		return getPlugin().getChatHeader();
	}

	@Override
	public void show(final CommandSender target)
	{
		show(target, getChatHeader(), false);
	}

	@Override
	@Localized({ "CRAZYCORE.PLAYERINFO.LANGUAGE $Language$", "CRAZYCORE.PLAYERINFO.ASSOCIATES $Associates$", "CRAZYCORE.PLAYERINFO.GROUPS $Groups$", "CRAZYCORE.PLAYERINFO.PROTECTEDPLAYER $Protected$" })
	public void showDetailed(final CommandSender target, final String chatHeader)
	{
		final CrazyLocale locale = getPlugin().getLocale().getSecureLanguageEntry("PLAYERINFO");
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("LANGUAGE"), CrazyLocale.getUserLanguageName(name, true));
		final CrazyPlayerAssociatesEvent associatesEvent = new CrazyPlayerAssociatesEvent(name);
		associatesEvent.callEvent();
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("ASSOCIATES"), ChatHelper.listingString(associatesEvent.getAssociates()));
		final Player player = getPlayer();
		if (player != null)
		{
			final Set<String> groups = PermissionModule.getGroups(player);
			if (groups == null)
			{
				final String group = PermissionModule.getGroup(player);
				if (group != null)
					ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("GROUPS"), group);
			}
			else
				ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("GROUPS"), ChatHelper.listingString(groups));
		}
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("PROTECTEDPLAYER"), getPlugin().isProtectedPlayer(name) ? "True" : "False");
	}
}
