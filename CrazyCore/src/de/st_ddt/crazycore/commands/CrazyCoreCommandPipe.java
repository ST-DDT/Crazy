package de.st_ddt.crazycore.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.CrazyPipe;
import de.st_ddt.crazyutil.PreSetList;

public class CrazyCoreCommandPipe extends CrazyCoreCommandExecutor
{

	private final Map<String, PreSetList> preSets = PreSetList.PRESETLISTS;

	public CrazyCoreCommandPipe(final CrazyCore plugin)
	{
		super(plugin);
	}

	@Override
	public void command(final CommandSender sender, String[] args) throws CrazyException
	{
		if (args.length == 0)
			throw new CrazyCommandUsageException("<$PresetList> [> PipeCommand]", "<Arg1>, [Arg2], ... [> PipeCommand]");
		String[] pipeArgs = new String[] { "show", "$0$" };
		if (args[0].startsWith("$"))
		{
			final String value = args[0].toLowerCase().substring(1);
			final PreSetList preSet = preSets.get(value);
			if (preSet == null)
				throw new CrazyCommandNoSuchException("PresetList", args[0], preSets.keySet());
			final List<String> datas = preSet.getList();
			if (args.length > 1)
				if (args[1].equals(">"))
					pipeArgs = ChatHelperExtended.shiftArray(args, 2);
				else
					throw new CrazyCommandUsageException("<$PresetList> [> PipeCommand]");
			for (final String data : datas)
				CrazyPipe.pipe(sender, data, pipeArgs);
			return;
		}
		for (int i = 0; i < args.length; i++)
			if (args[i].equals(">"))
			{
				pipeArgs = ChatHelperExtended.shiftArray(args, i + 1);
				args = ChatHelperExtended.cutArray(args, i);
				break;
			}
		final String arg = ChatHelper.listingString(" ", args);
		for (final String data : arg.split(","))
			CrazyPipe.pipe(sender, data.trim(), pipeArgs);
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		if (args.length == 0)
		{
			final List<String> res = new ArrayList<String>();
			for (final String preSet : preSets.keySet())
				res.add("$" + preSet);
			return res;
		}
		else if (args.length == 1)
		{
			final List<String> res = new ArrayList<String>();
			String arg = args[0];
			if (!arg.startsWith("$"))
				return null;
			arg = arg.substring(1);
			for (final String preSet : preSets.keySet())
				if (preSet.startsWith(arg))
					res.add("$" + preSet);
			return res;
		}
		else
			return null;
	}
}
