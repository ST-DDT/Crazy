package de.st_ddt.crazyplugin.commands;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ListFormat;

public abstract class CrazyCommandCollectionEditor<S extends CrazyPluginInterface, T> extends CrazyCommandTreeExecutor<S>
{

	public CrazyCommandCollectionEditor(final S plugin)
	{
		this(plugin, true, true);
	}

	public CrazyCommandCollectionEditor(final S plugin, final boolean add, final boolean remove)
	{
		super(plugin, null);
		defaultExecutor = new CrazyCommandCollectionList(plugin);
		addSubCommand(defaultExecutor, "list");
		if (add)
			addSubCommand(new CrazyCommandCollectionAdd(plugin), "add", "+");
		if (remove)
			addSubCommand(new CrazyCommandCollectionRemove(plugin), "rem", "remove", "del", "delete", "-");
	}

	public abstract Collection<T> getCollection();

	public abstract ListFormat listFormat();

	public abstract T getEntry(String... args) throws CrazyException;

	public void showList(final CommandSender sender, final int amount, final int page)
	{
		plugin.sendLocaleList(sender, listFormat(), amount, page, new ArrayList<T>(getCollection()));
	}

	public abstract String addLocale();

	public abstract String removeLocale();

	private class CrazyCommandCollectionList extends CrazyCommandExecutor<S>
	{

		public CrazyCommandCollectionList(final S plugin)
		{
			super(plugin);
		}

		@Override
		public void command(final CommandSender sender, final String[] args) throws CrazyException
		{
			int amount = 10;
			int page = 1;
			final int length = args.length;
			if (length > 2)
				throw new CrazyCommandUsageException("[amount:Integer] [[page:]Integer]");
			for (int i = 0; i < length; i++)
			{
				final String arg = args[i].toLowerCase();
				if (arg.startsWith("page:"))
					try
					{
						page = Integer.parseInt(arg.substring(5));
						if (page < 0)
							throw new CrazyCommandParameterException(i, "positive Integer");
					}
					catch (final NumberFormatException e)
					{
						throw new CrazyCommandParameterException(i, "Number (Integer)");
					}
				else if (arg.startsWith("amount:"))
				{
					if (arg.substring(7).equals("*"))
						amount = -1;
					else
						try
						{
							amount = Integer.parseInt(arg.substring(7));
						}
						catch (final NumberFormatException e)
						{
							throw new CrazyCommandParameterException(i, "Number (Integer)");
						}
				}
				else if (arg.equals("*"))
				{
					page = Integer.MIN_VALUE;
				}
				else
					try
					{
						page = Integer.parseInt(arg);
					}
					catch (final NumberFormatException e)
					{
						throw new CrazyCommandUsageException("[amount:Integer] [[page:]Integer]");
					}
			}
			showList(sender, amount, page);
		}
	}

	private class CrazyCommandCollectionAdd extends CrazyCommandExecutor<S>
	{

		public CrazyCommandCollectionAdd(final S plugin)
		{
			super(plugin);
		}

		@Override
		public void command(final CommandSender sender, final String[] args) throws CrazyException
		{
			final T elem = getEntry(args);
			getCollection().add(elem);
			plugin.sendLocaleMessage(addLocale(), sender, elem);
		}
	}

	private class CrazyCommandCollectionRemove extends CrazyCommandExecutor<S>
	{

		public CrazyCommandCollectionRemove(final S plugin)
		{
			super(plugin);
		}

		@Override
		public void command(final CommandSender sender, final String[] args) throws CrazyException
		{
			final T elem = getEntry(args);
			getCollection().remove(elem);
			plugin.sendLocaleMessage(removeLocale(), sender, elem);
		}
	}
}
