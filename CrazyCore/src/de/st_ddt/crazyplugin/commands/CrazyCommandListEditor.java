package de.st_ddt.crazyplugin.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;

public abstract class CrazyCommandListEditor<S extends CrazyPluginInterface, T> extends CrazyCommandCollectionEditor<S, T>
{

	public CrazyCommandListEditor(final S plugin)
	{
		this(plugin, true, true, true);
	}

	public CrazyCommandListEditor(final S plugin, final boolean add, final boolean insert, final boolean remove)
	{
		super(plugin, add, false);
		if (insert)
			addSubCommand(new CrazyCommandListInsert(plugin), "insert");
		if (remove)
			addSubCommand(new CrazyCommandCollectionRemove(plugin), "rem", "remove", "del", "delete", "-");
	}

	@Override
	public abstract List<T> getCollection();

	@Override
	public void showList(final CommandSender sender, final int amount, final int page)
	{
		plugin.sendLocaleList(sender, listFormat(), amount, page, getCollection());
	}

	public abstract String addViaIndexLocale();

	public abstract String removeViaIndexLocale();

	private class CrazyCommandListInsert extends CrazyCommandExecutor<S>
	{

		public CrazyCommandListInsert(final S plugin)
		{
			super(plugin);
		}

		@Override
		public void command(final CommandSender sender, final String[] args) throws CrazyException
		{
			try
			{
				final int index = Math.min(Integer.parseInt(args[0]), getCollection().size());
				if (index < 0)
					throw new CrazyCommandParameterException(0, "positive Number (Integer)");
				try
				{
					final T elem = getEntry(ChatHelperExtended.shiftArray(args, 1));
					getCollection().add(index, elem);
					plugin.sendLocaleMessage(addViaIndexLocale(), sender, elem, index);
				}
				catch (final CrazyCommandException e)
				{
					e.addCommandPrefix(args[0]);
					throw e;
				}
			}
			catch (final NumberFormatException e)
			{
				throw new CrazyCommandParameterException(0, "Number (Integer)");
			}
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
			if (args.length == 1)
				try
				{
					final int index = Integer.parseInt(args[0]);
					final T elem = getCollection().remove(index);
					plugin.sendLocaleMessage(removeViaIndexLocale(), sender, elem, index);
					return;
				}
				catch (final Exception e)
				{}
			final T elem = getEntry(args);
			getCollection().remove(elem);
			plugin.sendLocaleMessage(removeLocale(), sender, elem);
		}
	}
}
