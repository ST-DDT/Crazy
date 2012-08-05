package de.st_ddt.crazyutil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.data.ParameterData;

public abstract class CrazyPipe
{

	private final static HashMap<String, CrazyPipe> pipes = new HashMap<String, CrazyPipe>();

	public static CrazyPipe registerPipe(final CrazyPipe pipe, final String... keys)
	{
		for (final String key : keys)
			pipes.put(key, pipe);
		return pipe;
	}

	public static void pipe(final CommandSender sender, final Collection<ParameterData> datas, final String... pipeArgs)
	{
		if (datas.size() == 0)
			return;
		if (pipeArgs == null)
		{
			defaultPipe(sender, datas, new String[0]);
			return;
		}
		if (pipeArgs.length == 0)
		{
			defaultPipe(sender, datas, pipeArgs);
			return;
		}
		int length = pipeArgs.length;
		for (int i = 0; i < length; i++)
			if (pipeArgs[i].equals("|"))
			{
				pipe(sender, datas, ChatHelper.cutArray(pipeArgs, i));
				pipe(sender, datas, ChatHelper.shiftArray(pipeArgs, i + 1));
				return;
			}
		final CrazyPipe pipe = pipes.get(pipeArgs[0].toLowerCase());
		if (pipe == null)
		{
			commandPipe(sender, datas, pipeArgs);
			return;
		}
		else
			pipe.execute(sender, datas, ChatHelper.shiftArray(pipeArgs, 1));
	}

	public static void pipe(final CommandSender sender, final ParameterData data, final String... pipeArgs)
	{
		if (pipeArgs == null)
		{
			data.show(sender);
			return;
		}
		if (pipeArgs.length == 0)
		{
			data.show(sender);
			return;
		}
		commandPipe(sender, data, pipeArgs);
	}

	private static void defaultPipe(final CommandSender sender, final Collection<ParameterData> datas, final String... strings)
	{
		for (final ParameterData data : datas)
			sender.sendMessage(data.getShortInfo(strings));
	}

	private static void commandPipe(final CommandSender sender, final Collection<ParameterData> datas, final String... pipeArgs)
	{
		for (final ParameterData data : datas)
			commandPipe(sender, data, pipeArgs);
	}

	private static void commandPipe(final CommandSender sender, final ParameterData data, final String... pipeArgs)
	{
		String command = ChatHelper.listingString(" ", pipeArgs);
		Bukkit.dispatchCommand(sender, shiftPipeEntry(ChatHelper.putArgsPara(command, data)));
	}

	public static String[] shiftPipe(final String... args)
	{
		final int length = args.length;
		final String[] newArgs = new String[length];
		for (int i = 0; i < length; i++)
			newArgs[i] = shiftPipeEntry(args[i]);
		return newArgs;
	}

	public static String shiftPipeEntry(String arg)
	{
		return arg.replace("$&", "$").replace("|&", "|");
	}

	public abstract void execute(CommandSender sender, Collection<ParameterData> datas, String... pipeArgs);

	static
	{
		registerPipe(new CrazyPipe()
		{

			@Override
			public void execute(final CommandSender sender, final Collection<ParameterData> datas, final String... pipeArgs)
			{
				String[] newArgs = null;
				final LinkedList<ParameterData> newEntries = new LinkedList<ParameterData>();
				final ArrayList<ParameterData> entries = new ArrayList<ParameterData>();
				entries.addAll(datas);
				final int length = pipeArgs.length;
				for (int a = 0; a < length; a++)
				{
					final String arg = pipeArgs[a];
					if (arg.equals(">"))
					{
						newArgs = shiftPipe(ChatHelper.shiftArray(pipeArgs, a + 1));
						break;
					}
					for (final String part : arg.split(","))
					{
						final String[] split = part.split("-");
						final int begin = Math.max(Math.min(entries.size(), Integer.parseInt(split[0])), 0);
						final int ende = Math.max(Math.min(entries.size(), Integer.parseInt(split[split.length - 1])), begin);
						for (int i = begin; i <= ende; i++)
							newEntries.add(entries.get(i - 1));
					}
				}
				pipe(sender, newEntries, newArgs);
			}
		}, "entry", "entries");
		registerPipe(new CrazyPipe()
		{

			@Override
			public void execute(final CommandSender sender, final Collection<ParameterData> datas, final String... pipeArgs)
			{
				String[] newArgs = shiftPipe(ChatHelper.shiftArray(pipeArgs, 1));
				final LinkedList<ParameterData> newEntries = new LinkedList<ParameterData>();
				final ArrayList<ParameterData> entries = new ArrayList<ParameterData>();
				entries.addAll(datas);
				newEntries.add(entries.get(0));
				pipe(sender, newEntries, newArgs);
			}
		}, "first");
		registerPipe(new CrazyPipe()
		{

			@Override
			public void execute(final CommandSender sender, final Collection<ParameterData> datas, final String... pipeArgs)
			{
				String[] newArgs = shiftPipe(ChatHelper.shiftArray(pipeArgs, 1));
				final LinkedList<ParameterData> newEntries = new LinkedList<ParameterData>();
				final ArrayList<ParameterData> entries = new ArrayList<ParameterData>();
				entries.addAll(datas);
				newEntries.add(entries.get(entries.size() - 1));
				pipe(sender, newEntries, newArgs);
			}
		}, "last");
		registerPipe(new CrazyPipe()
		{

			@Override
			public void execute(final CommandSender sender, final Collection<ParameterData> datas, final String... pipeArgs)
			{
				int page = 1;
				int amount = 10;
				final int length = pipeArgs.length;
				for (int i = 0; i < length; i++)
				{
					final String arg = pipeArgs[i].toLowerCase();
					if (arg.startsWith("p:"))
						page = Integer.parseInt(arg.substring(2));
					else if (arg.startsWith("page:"))
						page = Integer.parseInt(arg.substring(5));
					else if (arg.equals("a:*"))
						amount = -1;
					else if (arg.startsWith("a:"))
						amount = Integer.parseInt(arg.substring(2));
					else if (arg.equals("amount:*"))
						amount = -1;
					else if (arg.startsWith("amount:"))
						amount = Integer.parseInt(arg.substring(7));
					else if (i == 0)
						page = Integer.parseInt(arg);
					else if (arg.equals("*"))
						amount = -1;
					else
						amount = Integer.parseInt(arg);
				}
				final ArrayList<ParameterData> entries = new ArrayList<ParameterData>(datas);
				ChatHelper.sendListMessage(sender, "", "CRAZYPLUGIN.LIST.HEAD", null, null, null, amount, page, entries, new ShowableDataGetter());
			}
		}, "page");
		registerPipe(new CrazyPipe()
		{

			@Override
			public void execute(final CommandSender sender, final Collection<ParameterData> datas, final String... pipeArgs)
			{
				String message = ChatHelper.listingString(" ", pipeArgs);
				for (ParameterData data : datas)
					sender.sendMessage(ChatHelper.putArgsPara(message, data));
			}
		}, "show");
	}
}
