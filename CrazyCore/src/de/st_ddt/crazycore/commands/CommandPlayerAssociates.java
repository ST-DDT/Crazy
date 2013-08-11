package de.st_ddt.crazycore.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.events.CrazyPlayerAssociatesEvent;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.CrazyPipe;
import de.st_ddt.crazyutil.ListFormat;
import de.st_ddt.crazyutil.ListOptionsModder;
import de.st_ddt.crazyutil.Tabbed;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.IntegerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.OfflinePlayerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.Permission;

public class CommandPlayerAssociates extends CommandExecutor
{

	private final ListFormat listFormat = new ListFormat()
	{

		@Override
		@Localized("CRAZYCORE.COMMAND.PLAYER.ASSOCIATES.LISTFORMAT $Index$ $Entry$ $ChatHeader$")
		public String listFormat(final CommandSender target)
		{
			return CrazyLocale.getLocaleHead().getLocaleMessage(target, "CRAZYCORE.COMMAND.PLAYER.ASSOCIATES.LISTFORMAT");
		}

		@Override
		@Localized("CRAZYCORE.COMMAND.PLAYER.ASSOCIATES.HEADFORMAT $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$ $PlayerName$")
		public String headFormat(final CommandSender target)
		{
			return CrazyLocale.getLocaleHead().getLocaleMessage(target, "CRAZYCORE.COMMAND.PLAYER.ASSOCIATES.HEADFORMAT");
		}

		@Override
		public String entryFormat(final CommandSender target)
		{
			return "$0$";
		}
	};

	public CommandPlayerAssociates(final CrazyCore plugin)
	{
		super(plugin);
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		final AssoicatesListModer moder = new AssoicatesListModer(sender);
		final String[] pipeArgs = ChatHelperExtended.processListCommand(sender, args, plugin.getChatHeader(), listFormat, null, null, null, moder, new ArrayList<String>(), moder);
		if (pipeArgs != null)
			CrazyPipe.pipe(sender, moder.getList(), false, pipeArgs);
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		final Map<String, Tabbed> params = new TreeMap<String, Tabbed>();
		new AssoicatesListModer(sender).tabHelp(params);
		final Tabbed page = ChatHelperExtended.listTabHelp(params, sender, null, null);
		return ChatHelperExtended.tabHelp(args, params, page);
	}

	@Override
	@Permission("crazycore.player.associates")
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazycore.player.associates");
	}

	private class AssoicatesListModer implements ListOptionsModder<String>
	{

		private final OfflinePlayerParamitrisable player;
		private final IntegerParamitrisable recursion = new IntegerParamitrisable(0);
		private List<String> datas;

		protected AssoicatesListModer(final CommandSender sender)
		{
			super();
			if (sender instanceof Player)
				this.player = new OfflinePlayerParamitrisable((Player) sender);
			else
				this.player = new OfflinePlayerParamitrisable(null);
		}

		public List<String> getList()
		{
			return datas;
		}

		@Override
		public void modListPreOptions(final Map<String, Paramitrisable> params, final List<String> datas)
		{
			this.datas = datas;
			params.put("n", player);
			params.put("name", player);
			params.put("p", player);
			params.put("player", player);
			params.put("r", recursion);
			params.put("recursion", recursion);
		}

		@Override
		public String[] modListPostOptions(final List<String> datas, final String[] pipeArgs) throws CrazyException
		{
			if (player.getValue() == null)
				throw new CrazyCommandNoSuchException("Player", "(none)");
			final CrazyPlayerAssociatesEvent event = new CrazyPlayerAssociatesEvent(player.getValue().getName(), recursion.getValue());
			event.callEvent();
			datas.addAll(event.getAssociates());
			return pipeArgs;
		}

		public void tabHelp(final Map<String, Tabbed> params)
		{
			params.put("n", player);
			params.put("name", player);
			params.put("p", player);
			params.put("player", player);
			params.put("r", recursion);
			params.put("recursion", recursion);
		}

		@Override
		public String toString()
		{
			if (player.getValue() == null)
				return null;
			else
				return player.getValue().getName();
		}
	}
}
