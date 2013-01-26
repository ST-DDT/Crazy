package de.st_ddt.crazyplugin.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHeaderProvider;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.ListFormat;
import de.st_ddt.crazyutil.Tabbed;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.paramitrisable.IntegerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;

public abstract class CrazyCommandCollectionEditor<S extends ChatHeaderProvider, T> extends CrazyCommandTreeExecutor<S>
{

	public CrazyCommandCollectionEditor(final S chatHeaderProvider)
	{
		this(chatHeaderProvider, true, true);
	}

	@Deprecated
	@SuppressWarnings("unchecked")
	public CrazyCommandCollectionEditor(final CrazyPluginInterface chatHeaderProvider)
	{
		// EDIT remove compatibility code
		this((S) chatHeaderProvider, true, true);
	}

	@Deprecated
	@SuppressWarnings("unchecked")
	public CrazyCommandCollectionEditor(final CrazyPluginInterface chatHeaderProvider, final boolean add, final boolean remove)
	{
		this((S) chatHeaderProvider, add, remove);
	}

	public CrazyCommandCollectionEditor(final S chatHeaderProvider, final boolean add, final boolean remove)
	{
		super(chatHeaderProvider, null);
		defaultExecutor = new CrazyCommandCollectionList(plugin);
		addSubCommand(defaultExecutor, "list");
		if (add)
			addSubCommand(new CrazyCommandCollectionAdd(plugin), "add", "+");
		if (remove)
			addSubCommand(new CrazyCommandCollectionRemove(plugin), "rem", "remove", "del", "delete", "-");
	}

	public abstract Collection<T> getCollection();

	public abstract ListFormat listFormat();

	public abstract T getEntry(CommandSender sender, String... args) throws CrazyException;

	public void showList(final CommandSender sender, final int amount, final int page)
	{
		ChatHelperExtended.sendList(sender, plugin.getChatHeader(), listFormat(), amount, page, new ArrayList<T>(getCollection()));
	}

	// @ // Localized("PATH $Element$")
	public abstract String addLocale();

	// @ // Localized("PATH $Element$")
	public abstract String removeLocale();

	/**
	 * Save changes. Executed after add and remove
	 */
	protected void saveChanges()
	{
	}

	private class CrazyCommandCollectionList extends CrazyCommandExecutor<S>
	{

		public CrazyCommandCollectionList(final S plugin)
		{
			super(plugin);
		}

		@Override
		public void command(final CommandSender sender, final String[] args) throws CrazyException
		{
			if (args.length > 2)
				throw new CrazyCommandUsageException("[page:Integer] [amount:Integer]");
			final Map<String, Paramitrisable> params = new HashMap<String, Paramitrisable>();
			final IntegerParamitrisable page = new IntegerParamitrisable(1)
			{

				@Override
				public void setParameter(final String parameter) throws CrazyException
				{
					if (parameter.equals("*"))
						value = Integer.MIN_VALUE;
					else
						super.setParameter(parameter);
				}
			};
			final IntegerParamitrisable amount = new IntegerParamitrisable(10);
			ChatHelperExtended.readParameters(args, params, page, amount);
			showList(sender, amount.getValue(), page.getValue());
		}

		@Override
		public List<String> tab(final CommandSender sender, final String[] args)
		{
			final Map<String, Tabbed> params = new HashMap<String, Tabbed>();
			final IntegerParamitrisable page = new IntegerParamitrisable(1);
			final IntegerParamitrisable amount = new IntegerParamitrisable(10);
			return ChatHelperExtended.tabHelp(args, params, page, amount);
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
			final T elem = getEntry(sender, args);
			getCollection().add(elem);
			saveChanges();
			CrazyLocale.getLocaleHead().getLanguageEntry(addLocale()).sendMessage(sender, elem);
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
			final T elem = getEntry(sender, args);
			getCollection().remove(elem);
			saveChanges();
			CrazyLocale.getLocaleHead().getLanguageEntry(removeLocale()).sendMessage(sender, elem);
		}
	}
}
