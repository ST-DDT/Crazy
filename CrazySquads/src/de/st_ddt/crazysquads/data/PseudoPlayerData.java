package de.st_ddt.crazysquads.data;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.data.PlayerData;
import de.st_ddt.crazysquads.CrazySquads;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.locales.Localized;

public class PseudoPlayerData extends PlayerData<PseudoPlayerData>
{

	private final Squad squad;

	public PseudoPlayerData(final String name, final Squad squad)
	{
		super(name);
		this.squad = squad;
	}

	@Override
	@Localized({ "CRAZYSQUADS.PLAYERINFO.SQUAD.NAME $Name$", "CRAZYSQUADS.PLAYERINFO.SQUAD.MEMBERS $Members$" })
	public void showDetailed(final CommandSender target, final String chatHeader)
	{
		final CrazyLocale locale = CrazyLocale.getLocaleHead().getSecureLanguageEntry("CRAZYSQUADS.PLAYERINFO");
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("SQUAD.NAME"), squad.getName());
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("SQUAD.MEMBERS"), ChatHelper.listingString(squad.getMemberNames()));
	}

	@Override
	public String getParameter(final CommandSender sender, final int index)
	{
		switch (index)
		{
			case 0:
				return name;
			case 1:
				return squad.getName();
			case 2:
				return ChatHelper.listingString(squad.getMemberNames());
			default:
				return "";
		}
	}

	@Override
	public int getParameterCount()
	{
		return 3;
	}

	@Override
	protected String getChatHeader()
	{
		return getPlugin().getChatHeader();
	}

	protected CrazySquads getPlugin()
	{
		return CrazySquads.getPlugin();
	}
}
