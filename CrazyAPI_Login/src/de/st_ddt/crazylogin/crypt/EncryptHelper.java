package de.st_ddt.crazylogin.crypt;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazylogin.LoginPlugin;
import de.st_ddt.crazylogin.data.LoginData;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public final class EncryptHelper
{

	private static final TreeMap<String, Class<? extends Encryptor>> encryptors = new TreeMap<String, Class<? extends Encryptor>>();

	public static Encryptor getEncryptor(final LoginPlugin<? extends LoginData> plugin, final ConfigurationSection config)
	{
		if (config == null)
			return null;
		final String algorithm = config.getString("name");
		Class<? extends Encryptor> clazz = null;
		if (algorithm != null)
			clazz = encryptors.get(algorithm.toLowerCase());
		if (clazz == null)
		{
			final String type = config.getString("type");
			if (type != null)
				try
				{
					clazz = Class.forName(type).asSubclass(Encryptor.class);
				}
				catch (final ClassNotFoundException e)
				{}
		}
		if (clazz == null)
			return null;
		try
		{
			return clazz.getConstructor(LoginPlugin.class, ConfigurationSection.class).newInstance(plugin, config);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Required for updating Encryptor names in new Version!
	 */
	public static Encryptor getEncryptor(final LoginPlugin<? extends LoginData> plugin, final String algorithm, final ConfigurationSection config)
	{
		final Class<? extends Encryptor> clazz = encryptors.get(algorithm.toLowerCase());
		if (clazz == null)
			return null;
		try
		{
			return clazz.getConstructor(LoginPlugin.class, ConfigurationSection.class).newInstance(plugin, config);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static Encryptor getEncryptor(final LoginPlugin<? extends LoginData> plugin, final String algorithm, final String[] args) throws CrazyException
	{
		final Class<? extends Encryptor> clazz = encryptors.get(algorithm.toLowerCase());
		if (clazz == null)
			return null;
		try
		{
			return clazz.getConstructor(LoginPlugin.class, String[].class).newInstance(plugin, args);
		}
		catch (final InvocationTargetException e)
		{
			final Throwable t = e.getCause();
			if (t instanceof CrazyCommandException)
				((CrazyCommandException) t).addCommandPrefix(algorithm);
			if (t instanceof CrazyException)
				throw (CrazyException) t;
			e.printStackTrace();
			return null;
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static void registerAlgorithm(final String algorithm, final Class<? extends Encryptor> clazz)
	{
		encryptors.put(algorithm.toLowerCase(), clazz);
	}

	public static Set<String> getAlgorithms()
	{
		return encryptors.keySet();
	}

	private EncryptHelper()
	{
		super();
	}

	public static String byteArrayToHexString(final byte... args)
	{
		final StringBuilder res = new StringBuilder();
		final String[] chars = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
		for (final byte arg : args)
		{
			res.append(chars[(arg >> 4) + 16]);
			res.append(chars[arg % 16 + 16]);
		}
		return res.toString();
	}
}
