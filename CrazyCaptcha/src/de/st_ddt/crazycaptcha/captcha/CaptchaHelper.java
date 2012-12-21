package de.st_ddt.crazycaptcha.captcha;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazycaptcha.CrazyCaptcha;

public class CaptchaHelper
{

	private static final Map<String, Class<? extends CaptchaGenerator>> generators = new TreeMap<String, Class<? extends CaptchaGenerator>>();

	protected CaptchaHelper()
	{
		super();
	}

	public static CaptchaGenerator getCaptchaGenerator(final CrazyCaptcha plugin, final ConfigurationSection config)
	{
		if (config == null)
			return null;
		final String name = config.getString("name");
		Class<? extends CaptchaGenerator> clazz = null;
		if (name != null)
			clazz = generators.get(name.toLowerCase());
		if (clazz == null)
		{
			final String type = config.getString("type");
			if (type != null)
				try
				{
					clazz = Class.forName(type).asSubclass(CaptchaGenerator.class);
				}
				catch (final ClassNotFoundException e)
				{}
		}
		if (clazz == null)
			return null;
		try
		{
			return clazz.getConstructor(CrazyCaptcha.class, ConfigurationSection.class).newInstance(plugin, config);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static void registerGenerator(final String name, final Class<? extends CaptchaGenerator> clazz)
	{
		generators.put(name.toLowerCase(), clazz);
	}

	public static Set<String> getAvailableGenerators()
	{
		return generators.keySet();
	}
}
