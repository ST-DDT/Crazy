package de.st_ddt.crazyutil.modules.permissions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import de.st_ddt.crazyutil.modules.Module;

public class PermissionModule implements Module
{

	public final static List<Class<? extends PermissionSystem>> PERMISSIONSYSTEMS = new ArrayList<Class<? extends PermissionSystem>>();
	static
	{
		PERMISSIONSYSTEMS.add(PermissionPermissionsExSystem.class);
		PERMISSIONSYSTEMS.add(PermissionGroupManagerSystem.class);
		PERMISSIONSYSTEMS.add(PermissionBukkitSystem.class);
	}
	private static PermissionSystem permissionModule;

	public static void init(final String chatHeader, final CommandSender sender)
	{
		if (permissionModule == null)
			new PermissionModule().initialize(chatHeader, sender);
	}

	public PermissionSystem getPermissionSystem()
	{
		return permissionModule;
	}

	public static void setPermissionModule(final PermissionSystem permissionModule)
	{
		PermissionModule.permissionModule = permissionModule;
	}

	public static boolean hasPermission(final CommandSender sender, final String permission)
	{
		return permissionModule.hasPermission(sender, permission);
	}

	public PermissionModule()
	{
		super();
		MODULES.put("permission", this);
	}

	@Override
	public boolean initialize(final String chatHeader, final CommandSender sender)
	{
		sender.sendMessage(chatHeader + "Checking permission modules:");
		for (final Class<? extends PermissionSystem> clazz : PERMISSIONSYSTEMS)
		{
			final String name = clazz.getAnnotation(Named.class).name();
			sender.sendMessage(chatHeader + ChatColor.YELLOW + "- " + name);
			if (registerPermissionModule(clazz))
			{
				sender.sendMessage(chatHeader + "Activated " + ChatColor.GREEN + permissionModule.getName() + ChatColor.WHITE + " Permission Module!");
				return true;
			}
		}
		return false;
	}

	public static boolean registerPermissionModule(final Class<? extends PermissionSystem> clazz)
	{
		final PluginDepency plugin = clazz.getAnnotation(PluginDepency.class);
		if (plugin != null)
			if (Bukkit.getPluginManager().getPlugin(plugin.depend()) == null)
				return false;
		try
		{
			permissionModule = clazz.newInstance();
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
		return "PermissionModule";
	}

	@Override
	public boolean isActive()
	{
		return permissionModule != null;
	}
}
