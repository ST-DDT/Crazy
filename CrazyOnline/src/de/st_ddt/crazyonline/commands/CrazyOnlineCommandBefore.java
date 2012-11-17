package de.st_ddt.crazyonline.commands;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyonline.CrazyOnline;
import de.st_ddt.crazyonline.data.OnlineData;
import de.st_ddt.crazyonline.data.comparator.OnlineDataLastLoginComperator;
import de.st_ddt.crazyplugin.CrazyLightPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.FilterInterface;
import de.st_ddt.crazyutil.FilterInterface.FilterInstanceInterface;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class CrazyOnlineCommandBefore extends CrazyOnlineCommandExecutor
{

	public CrazyOnlineCommandBefore(final CrazyOnline plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYONLINE.COMMAND.BEFORE.HEADER $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$ $BeforeDateTime$", "CRAZYONLINE.COMMAND.BEFORE.LISTFORMAT $Index$ $Entry$ $ChatHeader$", "CRAZYONLINE.COMMAND.BEFORE.ENTRYFORMAT $Name$ $FirstLogin$ $LastLogin$ $LastLogout$ $TimeTotalValue$ $IP$ $TimeTotalText$" })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		Date date = null;
		try
		{
			date = CrazyLightPluginInterface.DATEFORMAT.parse(CrazyLightPluginInterface.DATEFORMAT.format(new Date()));
		}
		catch (final ParseException e1)
		{}
		final Date today = date;
		if (sender instanceof Player)
			if (plugin.hasPlayerData((Player) sender))
				date = plugin.getPlayerData((Player) sender).getLastLogin();
		final Date defaultDate = date;
		final FilterInstanceInterface<OnlineData> filter = new FilterInstanceInterface<OnlineData>()
		{

			private Date date = defaultDate;

			@Override
			public String getName()
			{
				return "";
			}

			@Override
			public String[] getAliases()
			{
				return new String[0];
			}

			@Override
			public void filter(final Collection<? extends OnlineData> datas)
			{
				final Iterator<? extends OnlineData> it = datas.iterator();
				while (it.hasNext())
					if (!filter(it.next()))
						it.remove();
			}

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				try
				{
					if (parameter.equalsIgnoreCase("TODAY"))
						date = today;
					else if (parameter.contains(" "))
						date = CrazyLightPluginInterface.DATETIMEFORMAT.parse(parameter);
					else
						date = CrazyLightPluginInterface.DATEFORMAT.parse(parameter);
				}
				catch (final ParseException e)
				{
					throw new CrazyCommandParameterException(0, "Date <JJJJ.MM.DD> [hh:mm:ss]/TODAY");
				}
			}

			@Override
			public boolean filter(final OnlineData data)
			{
				return date.after(data.getLastLogin());
			}

			@Override
			public String toString()
			{
				return CrazyLightPluginInterface.DATETIMEFORMAT.format(date);
			}
		};
		final ArrayList<FilterInstanceInterface<OnlineData>> filterInstances = new ArrayList<FilterInterface.FilterInstanceInterface<OnlineData>>();
		filterInstances.add(filter);
		final List<OnlineData> list;
		synchronized (plugin.getPlayerDataLock())
		{
			list = new ArrayList<OnlineData>(plugin.getPlayerData());
		}
		ChatHelperExtended.processFullListCommand(sender, args, plugin.getChatHeader(), plugin.getLocale().getLanguageEntry("COMMAND.BEFORE.HEADER").getLanguageText(sender), plugin.getLocale().getLanguageEntry("COMMAND.BEFORE.LISTFORMAT").getLanguageText(sender), plugin.getLocale().getLanguageEntry("COMMAND.BEFORE.ENTRYFORMAT").getLanguageText(sender), filterInstances, plugin.getPlayerDataComparators(), new OnlineDataLastLoginComperator(), null, list, filter);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazyonline.before");
	}
}
