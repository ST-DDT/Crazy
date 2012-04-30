package de.st_ddt.crazyutil.locales;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.Pair;
import de.st_ddt.crazyutil.PairList;

public class CrazyLocale extends PairList<String, CrazyLocale>
{

	private static final long serialVersionUID = 7789788937594284997L;
	private final static CrazyLocale locale = getCrazyLocaleHead();
	private final static CrazyLocale missing = getCrazyLocaleMissing();
	private final static PairList<String, String> userLanguages = new PairList<String, String>();
	private final static ArrayList<String> languages = new ArrayList<String>();
	private final String name;
	private PairList<String, String> localeTexts;
	private final CrazyLocale parent;

	public final static CrazyLocale getLocaleHead()
	{
		return locale;
	}

	public final static CrazyLocale getLocaleMissing()
	{
		return missing;
	}

	public final static CrazyLocale getPluginHead(CrazyPlugin plugin)
	{
		getLocaleHead().addLanguageEntry("root", plugin.getName().toUpperCase(), plugin.getName());
		return getLocaleHead().getLanguageEntry(plugin.getName());
	}

	public static boolean isValid(CrazyLocale locale)
	{
		return locale != null && locale != getLocaleHead() && locale != getLocaleMissing();
	}

	public CrazyLocale(CrazyLocale parent, String name)
	{
		super();
		this.name = name;
		this.parent = parent;
		this.localeTexts = new PairList<String, String>();
	}

	private static CrazyLocale getCrazyLocaleHead()
	{
		CrazyLocale head = new CrazyLocale(null, "_HEAD_");
		head.setLanguageText("en_en", "This Entry is the root!");
		return head;
	}

	private static CrazyLocale getCrazyLocaleMissing()
	{
		CrazyLocale missing = new CrazyLocale(null, "_MISSING_");
		missing.setLanguageText("en_en", "This Entry is missing!");
		return missing;
	}

	public String getName()
	{
		return name;
	}

	public String getPath()
	{
		if (parent == null)
			return name;
		return parent.getPath() + "." + name;
	}

	public String getLocaleMessage(CommandSender sender, String localePath, Object... args)
	{
		return ChatHelper.putArgs(getLanguageEntry(localePath).getLanguageText(getUserLanguage(sender)), args);
	}

	public String getLanguageText(CommandSender sender)
	{
		return getLanguageText(getUserLanguage(sender));
	}

	public String getLanguageText(String language)
	{
		String res = localeTexts.findDataVia1(language);
		if (res == null)
		{
			res = localeTexts.findDataVia1(CrazyCore.getDefaultLanguage());
			if (res == null)
			{
				res = localeTexts.findDataVia1("en_en");
				if (res == null)
				{
					if (localeTexts.get(0) == null)
						return "EMPTY!";
					res = localeTexts.get(0).getData2();
					if (res == null)
						return "EMPTY!";
				}
			}
		}
		return res;
	}

	public void setLanguageText(String language, String text)
	{
		this.localeTexts.setDataVia1(language, text);
	}

	public void sendMessage(CommandSender target)
	{
		target.sendMessage(getLanguageText(target));
	}

	public void sendMessage(CommandSender... targets)
	{
		for (CommandSender target : targets)
			sendMessage(target);
	}

	public void sendMessage(CommandSender target, Object... args)
	{
		target.sendMessage(ChatHelper.putArgs(getLanguageText(target), args));
	}

	public void sendMessage(CommandSender[] targets, Object... args)
	{
		for (CommandSender target : targets)
			sendMessage(target, args);
	}

	public CrazyLocale getLanguageEntry(String path)
	{
		path = path.toUpperCase();
		CrazyLocale locale = this;
		try
		{
			for (String section : path.split("\\."))
				locale = locale.findDataVia1(section);
			if (locale == null)
				throw new NullPointerException();
		}
		catch (NullPointerException e)
		{
			System.out.println("[CrazyLocale] Missing language entry " + this.getPath() + "." + path);
			return missing;
		}
		return locale;
	}

	private void addLanguageEntry(String language, String path, String entry)
	{
		path = path.toUpperCase();
		CrazyLocale locale = this;
		CrazyLocale parent = this;
		for (String section : path.split("\\."))
		{
			locale = parent.findDataVia1(section);
			if (locale == null)
				locale = parent.setDataVia1(section, new CrazyLocale(parent, section)).getData2();
			parent = locale;
		}
		locale.setLanguageText(language, ChatHelper.colorise(entry));
	}

	public static void readFile(String language, Reader reader) throws IOException
	{
		BufferedReader bufreader = null;
		read: try
		{
			bufreader = new BufferedReader(reader);
			String zeile = bufreader.readLine();
			if (zeile == null)
				break read;
			try
			{
				// Remove UTF-8 BOM (Windows, Linux)
				if (zeile.getBytes()[0] == (byte) 63)
					zeile = zeile.substring(1);
				else if (zeile.getBytes()[0] == (byte) 239)
					if (zeile.getBytes()[1] == (byte) 187)
						if (zeile.getBytes()[2] == (byte) 191)
							zeile = zeile.substring(3);
			}
			catch (IndexOutOfBoundsException e)
			{}
			String[] split = null;
			if (!zeile.equals("") && !zeile.startsWith("#"))
			{
				split = zeile.split("=", 2);
				try
				{
					locale.addLanguageEntry(language, split[0], split[1]);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
					System.err.println("Invalid line " + zeile);
				}
			}
			while ((zeile = bufreader.readLine()) != null)
			{
				if (zeile.startsWith("#"))
					continue;
				split = zeile.split("=", 2);
				try
				{
					locale.addLanguageEntry(language, split[0], split[1]);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
					System.err.println("Invalid line " + zeile);
				}
			}
		}
		finally
		{
			if (bufreader != null)
				bufreader.close();
		}
	}

	public static void printAll(String language, CommandSender sender)
	{
		sender.sendMessage("Complete Language Print!");
		getLocaleHead().print(language, sender);
	}

	public static void printAll(String language)
	{
		printAll(Bukkit.getConsoleSender());
	}

	public static void printAll(CommandSender sender)
	{
		printAll(getUserLanguage(sender), sender);
	}

	public void print(String language, CommandSender sender)
	{
		sender.sendMessage(getPath() + ":" + getLanguageText(language));
		for (Pair<String, CrazyLocale> pair : this)
			pair.getData2().print(language, sender);
	}

	public void print(CommandSender sender)
	{
		print(getUserLanguage(sender), sender);
	}

	public static String getUserLanguage(CommandSender sender)
	{
		return getUserLanguage(sender.getName());
	}

	public static String getUserLanguage(String name)
	{
		String res = userLanguages.findDataVia1(name);
		if (res == null)
			res = CrazyCore.getDefaultLanguage();
		return res;
	}

	public static void setUserLanguage(CommandSender sender, String language)
	{
		setUserLanguage(sender.getName(), language);
	}

	public static void setUserLanguage(String name, String language)
	{
		userLanguages.setDataVia1(name, language);
		loadLanguage(language);
	}

	public static void loadLanguage(String language)
	{
		if (!languages.contains(language))
		{
			for (CrazyPlugin plugin : CrazyPlugin.getCrazyPlugins())
				plugin.loadLanguage(language);
		}
		if (!languages.contains(language))
			languages.add(language);
	}

	public static ArrayList<String> getLoadedLanguages()
	{
		return languages;
	}

	public static void save(ConfigurationSection config, String path)
	{
		for (Pair<String, String> user : userLanguages)
			config.set(path + user.getData1(), user.getData2());
	}

	public static void load(ConfigurationSection config)
	{
		if (config == null)
			return;
		for (String name : config.getKeys(false))
			setUserLanguage(name, config.getString(name));
	}
}
