package de.st_ddt.crazysquads.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.commands.CrazyCommandListEditor;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazysquads.CrazySquads;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.ListFormat;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class CrazySquadsCommandMainCommands extends CrazyCommandListEditor<CrazySquads, String>
{

	private final ListFormat format = new ListFormat()
	{

		@Override
		@Localized("CRAZYSQUADS.COMMAND.SQUADCOMMANDS.LISTHEAD $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$")
		public String headFormat(final CommandSender sender)
		{
			return plugin.getLocale().getLanguageEntry("COMMAND.SQUADCOMMANDS.LISTHEAD").getLanguageText(sender);
		}

		@Override
		public String listFormat(final CommandSender sender)
		{
			return "$1$\n";
		}

		@Override
		public String entryFormat(final CommandSender sender)
		{
			return "$0$";
		}
	};

	public CrazySquadsCommandMainCommands(final CrazySquads plugin)
	{
		super(plugin, true, false, true);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazysquads.commands");
	}

	@Override
	public List<String> getCollection()
	{
		return plugin.getCommandWhiteList();
	}

	@Override
	@Localized("CRAZYSQUADS.COMMAND.SQUADCOMMANDS.ADDED $Element$")
	public String addLocale()
	{
		return "COMMAND.SQUADCOMMANDS.ADDED";
	}

	@Override
	public String addViaIndexLocale()
	{
		return null;
	}

	@Override
	@Localized("CRAZYSQUADS.COMMAND.SQUADCOMMANDS.REMOVED $Element$")
	public String removeLocale()
	{
		return "COMMAND.SQUADCOMMANDS.REMOVED";
	}

	@Override
	@Localized("CRAZYSQUADS.COMMAND.SQUADCOMMANDS.REMOVED $Element$")
	public String removeViaIndexLocale()
	{
		return "COMMAND.SQUADCOMMANDS.REMOVED";
	}

	@Override
	public ListFormat listFormat()
	{
		return format;
	}

	@Override
	public String getEntry(final CommandSender sender, final String... args) throws CrazyException
	{
		return ChatHelper.listingString(" ", args);
	}
}
