package de.st_ddt.crazyutil.locales;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

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
	private final PairList<String, String> localeTexts;
	private final CrazyLocale parent;
	private CrazyLocale alternative = null;

	public final static CrazyLocale getLocaleHead()
	{
		return locale;
	}

	public final static CrazyLocale getLocaleMissing()
	{
		return missing;
	}

	static
	{
		getLocaleHead().addLanguageEntry("en_en", "CRAZYPLUGIN.LANGUAGE.ERROR.READ", "Failed reading $0$ language files!");
		getLocaleHead().addLanguageEntry("en_en", "CRAZYPLUGIN.LANGUAGE.ERROR.EXPORT", "Failed exporting $0$ language files!");
		getLocaleHead().addLanguageEntry("en_en", "CRAZYPLUGIN.LANGUAGE.ERROR.DOWNLOAD", "Failed downloading $0$ language files!");
		getLocaleHead().addLanguageEntry("en_en", "CRAZYPLUGIN.LANGUAGE.ERROR.AVAILABLE", "$0$ language files not available!");
		getLocaleHead().addLanguageEntry("en_en", "CRAZYPLUGIN.LANGUAGE.LOADED", "Language $0$ loaded sucessfully!");
		getLocaleHead().addLanguageEntry("en_en", "LANGUAGE.NAME", "English");
	}

	public final static CrazyLocale getPluginHead(final CrazyPlugin plugin)
	{
		getLocaleHead().addLanguageEntry("root", plugin.getName().toUpperCase(), plugin.getName());
		return getLocaleHead().getLanguageEntry(plugin.getName());
	}

	public final static CrazyLocale getUnit(final String unit)
	{
		return getLocaleHead().getLanguageEntry("UNIT." + unit.toUpperCase());
	}

	public final static String getUnitText(final String unit, final CommandSender sender)
	{
		return getUnit(unit).getLanguageText(sender);
	}

	public final static String getUnitText(final String unit, final String language)
	{
		return getUnit(unit).getLanguageText(language);
	}

	public static CrazyLocale getLanguageName()
	{
		return getLocaleHead().getLanguageEntry("LANGUAGE.NAME");
	}

	public static String getLanguageName(String language)
	{
		return getLocaleHead().getLanguageEntry("LANGUAGE.NAME").getExactLanguageText(language);
	}

	public static String getLanguageName(String language, boolean appendLanguage)
	{
		String res = getLanguageName(language);
		if (res == null)
			return null;
		return res + " (" + language + ")";
	}

	public static List<String> getActiveLanguages()
	{
		return getLanguageName().getData1List();
	}

	public static List<String> getActiveLanguagesNames(boolean appendLanguage)
	{
		List<String> res = new ArrayList<String>();
		for (String language : getActiveLanguages())
		{
			String text = getLanguageName(language, appendLanguage);
			if (text == null)
				continue;
			res.add(text);
		}
		return res;
	}

	public static boolean isValid(final CrazyLocale locale)
	{
		return locale != null && locale != getLocaleHead() && locale != getLocaleMissing();
	}

	public CrazyLocale(final CrazyLocale parent, final String name)
	{
		super();
		this.name = name;
		this.parent = parent;
		this.localeTexts = new PairList<String, String>();
	}

	private static CrazyLocale getCrazyLocaleHead()
	{
		final CrazyLocale head = new CrazyLocale(null, "_HEAD_");
		head.setLanguageText("en_en", "This Entry is the root!");
		return head;
	}

	private static CrazyLocale getCrazyLocaleMissing()
	{
		final CrazyLocale missing = new CrazyLocale(null, "_MISSING_");
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

	public CrazyLocale getAlternative()
	{
		return alternative;
	}

	public void setAlternative(final CrazyLocale alternative)
	{
		this.alternative = alternative;
		updateAlternative();
	}

	public void updateAlternative()
	{
		if (alternative == null)
			return;
		CrazyLocale locale;
		for (final Pair<String, CrazyLocale> subPath : alternative)
		{
			locale = this.findDataVia1(subPath.getData1());
			if (locale != null)
				locale.setAlternative(subPath.getData2());
		}
	}

	public String getLocaleMessage(final CommandSender target, final String localePath, final Object... args)
	{
		return ChatHelper.putArgsExtended(target, getLanguageEntry(localePath), args);
	}

	public String getDefaultLocaleMessage(final String localePath, final Object... args)
	{
		return ChatHelper.putArgs(getLanguageEntry(localePath).getDefaultLanguageText(), args);
	}

	public String getLanguageText(final CommandSender sender)
	{
		return getLanguageText(getUserLanguage(sender));
	}

	public String getLanguageText(final String language)
	{
		String res = localeTexts.findDataVia1(language);
		if (res == null)
		{
			res = getDefaultLanguageText();
		}
		return res;
	}

	public String getExactLanguageText(String language)
	{
		return localeTexts.findDataVia1(language);
	}

	public String getDefaultLanguageText()
	{
		String res = localeTexts.findDataVia1(CrazyCore.getDefaultLanguage());
		if (res == null)
		{
			res = localeTexts.findDataVia1("en_en");
			if (res == null)
			{
				if (localeTexts.size() == 0)
					return "EMPTY!";
				if (localeTexts.get(0) == null)
					return "EMPTY!";
				res = localeTexts.get(0).getData2();
				if (res == null)
					return "EMPTY!";
			}
		}
		return res;
	}

	public void setLanguageText(final String language, final String text)
	{
		this.localeTexts.setDataVia1(language, text);
	}

	public void sendMessage(final CommandSender target)
	{
		target.sendMessage(getLanguageText(target));
	}

	public void sendMessage(final CommandSender... targets)
	{
		for (final CommandSender target : targets)
			sendMessage(target);
	}

	public void sendMessage(final CommandSender target, final Object... args)
	{
		ChatHelper.sendMessage(target, this, args);
	}

	public void sendMessage(final CommandSender[] targets, final Object... args)
	{
		for (final CommandSender target : targets)
			sendMessage(target, args);
	}

	public CrazyLocale getLanguageEntry(String path)
	{
		path = path.toUpperCase();
		CrazyLocale locale = this;
		try
		{
			for (final String section : path.split("\\."))
			{
				locale = locale.findDataVia1(section);
				if (locale == null)
					if (alternative == null)
						throw new NullPointerException();
					else
						locale = alternative.findDataVia1(section);
			}
			if (locale == null)
				throw new NullPointerException();
		}
		catch (final NullPointerException e)
		{
			System.out.println("[CrazyLocale] Missing language entry " + this.getPath() + "." + path);
			return missing;
		}
		return locale;
	}

	public CrazyLocale getSecureLanguageEntry(String path)
	{
		path = path.toUpperCase();
		CrazyLocale locale = this;
		CrazyLocale parent = this;
		for (final String section : path.split("\\."))
		{
			locale = parent.findDataVia1(section);
			if (locale == null)
				locale = parent.setDataVia1(section, new CrazyLocale(parent, section)).getData2();
			parent = locale;
		}
		return locale;
	}

	private void addLanguageEntry(final String language, String path, final String entry)
	{
		path = path.toUpperCase();
		CrazyLocale locale = this;
		CrazyLocale parent = this;
		for (final String section : path.split("\\."))
		{
			locale = parent.findDataVia1(section);
			if (locale == null)
			{
				locale = parent.setDataVia1(section, new CrazyLocale(parent, section)).getData2();
				if (parent.getAlternative() != null)
					locale.setAlternative(parent.getAlternative().findDataVia1(section));
			}
			parent = locale;
		}
		locale.setLanguageText(language, ChatHelper.colorise(entry));
	}

	public static void readFile(final String language, final Reader reader) throws IOException
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
			catch (final IndexOutOfBoundsException e)
			{}
			String[] split = null;
			if (!zeile.equals("") && !zeile.startsWith("#"))
			{
				split = zeile.split("=", 2);
				try
				{
					locale.addLanguageEntry(language, split[0], split[1]);
				}
				catch (final ArrayIndexOutOfBoundsException e)
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
				catch (final ArrayIndexOutOfBoundsException e)
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

	public static void printAll(final String language, final CommandSender sender)
	{
		sender.sendMessage("Complete Language Print!");
		getLocaleHead().print(language, sender);
	}

	public static void printAll(final String language)
	{
		printAll(Bukkit.getConsoleSender());
	}

	public static void printAll(final CommandSender sender)
	{
		printAll(getUserLanguage(sender), sender);
	}

	public void print(final String language, final CommandSender sender)
	{
		sender.sendMessage(getPath() + ":" + getLanguageText(language));
		for (final Pair<String, CrazyLocale> pair : this)
			pair.getData2().print(language, sender);
	}

	public void print(final CommandSender sender)
	{
		print(getUserLanguage(sender), sender);
	}

	public static String getUserLanguage(final CommandSender sender)
	{
		return getUserLanguage(sender.getName());
	}

	public static String getUserLanguage(final String name)
	{
		String res = userLanguages.findDataVia1(name);
		if (res == null)
			res = CrazyCore.getDefaultLanguage();
		return res;
	}

	public static void setUserLanguage(final CommandSender sender, final String language)
	{
		setUserLanguage(sender.getName(), language);
	}

	public static void setUserLanguage(final String name, final String language)
	{
		userLanguages.setDataVia1(name, language);
		loadLanguage(language);
	}

	public static void loadLanguage(final String language)
	{
		if (!languages.contains(language))
		{
			for (final CrazyPlugin plugin : CrazyPlugin.getCrazyPlugins())
				plugin.loadLanguage(language);
		}
		if (!languages.contains(language))
			languages.add(language);
	}

	public static ArrayList<String> getLoadedLanguages()
	{
		return languages;
	}

	public static void save(final ConfigurationSection config, final String path)
	{
		for (final Pair<String, String> user : userLanguages)
			config.set(path + user.getData1(), user.getData2());
	}

	public static void load(final ConfigurationSection config)
	{
		if (config == null)
			return;
		for (final String name : config.getKeys(false))
			setUserLanguage(name, config.getString(name));
	}

	@Override
	public String toString()
	{
		return getDefaultLanguageText();
	}
}
