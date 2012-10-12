package de.st_ddt.crazycore.commands;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.CrazyPages;

public class CrazyCoreCommandPager extends CrazyCoreCommandExecutor
{

	public CrazyCoreCommandPager(CrazyCore plugin)
	{
		super(plugin);
	}

	@Override
	public void command(CommandSender sender, String[] args) throws CrazyException
	{
		if (args.length > 1)
			throw new CrazyCommandUsageException("[+/-]", "[Pagenumber]");
		if (args.length == 0)
		{
			CrazyPages.showPage(sender);
			return;
		}
		String arg = args[0];
		if (arg.equals("+"))
			CrazyPages.showNextPage(sender);
		else if (arg.equals("-"))
			CrazyPages.showPrevPage(sender);
		else
		{
			try
			{
				CrazyPages.showPage(sender, Integer.parseInt(arg));
			}
			catch (NumberFormatException e)
			{
				throw new CrazyCommandUsageException("[+/-]", "[Pagenumber (Integer)]");
			}
		}
	}
}
