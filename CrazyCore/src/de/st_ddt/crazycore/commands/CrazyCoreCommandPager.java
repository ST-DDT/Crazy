package de.st_ddt.crazycore.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.CrazyPages;

public class CrazyCoreCommandPager extends CrazyCoreCommandExecutor
{

	public CrazyCoreCommandPager(final CrazyCore plugin)
	{
		super(plugin);
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length > 1)
			throw new CrazyCommandUsageException("[+/-]", "[Pagenumber]");
		if (args.length == 0)
		{
			CrazyPages.showPage(sender);
			return;
		}
		final String arg = args[0];
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
			catch (final NumberFormatException e)
			{
				throw new CrazyCommandUsageException("[+/-]", "[Pagenumber (Integer)]");
			}
		}
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		if (args.length > 1)
			return null;
		if (CrazyPages.getPages(sender) == null)
			return null;
		final List<String> res = new ArrayList<String>();
		res.add("+");
		res.add("-");
		final int pages = CrazyPages.getPages(sender).getMaxPage();
		for (int i = 1; i <= pages; i++)
			res.add(Integer.toString(i));
		return res;
	}
}
