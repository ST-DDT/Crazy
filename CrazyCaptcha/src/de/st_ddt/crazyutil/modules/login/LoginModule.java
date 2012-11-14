package de.st_ddt.crazyutil.modules.login;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyutil.modules.Module;

public class LoginModule implements Module
{

	public final static List<Class<? extends LoginSystem>> LOGINSYSTEMS = new ArrayList<Class<? extends LoginSystem>>();
	static
	{
		LOGINSYSTEMS.add(NoLoginSystem.class);
	}
	private static LoginSystem loginModule;

	public static void init(final String chatHeader, final CommandSender sender)
	{
		if (loginModule == null)
			new LoginModule().initialize(chatHeader, sender);
	}

	public LoginSystem getLoginSystem()
	{
		return loginModule;
	}

	public static void setPermissionModule(final LoginSystem loginModule)
	{
		LoginModule.loginModule = loginModule;
	}

	public static boolean hasAccount(final OfflinePlayer player)
	{
		return loginModule.hasAccount(player);
	}

	public static boolean isLoggedIn(final Player player)
	{
		return loginModule.isLoggedIn(player);
	}

	public LoginModule()
	{
		super();
		MODULES.put("login", this);
	}

	@Override
	public boolean initialize(final String chatHeader, final CommandSender sender)
	{
		sender.sendMessage(chatHeader + "Checking login modules:");
		for (final Class<? extends LoginSystem> clazz : LOGINSYSTEMS)
		{
			final String name = clazz.getAnnotation(Named.class).name();
			sender.sendMessage(chatHeader + ChatColor.YELLOW + "- " + name);
			if (registerLoginModule(clazz))
			{
				sender.sendMessage(chatHeader + "Activated " + ChatColor.GREEN + loginModule.getName() + ChatColor.WHITE + " Login Module!");
				return true;
			}
		}
		return false;
	}

	public static boolean registerLoginModule(final Class<? extends LoginSystem> clazz)
	{
		final PluginDepency plugin = clazz.getAnnotation(PluginDepency.class);
		if (plugin != null)
			if (Bukkit.getPluginManager().getPlugin(plugin.depend()) == null)
				return false;
		try
		{
			loginModule = clazz.newInstance();
			return true;
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public final String getName()
	{
		return "LoginModule";
	}

	@Override
	public boolean isActive()
	{
		return loginModule != null;
	}
}
