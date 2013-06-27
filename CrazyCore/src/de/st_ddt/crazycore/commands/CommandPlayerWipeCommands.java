package de.st_ddt.crazycore.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.commands.CrazyCommandListEditor;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.ListFormat;
import de.st_ddt.crazyutil.source.Localized;

public class CommandPlayerWipeCommands extends CrazyCommandListEditor<CrazyCore, String>
{

	private final ListFormat format = new ListFormat()
	{

		@Override
		@Localized("CRAZYCORE.COMMAND.PLAYERWIPECOMMANDS.LISTHEAD $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$")
		public String headFormat(final CommandSender sender)
		{
			return plugin.getLocale().getLanguageEntry("COMMAND.PLAYERWIPECOMMANDS.LISTHEAD").getLanguageText(sender);
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

	public CommandPlayerWipeCommands(final CrazyCore plugin)
	{
		super(plugin);
	}

	@Override
	public List<String> getCollection()
	{
		return plugin.getWipePlayerCommands();
	}

	@Override
	@Localized("CRAZYCORE.COMMAND.PLAYERWIPECOMMANDS.ADD2 $Element$ $Index$")
	public String addViaIndexLocale()
	{
		return "COMMAND.PLAYERWIPECOMMANDS.ADD2";
	}

	@Override
	@Localized("CRAZYCORE.COMMAND.PLAYERWIPECOMMANDS.REMOVE2 $Element$ $Index$")
	public String removeViaIndexLocale()
	{
		return "COMMAND.PLAYERWIPECOMMANDS.REMOVE2";
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
	@Localized("CRAZYCORE.COMMAND.PLAYERWIPECOMMANDS.ADD $Element$")
	public String addLocale()
	{
		return "COMMAND.PLAYERWIPECOMMANDS.ADD";
	}

	@Override
	@Localized("CRAZYCORE.COMMAND.PLAYERWIPECOMMANDS.REMOVE $Element$")
	public String removeLocale()
	{
		return "COMMAND.PLAYERWIPECOMMANDS.REMOVE";
	}

	@Override
	protected void saveChanges()
	{
		plugin.saveConfiguration();
	}
}
