package de.st_ddt.crazyplugin.exceptions;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.source.Localized;

public class CrazyCommandUsageException extends CrazyCommandException
{

	private static final long serialVersionUID = 1755697197006991043L;
	private final String[] validUsage;
	private String commandHierarchy = "";

	public CrazyCommandUsageException(final String... validUsage)
	{
		super();
		this.validUsage = validUsage;
	}

	@Override
	public String getLangPath()
	{
		return super.getLangPath() + ".USAGE";
	}

	@Override
	@Localized("CRAZYEXCEPTION.COMMAND.USAGE $Command$ $CommandHierarchy$ $ValidUsages$")
	public void print(final CommandSender sender, final String header)
	{
		ChatHelper.sendMessage(sender, header, locale, command, "/" + commandHierarchy, ChatHelper.listingString("\\n...", validUsage));
	}

	@Override
	public void addCommandPrefix(final String... prefixes)
	{
		commandHierarchy = ChatHelper.listingString(" ", prefixes) + " " + commandHierarchy;
	}
}
