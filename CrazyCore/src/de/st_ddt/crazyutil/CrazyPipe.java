package de.st_ddt.crazyutil;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.CrazyLightPluginInterface;
import de.st_ddt.crazyplugin.data.ParameterData;
import de.st_ddt.crazyplugin.data.PlayerDataInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandErrorException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUnsupportedException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.ColoredStringParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.FileParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.StringParamitrisable;
import de.st_ddt.crazyutil.source.Permission;

public abstract class CrazyPipe
{

	private final static Map<String, CrazyPipe> pipes = new HashMap<String, CrazyPipe>();
	protected static boolean disabled;

	public static CrazyPipe registerPipe(final CrazyPipe pipe, final String... keys)
	{
		for (final String key : keys)
			pipes.put(key, pipe);
		return pipe;
	}

	@Permission("crazypipe.use")
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
			commandPipe(sender, datas, pipeArgs);
		else
			pipe.execute(sender, datas, ChatHelperExtended.shiftArray(pipeArgs, 1));
	}

	@Permission("crazypipe.use")
	public static void pipe(final CommandSender sender, final Collection<String> datas, final boolean foo, final String... pipeArgs) throws CrazyException
	{
		if (disabled)
			return;
		if (!PermissionModule.hasPermission(sender, "crazypipe.use"))
			throw new CrazyCommandPermissionException();
		if (pipeArgs == null)
		{
			defaultPipe(sender, datas, false);
			return;
		}
		final int length = pipeArgs.length;
		if (length == 0)
		{
			defaultPipe(sender, datas, false);
			return;
		}
		for (int i = 0; i < length; i++)
			if (pipeArgs[i].equals("|"))
			{
				pipe(sender, datas, false, ChatHelperExtended.cutArray(pipeArgs, i));
				pipe(sender, datas, false, ChatHelperExtended.shiftArray(pipeArgs, i + 1));
				return;
			}
		final CrazyPipe pipe = pipes.get(pipeArgs[0].toLowerCase());
		if (pipe == null)
			commandPipe(sender, datas, false, pipeArgs);
		else
			pipe.execute(sender, datas, false, ChatHelperExtended.shiftArray(pipeArgs, 1));
	}

	public static List<String> tabHelp(final CommandSender sender, final String[] pipeArgs)
	{
		final int length = pipeArgs.length;
		if (length == 0)
			return null;
		for (int i = 0; i < length; i++)
			if (pipeArgs[i].equals("|"))
				return tabHelp(sender, ChatHelperExtended.shiftArray(pipeArgs, i + 1));
		final CrazyPipe pipe = pipes.get(pipeArgs[0].toLowerCase());
		if (pipe == null)
		{
			final List<String> res = new LinkedList<String>();
			final PluginCommand command = Bukkit.getPluginCommand(pipeArgs[0]);
			if (pipeArgs.length == 1)
			{
				for (final String cmd : CommandHelper.getCommandNames())
					if (cmd.toLowerCase().startsWith(pipeArgs[0].toLowerCase()))
						res.add(cmd);
				for (final String cmd : pipes.keySet())
					if (cmd.startsWith(pipeArgs[0].toLowerCase()))
						res.add(cmd);
			}
			else if (command != null)
				res.addAll(command.tabComplete(sender, pipeArgs[0], ChatHelperExtended.shiftArray(pipeArgs, 1)));
			return res;
		}
		else
			return pipe.tab(sender, ChatHelperExtended.shiftArray(pipeArgs, 1));
	}

	@Permission("crazypipe.use")
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

	@Permission("crazypipe.use")
	public static void pipe(final CommandSender sender, final String data, final String... pipeArgs) throws CrazyException
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

	private static void defaultPipe(final CommandSender sender, final Collection<? extends ParameterData> datas)
	{
		for (final ParameterData data : datas)
			sender.sendMessage(data.getShortInfo());
	}

	private static void defaultPipe(final CommandSender sender, final Collection<String> datas, final boolean foo)
	{
		for (final String data : datas)
			sender.sendMessage(data);
	}

	private static void defaultPipe(final CommandSender sender, final ParameterData data)
	{
		data.show(sender);
	}

	private static void defaultPipe(final CommandSender sender, final String data)
	{
		sender.sendMessage(data);
	}

	private static void commandPipe(final CommandSender sender, final Collection<? extends ParameterData> datas, final String... pipeArgs)
	{
		for (final ParameterData data : datas)
			commandPipe(sender, data, pipeArgs);
	}

	private static void commandPipe(final CommandSender sender, final Collection<String> datas, final boolean foo, final String... pipeArgs)
	{
		for (final String data : datas)
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
			ChatHelper.sendMessage(sender, "", command.substring(5), data);
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

	public abstract void execute(CommandSender sender, Collection<String> datas, boolean foo, String... pipeArgs) throws CrazyException;

	public List<String> tab(final CommandSender sender, final String[] parameter)
	{
		return new LinkedList<String>();
	}

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
						try
						{
							final String[] split = part.split("-");
							final int begin = Math.max(Math.min(entries.size(), Integer.parseInt(split[0])), 0);
							final int ende = Math.max(Math.min(entries.size(), Integer.parseInt(split[split.length - 1])), begin);
							for (int i = begin; i <= ende; i++)
								newEntries.add(entries.get(i - 1));
						}
						catch (final NumberFormatException e)
						{
							throw new CrazyCommandParameterException(a, "NumberRange");
						}
				}
				pipe(sender, newEntries, newArgs);
			}

			@Override
			public void execute(final CommandSender sender, final Collection<String> datas, final boolean foo, final String... pipeArgs) throws CrazyException
			{
				String[] newArgs = null;
				final LinkedList<String> newEntries = new LinkedList<String>();
				final ArrayList<String> entries = new ArrayList<String>();
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
						try
						{
							final String[] split = part.split("-");
							final int begin = Math.max(Math.min(entries.size(), Integer.parseInt(split[0])), 0);
							final int ende = Math.max(Math.min(entries.size(), Integer.parseInt(split[split.length - 1])), begin);
							for (int i = begin; i <= ende; i++)
								newEntries.add(entries.get(i - 1));
						}
						catch (final NumberFormatException e)
						{
							throw new CrazyCommandParameterException(a, "NumberRange");
						}
				}
				pipe(sender, newEntries, false, newArgs);
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

			@Override
			public void execute(final CommandSender sender, final Collection<String> datas, final boolean foo, final String... pipeArgs) throws CrazyException
			{
				final String[] newArgs = shiftPipe(ChatHelperExtended.shiftArray(pipeArgs, 1));
				final LinkedList<String> newEntries = new LinkedList<String>();
				final ArrayList<String> entries = new ArrayList<String>();
				entries.addAll(datas);
				newEntries.add(entries.get(0));
				pipe(sender, newEntries, false, newArgs);
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

			@Override
			public void execute(final CommandSender sender, final Collection<String> datas, final boolean foo, final String... pipeArgs) throws CrazyException
			{
				final String[] newArgs = shiftPipe(ChatHelperExtended.shiftArray(pipeArgs, 1));
				final LinkedList<String> newEntries = new LinkedList<String>();
				final ArrayList<String> entries = new ArrayList<String>();
				entries.addAll(datas);
				newEntries.add(entries.get(entries.size() - 1));
				pipe(sender, newEntries, false, newArgs);
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

			@Override
			public void execute(final CommandSender sender, final Collection<String> datas, final boolean foo, final String... pipeArgs) throws CrazyException
			{
				final String[] args = ChatHelperExtended.processListCommand(sender, pipeArgs, "", null, null, null, null, null, new ArrayList<String>(datas));
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

			@Override
			public void execute(final CommandSender sender, final Collection<String> datas, final boolean foo, final String... pipeArgs) throws CrazyException
			{
				final String message = ChatHelper.colorise(ChatHelper.listingString(" ", pipeArgs));
				for (final String data : datas)
					sender.sendMessage(ChatHelper.putArgs(message, data));
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

			@Override
			public void execute(final CommandSender sender, final Collection<String> datas, final boolean foo, final String... pipeArgs) throws CrazyException
			{
				final String message = ChatHelper.colorise(ChatHelper.listingString(" ", pipeArgs));
				final String separator = "----------------------------------------";
				for (final String data : datas)
				{
					sender.sendMessage(separator);
					sender.sendMessage(ChatHelper.putArgs(message, data));
				}
			}
		}, "show+");
		registerPipe(new CrazyPipe()
		{

			@Override
			public void execute(final CommandSender sender, final Collection<? extends ParameterData> datas, final String... pipeArgs) throws CrazyException
			{
				final Map<String, Paramitrisable> params = new HashMap<String, Paramitrisable>();
				final FileParamitrisable file = new FileParamitrisable(CrazyCore.getPlugin().getDataFolder().getAbsoluteFile().getParentFile().getParentFile(), null);
				params.put("file", file);
				final BooleanParamitrisable append = new BooleanParamitrisable(false);
				params.put("append", append);
				final StringParamitrisable chatHeader = new ColoredStringParamitrisable("");
				params.put("chatheader", chatHeader);
				final StringParamitrisable headFormat = new ColoredStringParamitrisable(null);
				params.put("headformat", headFormat);
				final StringParamitrisable listFormat = new ColoredStringParamitrisable("$1$\n");
				params.put("listformat", listFormat);
				final StringParamitrisable entryFormat = new ColoredStringParamitrisable("$0$");
				params.put("entryformat", entryFormat);
				ChatHelperExtended.readParameters(pipeArgs, params, file);
				if (file.getValue() == null)
					throw new CrazyCommandNoSuchException("File", "(null)");
				int i = 0;
				try (final Writer writer = new FileWriter(file.getValue(), append.getValue()))
				{
					if (headFormat.getValue() != null)
						writer.write(ChatHelper.putArgs(headFormat.getValue() + "\n", "", "", chatHeader.getValue(), CrazyLightPluginInterface.DATETIMEFORMAT.format(new Date())));
					for (final ParameterData data : datas)
						writer.write(ChatHelper.putArgs(listFormat.getValue(), Integer.toString(i++), ChatHelper.putArgsPara(sender, entryFormat.getValue(), data), chatHeader.getValue()));
				}
				catch (final IOException e)
				{
					throw new CrazyCommandErrorException(e);
				}
			}

			@Override
			public void execute(final CommandSender sender, final Collection<String> datas, final boolean foo, final String... pipeArgs) throws CrazyException
			{
				final Map<String, Paramitrisable> params = new HashMap<String, Paramitrisable>();
				final FileParamitrisable file = new FileParamitrisable(CrazyCore.getPlugin().getDataFolder().getAbsoluteFile().getParentFile().getParentFile(), null);
				params.put("file", file);
				final BooleanParamitrisable append = new BooleanParamitrisable(false);
				params.put("append", append);
				final StringParamitrisable chatHeader = new ColoredStringParamitrisable("");
				params.put("chatheader", chatHeader);
				final StringParamitrisable headFormat = new ColoredStringParamitrisable(null);
				params.put("headformat", headFormat);
				final StringParamitrisable listFormat = new ColoredStringParamitrisable("$1$\n");
				params.put("listformat", listFormat);
				final StringParamitrisable entryFormat = new ColoredStringParamitrisable("$0$");
				params.put("entryformat", entryFormat);
				ChatHelperExtended.readParameters(pipeArgs, params, file);
				if (file.getValue() == null)
					throw new CrazyCommandUsageException("File", "(null)");
				int i = 0;
				try (final Writer writer = new FileWriter(file.getValue(), append.getValue()))
				{
					if (headFormat.getValue() != null)
						writer.write(ChatHelper.putArgs(headFormat.getValue() + "\n", "", "", chatHeader.getValue(), CrazyLightPluginInterface.DATETIMEFORMAT.format(new Date())));
					for (final String data : datas)
						writer.write(ChatHelper.putArgs(listFormat.getValue(), Integer.toString(i++), ChatHelper.putArgs(entryFormat.getValue(), data), chatHeader.getValue()));
				}
				catch (final IOException e)
				{
					throw new CrazyCommandErrorException(e);
				}
			}

			@Override
			public List<String> tab(final CommandSender sender, final String[] args)
			{
				final Map<String, Tabbed> params = new HashMap<String, Tabbed>();
				final FileParamitrisable file = new FileParamitrisable(CrazyCore.getPlugin().getDataFolder().getAbsoluteFile().getParentFile().getParentFile(), null);
				params.put("file", file);
				final BooleanParamitrisable append = new BooleanParamitrisable(false);
				params.put("append", append);
				final StringParamitrisable chatHeader = new ColoredStringParamitrisable(null);
				params.put("chatheader", chatHeader);
				final StringParamitrisable headFormat = new ColoredStringParamitrisable(null);
				params.put("headformat", headFormat);
				final StringParamitrisable listFormat = new ColoredStringParamitrisable(null);
				params.put("listformat", listFormat);
				final StringParamitrisable entryFormat = new ColoredStringParamitrisable(null);
				params.put("entryformat", entryFormat);
				return ChatHelperExtended.tabHelp(args, params, file);
			}
		}, "log", "file");
		registerPipe(new CrazyPipe()
		{

			@Override
			public void execute(final CommandSender sender, final Collection<? extends ParameterData> datas, final String... pipeArgs) throws CrazyException
			{
				try
				{
					pipe(sender, datas, pipeArgs);
				}
				catch (final CrazyException e)
				{
					e.show(sender);
				}
			}

			@Override
			public void execute(final CommandSender sender, final Collection<String> datas, final boolean foo, final String... pipeArgs) throws CrazyException
			{
				try
				{
					pipe(sender, datas, false, pipeArgs);
				}
				catch (final CrazyException e)
				{
					e.show(sender);
				}
			}

			@Override
			public List<String> tab(final CommandSender sender, final String[] parameter)
			{
				return tabHelp(sender, parameter);
			}
		}, "save");
	}
}
