package de.st_ddt.crazyutil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.data.ParameterData;
import de.st_ddt.crazyplugin.data.PlayerDataInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUnsupportedException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public abstract class CrazyPipe
{

	private final static HashMap<String, CrazyPipe> pipes = new HashMap<String, CrazyPipe>();
	protected static boolean disabled;

	public static CrazyPipe registerPipe(final CrazyPipe pipe, final String... keys)
	{
		for (final String key : keys)
			pipes.put(key, pipe);
		return pipe;
	}

	public static void pipe(final CommandSender sender, final Collection<? extends ParameterData> datas, final String... pipeArgs) throws CrazyException
	{
		if (disabled)
			return;
		if (!PermissionModule.hasPermission(sender, "crazypipe.use"))
			throw new CrazyCommandPermissionException();
		if (datas.size() == 0)
			return;
		if (pipeArgs == null)
		{
			defaultPipe(sender, datas);
			return;
		}
		final int length = pipeArgs.length;
		if (length == 0)
		{
			defaultPipe(sender, datas);
			return;
		}
		for (int i = 0; i < length; i++)
			if (pipeArgs[i].equals("|"))
			{
				pipe(sender, datas, ChatHelperExtended.cutArray(pipeArgs, i));
				pipe(sender, datas, ChatHelperExtended.shiftArray(pipeArgs, i + 1));
				return;
			}
		final CrazyPipe pipe = pipes.get(pipeArgs[0].toLowerCase());
		if (pipe == null)
		{
			commandPipe(sender, datas, pipeArgs);
			return;
		}
		else
			pipe.execute(sender, datas, ChatHelperExtended.shiftArray(pipeArgs, 1));
	}

	public static void pipe(final CommandSender sender, final ParameterData data, final String... pipeArgs) throws CrazyException
	{
		if (disabled)
			return;
		if (!PermissionModule.hasPermission(sender, "crazypipe.use"))
			throw new CrazyCommandPermissionException();
		if (pipeArgs == null)
		{
			defaultPipe(sender, data);
			return;
		}
		final int length = pipeArgs.length;
		if (length == 0)
		{
			defaultPipe(sender, data);
			return;
		}
		for (int i = 0; i < length; i++)
			if (pipeArgs[i].equals("|"))
			{
				pipe(sender, data, ChatHelperExtended.cutArray(pipeArgs, i));
				pipe(sender, data, ChatHelperExtended.shiftArray(pipeArgs, i + 1));
				return;
			}
		commandPipe(sender, data, pipeArgs);
	}

	public static void pipe(final CommandSender sender, final String data, final String... pipeArgs) throws CrazyException
	{
		if (disabled)
			return;
		if (!PermissionModule.hasPermission(sender, "crazypipe.use"))
			throw new CrazyCommandPermissionException();
		if (pipeArgs == null)
			return;
		final int length = pipeArgs.length;
		if (length == 0)
			return;
		for (int i = 0; i < length; i++)
			if (pipeArgs[i].equals("|"))
			{
				pipe(sender, data, ChatHelperExtended.cutArray(pipeArgs, i));
				pipe(sender, data, ChatHelperExtended.shiftArray(pipeArgs, i + 1));
				return;
			}
		commandPipe(sender, data, pipeArgs);
	}

	private static void defaultPipe(final CommandSender sender, final Collection<? extends ParameterData> datas)
	{
		for (final ParameterData data : datas)
			sender.sendMessage(data.getShortInfo());
	}

	private static void defaultPipe(final CommandSender sender, final ParameterData data)
	{
		data.show(sender);
	}

	private static void commandPipe(final CommandSender sender, final Collection<? extends ParameterData> datas, final String... pipeArgs)
	{
		for (final ParameterData data : datas)
			commandPipe(sender, data, pipeArgs);
	}

	private static void commandPipe(final CommandSender sender, final ParameterData data, final String... pipeArgs)
	{
		final String command = ChatHelper.listingString(" ", pipeArgs);
		Bukkit.dispatchCommand(sender, shiftPipeEntry(ChatHelper.putArgsPara(sender, command, data)));
	}

	private static void commandPipe(final CommandSender sender, final String data, final String... pipeArgs)
	{
		final String command = ChatHelper.listingString(" ", pipeArgs);
		if (command.startsWith("show "))
		{
			ChatHelper.sendMessage(sender, "", command.substring(5), data);
		}
		else
			Bukkit.dispatchCommand(sender, shiftPipeEntry(ChatHelper.putArgs(command, data)));
	}

	public static String[] shiftPipe(final String... args)
	{
		final int length = args.length;
		final String[] newArgs = new String[length];
		for (int i = 0; i < length; i++)
			newArgs[i] = shiftPipeEntry(args[i]);
		return newArgs;
	}

	public static String shiftPipeEntry(final String arg)
	{
		return arg.replace("$&", "$").replace("|&", "|");
	}

	public abstract void execute(CommandSender sender, Collection<? extends ParameterData> datas, String... pipeArgs) throws CrazyException;

	public static boolean isDisabled()
	{
		return disabled;
	}

	public static void setDisabled(final boolean disabled)
	{
		CrazyPipe.disabled = disabled;
	}

	static
	{
		registerPipe(new CrazyPipe()
		{

			@Override
			public void execute(final CommandSender sender, final Collection<? extends ParameterData> datas, final String... pipeArgs) throws CrazyException
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
						newArgs = shiftPipe(ChatHelperExtended.shiftArray(pipeArgs, a + 1));
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
			public void execute(final CommandSender sender, final Collection<? extends ParameterData> datas, final String... pipeArgs) throws CrazyException
			{
				final String[] newArgs = shiftPipe(ChatHelperExtended.shiftArray(pipeArgs, 1));
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
			public void execute(final CommandSender sender, final Collection<? extends ParameterData> datas, final String... pipeArgs) throws CrazyException
			{
				final String[] newArgs = shiftPipe(ChatHelperExtended.shiftArray(pipeArgs, 1));
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
			public void execute(final CommandSender sender, final Collection<? extends ParameterData> datas, final String... pipeArgs) throws CrazyException
			{
				final String[] args = ChatHelperExtended.processListCommand(sender, pipeArgs, "", null, null, null, null, null, new ArrayList<ParameterData>(datas));
				if (args != null)
					throw new CrazyCommandUnsupportedException("PipeCommand", args);
			}
		}, "page");
		registerPipe(new CrazyPipe()
		{

			@Override
			public void execute(final CommandSender sender, final Collection<? extends ParameterData> datas, final String... pipeArgs)
			{
				final String message = ChatHelper.colorise(ChatHelper.listingString(" ", pipeArgs));
				for (final ParameterData data : datas)
					sender.sendMessage(ChatHelper.putArgsPara(sender, message, data));
			}
		}, "show");
		registerPipe(new CrazyPipe()
		{

			@Override
			public void execute(final CommandSender sender, final Collection<? extends ParameterData> datas, final String... pipeArgs)
			{
				String separator = "----------------------------------------";
				if (pipeArgs.length > 0)
					separator = ChatHelper.listingString(" ", pipeArgs);
				for (final ParameterData data : datas)
					if (data instanceof PlayerDataInterface)
					{
						sender.sendMessage(separator);
						((PlayerDataInterface) data).show(sender, "", true);
					}
				sender.sendMessage(separator);
			}
		}, "show+");
	}
}
