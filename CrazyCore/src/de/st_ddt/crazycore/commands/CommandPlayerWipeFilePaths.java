package de.st_ddt.crazycore.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.commands.CrazyCommandListEditor;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.ListFormat;
import de.st_ddt.crazyutil.source.Localized;

public class CommandPlayerWipeFilePaths extends CrazyCommandListEditor<CrazyCore, String>
{

	private final ListFormat format = new ListFormat()
	{

		@Override
		@Localized("CRAZYCORE.COMMAND.PLAYERWIPEFILEPATHS.LISTHEAD $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$")
		public String headFormat(final CommandSender sender)
		{
			return plugin.getLocale().getLanguageEntry("COMMAND.PLAYERWIPEFILEPATHS.LISTHEAD").getLanguageText(sender);
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

	public CommandPlayerWipeFilePaths(final CrazyCore plugin)
	{
		super(plugin);
	}

	@Override
	public List<String> getCollection()
	{
		return plugin.getWipePlayerCommands();
	}

	@Override
	@Localized("CRAZYCORE.COMMAND.PLAYERWIPEFILEPATHS.ADD2 $Element$ $Index$")
	public String addViaIndexLocale()
	{
		return "COMMAND.PLAYERWIPEFILEPATHS.ADD2";
	}

	@Override
	@Localized("CRAZYCORE.COMMAND.PLAYERWIPEFILEPATHS.REMOVE2 $Element$ $Index$")
	public String removeViaIndexLocale()
	{
		return "COMMAND.PLAYERWIPEFILEPATHS.REMOVE2";
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

	@Override
	@Localized("CRAZYCORE.COMMAND.PLAYERWIPEFILEPATHS.ADD $Element$")
	public String addLocale()
	{
		return "COMMAND.PLAYERWIPEFILEPATHS.ADD";
	}

	@Override
	@Localized("CRAZYCORE.COMMAND.PLAYERWIPEFILEPATHS.REMOVE $Element$")
	public String removeLocale()
	{
		return "COMMAND.PLAYERWIPEFILEPATHS.REMOVE";
	}

	@Override
	protected void saveChanges()
	{
		plugin.saveConfiguration();
	}
}
