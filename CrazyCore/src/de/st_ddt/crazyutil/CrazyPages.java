package de.st_ddt.crazyutil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import de.st_ddt.crazyplugin.CrazyLightPluginInterface;
import de.st_ddt.crazyplugin.data.ParameterData;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;

public class CrazyPages
{

	private static final HashMap<String, CrazyPages> storage = new HashMap<String, CrazyPages>();
	private final String chatHeader;
	private final String headFormat;
	private final String listFormat;
	private final String entryFormat;
	private final int amount;
	private int page;
	private final List<?> datas;

	public static CrazyPages storePages(final CommandSender sender, final String chatHeader, final String headFormat, final String listFormat, final String entryFormat, final int amount, final int page, final List<?> datas)
	{
		return storePages(sender.getName(), chatHeader, headFormat, listFormat, entryFormat, amount, page, datas);
	}

	public static CrazyPages storePages(final String name, final String chatHeader, final String headFormat, final String listFormat, final String entryFormat, final int amount, final int page, final List<?> datas)
	{
		final CrazyPages pages = new CrazyPages(chatHeader, headFormat, listFormat, entryFormat, amount, page, datas);
		storage.put(name.toLowerCase(), pages);
		return pages;
	}

	public static boolean hasPages(final CommandSender target)
	{
		return hasPages(target.getName());
	}

	public static boolean hasPages(final String target)
	{
		return storage.containsKey(target.toLowerCase());
	}

	public static CrazyPages getPages(final CommandSender target)
	{
		return getPages(target.getName());
	}

	public static CrazyPages getPages(final String target)
	{
		return storage.get(target.toLowerCase());
	}

	public static CrazyPages delPages(final CommandSender target)
	{
		return removePages(target.getName());
	}

	public static CrazyPages removePages(final String target)
	{
		return storage.remove(target.toLowerCase());
	}

	public static <S> void showPrevPage(final CommandSender target) throws CrazyCommandException
	{
		final CrazyPages data = storage.get(target.getName().toLowerCase());
		if (data == null)
			throw new CrazyCommandCircumstanceException("when a paged information has been shown");
		data.setPage(data.getPage() - 1);
		data.show(target);
	}

	public static <S> void showPage(final CommandSender target) throws CrazyCommandException
	{
		final CrazyPages data = storage.get(target.getName().toLowerCase());
		if (data == null)
			throw new CrazyCommandCircumstanceException("when a paged information has been shown");
		data.show(target);
	}

	public static <S> void showNextPage(final CommandSender target) throws CrazyCommandException
	{
		final CrazyPages data = storage.get(target.getName().toLowerCase());
		if (data == null)
			throw new CrazyCommandCircumstanceException("when a paged information has been shown");
		data.setPage(data.getPage() + 1);
		data.show(target);
	}

	public static <S> void showPage(final CommandSender target, final int page) throws CrazyCommandException
	{
		final CrazyPages data = storage.get(target.getName().toLowerCase());
		if (data == null)
			throw new CrazyCommandCircumstanceException("when a paged information has been shown");
		data.setPage(page);
		data.show(target);
	}

	public CrazyPages(final String chatHeader, final String headFormat, final String listFormat, final String entryFormat, final int amount, final int page, final List<?> datas)
	{
		super();
		this.chatHeader = chatHeader;
		this.headFormat = headFormat;
		this.listFormat = listFormat;
		this.entryFormat = entryFormat;
		this.amount = amount;
		this.page = page;
		this.datas = datas;
	}

	public int getPage()
	{
		return page;
	}

	public void setPage(final int page)
	{
		this.page = page;
	}

	public int getMaxPage()
	{
		return (datas.size() + amount - 1) / amount;
	}

	public void show(final CommandSender target)
	{
		int lastIndex = datas.size();
		page = Math.max(1, page);
		ChatHelper.sendMessage(target, "", headFormat, page, getMaxPage(), chatHeader, CrazyLightPluginInterface.DATETIMEFORMAT.format(new Date()));
		if (lastIndex + amount - 1 < page * amount)
			return;
		final StringBuilder formatString = new StringBuilder();
		for (int i = lastIndex; i > 0; i /= 10)
			formatString.append(" ");
		String format = formatString.toString();
		final int length = format.length();
		lastIndex = Math.min(lastIndex, page * amount);
		final StringBuilder message = new StringBuilder();
		if (target instanceof ConsoleCommandSender)
		{
			for (int i = page * amount - amount; i < lastIndex; i++)
				if (datas.get(i) instanceof ParameterData)
					message.append(ChatHelper.putArgs(listFormat, listShiftHelperConsole(String.valueOf(i + 1), length, format), ChatHelper.putArgsPara(target, entryFormat, (ParameterData) datas.get(i)), chatHeader));
				else if (datas.get(i) instanceof Showable)
					message.append(ChatHelper.putArgs(listFormat, listShiftHelperConsole(String.valueOf(i + 1), length, format), ((Showable) datas.get(i)).getShortInfo(), chatHeader));
				else
					message.append(ChatHelper.putArgs(listFormat, listShiftHelperConsole(String.valueOf(i + 1), length, format), datas.get(i).toString(), chatHeader));
		}
		else
		{
			format += format;
			for (int i = page * amount - amount; i < lastIndex; i++)
				if (datas.get(i) instanceof ParameterData)
					message.append(ChatHelper.putArgs(listFormat, listShiftHelper(String.valueOf(i + 1), length, format), ChatHelper.putArgsPara(target, entryFormat, (ParameterData) datas.get(i)), chatHeader));
				else if (datas.get(i) instanceof Showable)
					message.append(ChatHelper.putArgs(listFormat, listShiftHelperConsole(String.valueOf(i + 1), length, format), ((Showable) datas.get(i)).getShortInfo(), chatHeader));
				else
					message.append(ChatHelper.putArgs(listFormat, listShiftHelper(String.valueOf(i + 1), length, format), datas.get(i).toString(), chatHeader));
		}
		String listMessage = message.toString();
		if (listMessage.endsWith("\n"))
		{
			final int len = listMessage.length();
			listMessage = listMessage.substring(0, len - 1);
		}
		ChatHelper.sendFinalMessage(target, listMessage);
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
