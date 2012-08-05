package de.st_ddt.crazyutil;

import java.util.HashMap;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public class CrazyPage<S>
{

	protected static final HashMap<String, CrazyPage<?>> pageMap = new HashMap<String, CrazyPage<?>>();
	final String chatHeader;
	final CrazyLocale headLocale;
	final CrazyLocale seperator;
	final CrazyLocale entry;
	final CrazyLocale emptyPage;
	final int amount;
	int page;
	final List<? extends S> datas;
	final EntryDataGetter<S> getter;

	public static <S> CrazyPage<S> storePagedData(final CommandSender target, final String chatHeader, final CrazyLocale headLocale, final CrazyLocale seperator, final CrazyLocale entry, final CrazyLocale emptyPage, final int amount, final int page, final List<? extends S> datas, final EntryDataGetter<S> getter)
	{
		return storePagedData(target.getName(), chatHeader, headLocale, seperator, entry, emptyPage, amount, page, datas, getter);
	}

	public static <S> CrazyPage<S> storePagedData(final String target, final String chatHeader, final CrazyLocale headLocale, final CrazyLocale seperator, final CrazyLocale entry, final CrazyLocale emptyPage, final int amount, final int page, final List<? extends S> datas, final EntryDataGetter<S> getter)
	{
		final CrazyPage<S> data = new CrazyPage<S>(chatHeader, headLocale, seperator, entry, emptyPage, amount, page, datas, getter);
		pageMap.put(target.toLowerCase(), data);
		return data;
	}

	public static CrazyPage<?> getPagedData(final CommandSender target)
	{
		return getPagedData(target.getName());
	}

	public static CrazyPage<?> getPagedData(final String target)
	{
		return pageMap.get(target.toLowerCase());
	}

	public static <S> void showPrevPage(final CommandSender target) throws CrazyCommandException
	{
		final CrazyPage<?> data = pageMap.get(target.getName().toLowerCase());
		if (data == null)
			throw new CrazyCommandCircumstanceException("when a paged information has been shown");
		data.setPage(data.getPage() - 1);
		data.show(target);
	}

	public static <S> void showPage(final CommandSender target) throws CrazyCommandException
	{
		final CrazyPage<?> data = pageMap.get(target.getName().toLowerCase());
		if (data == null)
			throw new CrazyCommandCircumstanceException("when a paged information has been shown");
		data.show(target);
	}

	public static <S> void showNextPage(final CommandSender target) throws CrazyCommandException
	{
		final CrazyPage<?> data = pageMap.get(target.getName().toLowerCase());
		if (data == null)
			throw new CrazyCommandCircumstanceException("when a paged information has been shown");
		data.setPage(data.getPage() + 1);
		data.show(target);
	}

	public static <S> void showPage(final CommandSender target, final int page) throws CrazyCommandException
	{
		final CrazyPage<?> data = pageMap.get(target.getName().toLowerCase());
		if (data == null)
			throw new CrazyCommandCircumstanceException("when a paged information has been shown");
		data.setPage(page);
		data.show(target);
	}

	private CrazyPage(final String chatHeader, final CrazyLocale headLocale, final CrazyLocale seperator, final CrazyLocale entry, final CrazyLocale emptyPage, final int amount, final int page, final List<? extends S> datas, final EntryDataGetter<S> getter)
	{
		super();
		this.chatHeader = chatHeader;
		this.headLocale = headLocale;
		this.seperator = seperator;
		this.entry = entry;
		this.emptyPage = emptyPage;
		this.amount = Math.max(amount, 1);
		this.page = page;
		this.datas = datas;
		this.getter = getter;
	}

	public String getChatHeader()
	{
		return chatHeader;
	}

	public CrazyLocale getHeadLocale()
	{
		return headLocale;
	}

	public CrazyLocale getSeperator()
	{
		return seperator;
	}

	public CrazyLocale getEntry()
	{
		return entry;
	}

	public CrazyLocale getEmptyPage()
	{
		return emptyPage;
	}

	public int getAmount()
	{
		return amount;
	}

	public int getPage()
	{
		return page;
	}

	public void setPage(final int page)
	{
		this.page = page;
	}

	public List<? extends S> getDatas()
	{
		return datas;
	}

	public EntryDataGetter<S> getGetter()
	{
		return getter;
	}

	public void show(final CommandSender target)
	{
		int lastIndex = datas.size();
		page = Math.max(1, page);
		ChatHelper.sendMessage(target, chatHeader, headLocale, page, (datas.size() + amount - 1) / amount);
		ChatHelper.sendMessage(target, chatHeader, seperator);
		if (lastIndex + amount - 1 < page * amount)
		{
			ChatHelper.sendMessage(target, chatHeader, emptyPage, page);
			return;
		}
		final StringBuilder formatString = new StringBuilder();
		for (int i = lastIndex; i > 0; i /= 10)
			formatString.append(" ");
		String format = formatString.toString();
		final int length = format.length();
		lastIndex = Math.min(lastIndex, page * amount);
		if (target instanceof ConsoleCommandSender)
		{
			for (int i = page * amount - amount; i < lastIndex; i++)
				ChatHelper.sendMessage(target, chatHeader, entry, listShiftHelperConsole(String.valueOf(i + 1), length, format), getter.getEntryData(datas.get(i)));
		}
		else
		{
			format += format;
			for (int i = page * amount - amount; i < lastIndex; i++)
				ChatHelper.sendMessage(target, chatHeader, entry, listShiftHelper(String.valueOf(i + 1), length, format), getter.getEntryData(datas.get(i)));
		}
	}

	private static String listShiftHelper(final String number, final int length, final String filler)
	{
		final String res = filler + number;
		return res.substring(number.length() * 2);
	}

	private static String listShiftHelperConsole(final String number, final int length, final String filler)
	{
		final String res = filler + number;
		return res.substring(number.length());
	}
}
