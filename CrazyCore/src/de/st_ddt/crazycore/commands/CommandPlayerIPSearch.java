package de.st_ddt.crazycore.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.events.CrazyPlayerNamesConnectedToIPEvent;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.CrazyPipe;
import de.st_ddt.crazyutil.ListFormat;
import de.st_ddt.crazyutil.ListOptionsModder;
import de.st_ddt.crazyutil.Tabbed;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.StringParamitrisable;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.Permission;

public class CommandPlayerIPSearch extends CommandExecutor
{

	private final ListFormat listFormat = new ListFormat()
	{

		@Override
		@Localized("CRAZYCORE.COMMAND.PLAYER.IPSEARCH.LISTFORMAT $Index$ $Entry$ $ChatHeader$")
		public String listFormat(final CommandSender target)
		{
			return CrazyLocale.getLocaleHead().getLocaleMessage(target, "CRAZYCORE.COMMAND.PLAYER.IPSEARCH.LISTFORMAT");
		}

		@Override
		@Localized("CRAZYCORE.COMMAND.PLAYER.IPSEARCH.HEADFORMAT $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$ $IP$")
		public String headFormat(final CommandSender target)
		{
			return CrazyLocale.getLocaleHead().getLocaleMessage(target, "CRAZYCORE.COMMAND.PLAYER.IPSEARCH.HEADFORMAT");
		}

		@Override
		public String entryFormat(final CommandSender target)
		{
			return "$0$";
		}
	};

	public CommandPlayerIPSearch(final CrazyCore plugin)
	{
		super(plugin);
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		final IPListModer moder = new IPListModer();
		final String[] pipeArgs = ChatHelperExtended.processListCommand(sender, args, plugin.getChatHeader(), listFormat, null, null, null, moder, new ArrayList<String>(), moder);
		if (pipeArgs != null)
			CrazyPipe.pipe(sender, moder.getList(), false, pipeArgs);
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		final Map<String, Tabbed> params = new TreeMap<String, Tabbed>();
		new IPListModer().tabHelp(params);
		final Tabbed page = ChatHelperExtended.listTabHelp(params, sender, null, null);
		return ChatHelperExtended.tabHelp(args, params, page);
	}

	@Override
	@Permission("crazycore.player.ipsearch")
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazycore.player.ipsearch");
	}

	private class IPListModer implements ListOptionsModder<String>
	{

		private final StringParamitrisable ip = new StringParamitrisable(null);
		private List<String> datas;

		public List<String> getList()
		{
			return datas;
		}

		@Override
		public void modListPreOptions(final Map<String, Paramitrisable> params, final List<String> datas)
		{
			this.datas = datas;
			params.put("ip", ip);
		}

		@Override
		public String[] modListPostOptions(final List<String> datas, final String[] pipeArgs) throws CrazyException
		{
			if (ip.getValue() == null)
				throw new CrazyCommandNoSuchException("IP", "(none)");
			final CrazyPlayerNamesConnectedToIPEvent event = new CrazyPlayerNamesConnectedToIPEvent(ip.getValue());
			event.callEvent();
			datas.addAll(event.getNames());
			return pipeArgs;
		}

		public void tabHelp(final Map<String, Tabbed> params)
		{
			params.put("ip", ip);
		}

		@Override
		public String toString()
		{
			return ip.getValue();
		}
	}
}
