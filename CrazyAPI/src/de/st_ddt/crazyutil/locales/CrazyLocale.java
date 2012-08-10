package de.st_ddt.crazyutil.locales;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyutil.ChatHelper;

public class CrazyLocale extends HashMap<String, CrazyLocale>
{

	private static final long serialVersionUID = 7789788937594284997L;
	private final static CrazyLocale locale = getCrazyLocaleHead();
	private final static CrazyLocale missing = getCrazyLocaleMissing();
	private final static HashMap<String, String> userLanguages = new HashMap<String, String>();
	private final static HashSet<String> languages = new HashSet<String>();
	private final static HashMap<String, HashSet<String>> languageAlternatives = new HashMap<String, HashSet<String>>();
	private static String defaultLanguage;
	private final String name;
	private final HashMap<String, String> localeTexts;
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
		getLocaleHead().addLanguageEntry("en_en", "CRAZYPLUGIN", "CRAZYPLUGIN");
		getLocaleHead().addLanguageEntry("en_en", "CRAZYPLUGIN.UPDATED", "$0$ has been updated. Updating language files.");
		getLocaleHead().addLanguageEntry("en_en", "CRAZYPLUGIN.LANGUAGE.ERROR.READ", "Failed reading $0$ language files!");
		getLocaleHead().addLanguageEntry("en_en", "CRAZYPLUGIN.LANGUAGE.ERROR.EXPORT", "Failed exporting $0$ language files!");
		getLocaleHead().addLanguageEntry("en_en", "CRAZYPLUGIN.LANGUAGE.ERROR.DOWNLOAD", "Failed downloading $0$ language files!");
		getLocaleHead().addLanguageEntry("en_en", "CRAZYPLUGIN.LANGUAGE.ERROR.AVAILABLE", "$0$ language files not available!");
		getLocaleHead().addLanguageEntry("en_en", "CRAZYPLUGIN.LANGUAGE.LOADED", "Language $0$ loaded sucessfully!");
		getLocaleHead().addLanguageEntry("en_en", "CRAZYPLUGIN.DATABASE.LOADED", "Loaded $0$ entries from database!");
		getLocaleHead().addLanguageEntry("en_en", "CRAZYPLUGIN.DATABASE.ACCESSWARN", "&CWARNING! Cannot access Database!");
		getLocaleHead().addLanguageEntry("en_en", "LANGUAGE.NAME", "English");
	}

	public static String getDefaultLanguage()
	{
		return defaultLanguage;
	}

	public static void setDefaultLanguage(final String defaultLanguage)
	{
		CrazyLocale.defaultLanguage = defaultLanguage;
	}

	public final static CrazyLocale getPluginHead(final CrazyPluginInterface plugin)
	{
		return getLocaleHead().getSecureLanguageEntry(plugin.getName());
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
		return getLocaleHead().getSecureLanguageEntry("LANGUAGE.NAME");
	}

	public static String getLanguageName(final String language)
	{
		return getLanguageName().getExactLanguageText(language);
	}

	public static String getLanguageName(final String language, final boolean appendLanguage)
	{
		final String res = getLanguageName(language);
		if (res == null)
			return null;
		return res + " (" + language + ")";
	}

	public static boolean isActiveLanguage(final String language)
	{
		return getLanguageName(language) != null;
	}

	public static Set<String> getActiveLanguages()
	{
		return getLanguageName().getLanguageTextMap().keySet();
	}

	public static List<String> getActiveLanguagesNames(final boolean appendLanguage)
	{
		final List<String> res = new ArrayList<String>();
		for (final String language : getActiveLanguages())
		{
			final String text = getLanguageName(language, appendLanguage);
			if (text == null)
				continue;
			res.add(text);
		}
		return res;
	}

	public static Set<String> getLanguageAlternatives(final String language)
	{
		final String[] split = language.split("_");
		HashSet<String> res = null;
		if (split.length > 1)
			res = languageAlternatives.get(split[1]);
		if (res == null)
			res = new HashSet<String>();
		return res;
	}

	public static boolean isValid(final CrazyLocale locale)
	{
		return locale != null && locale != getLocaleHead() && locale != getLocaleMissing();
	}

	public boolean isValid()
	{
		return this != getLocaleHead() && this != getLocaleMissing();
	}

	public CrazyLocale(final CrazyLocale parent, final String name)
	{
		super();
		this.name = name;
		this.parent = parent;
		this.localeTexts = new HashMap<String, String>();
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
		missing.setLanguageText("en_en", "This Language-Entry is missing!");
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
		CrazyLocale temp;
		for (final java.util.Map.Entry<String, CrazyLocale> subPath : this.entrySet())
		{
			temp = alternative.get(subPath.getKey());
			if (temp != null)
				subPath.getValue().setAlternative(temp);
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

	public HashMap<String, String> getLanguageTextMap()
	{
		return localeTexts;
	}

	public String getLanguageText(final CommandSender sender)
	{
		return getLanguageText(getUserLanguage(sender));
	}

	public String getLanguageText(final String language)
	{
		String res = localeTexts.get(language);
		if (res == null)
		{
			final Iterator<String> it = getLanguageAlternatives(language).iterator();
			while (res == null && it.hasNext())
				res = localeTexts.get(it.next());
			if (res == null)
				res = getDefaultLanguageText();
		}
		return res;
	}

	public String getExactLanguageText(final String language)
	{
		return localeTexts.get(language);
	}

	public String getDefaultLanguageText()
	{
		String res = localeTexts.get(defaultLanguage);
		if (res == null)
		{
			res = localeTexts.get("en_en");
			if (res == null)
			{
				if (localeTexts.isEmpty())
					return "EMPTY!";
				res = localeTexts.values().iterator().next();
				if (res == null)
					return "EMPTY!";
			}
		}
		return res;
	}

	public void setLanguageText(final String language, final String text)
	{
		this.localeTexts.put(language, text);
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
				final CrazyLocale alternative = locale.getAlternative();
				locale = locale.get(section);
				if (locale == null)
					if (alternative == null)
						throw new NullPointerException();
					else
						locale = alternative.get(section);
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
		for (final String section : path.split("\\."))
		{
			CrazyLocale temp = locale.get(section);
			if (temp == null)
			{
				temp = new CrazyLocale(locale, section);
				locale.put(section, temp);
				if (locale.getAlternative() != null)
					temp.setAlternative(locale.getAlternative().get(section));
			}
			locale = temp;
		}
		return locale;
	}

	public CrazyLocale addLanguageEntry(final String language, final String path, final String entry)
	{
		final CrazyLocale locale = getSecureLanguageEntry(path);
		locale.setLanguageText(language, ChatHelper.colorise(entry));
		return locale;
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
		for (final CrazyLocale locale : this.values())
			locale.print(language, sender);
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
		String res = userLanguages.get(name);
		if (res == null)
			res = defaultLanguage;
		return res;
	}

	public static void setUserLanguage(final CommandSender sender, final String language)
	{
		setUserLanguage(sender.getName(), language);
	}

	public static void setUserLanguage(final String name, final String language)
	{
		userLanguages.put(name, language);
	}

	public static boolean removeUserLanguage(final String name)
	{
		return userLanguages.remove(name) != null;
	}

	public static void loadLanguage(final String language)
	{
		if (languages.add(language))
		{
			if (!isActiveLanguage(language))
				return;
			try
			{
				final String[] split = language.split("_");
				final String shortLang = split[0];
				if (split.length > 0)
				{
					if (!languageAlternatives.containsKey(shortLang))
						languageAlternatives.put(shortLang, new HashSet<String>());
					languageAlternatives.get(shortLang).add(language);
				}
			}
			catch (final Exception e)
			{}
		}
	}

	public static Set<String> getLoadedLanguages()
	{
		return languages;
	}

	public static void save(final ConfigurationSection config, final String path)
	{
		for (final java.util.Map.Entry<String, String> user : userLanguages.entrySet())
			config.set(path + user.getKey(), user.getValue());
	}

	public static Set<String> load(final ConfigurationSection config)
	{
		final HashSet<String> languages = new HashSet<String>();
		if (config == null)
			return languages;
		for (final String name : config.getKeys(false))
		{
			final String language = config.getString(name);
			setUserLanguage(name, language);
			languages.add(language);
		}
		return languages;
	}

	@Override
	public String toString()
	{
		return getDefaultLanguageText();
	}
}
