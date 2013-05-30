package de.st_ddt.crazyplugin.commands;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.Permission;

public class CrazyPluginCommandMainReload<S extends CrazyPluginInterface> extends CrazyCommandReload<S> implements CrazyPluginCommandExecutorInterface<S>
{

	protected final Reloadable configReloadable = new Reloadable()
	{

		@Override
		@Localized("$CRAZYPLUGIN$.COMMAND.CONFIG.RELOADED")
		public void reload(final CommandSender sender) throws CrazyException
		{
			plugin.reloadConfig();
			plugin.loadConfiguration();
			plugin.saveConfiguration();
			plugin.sendLocaleMessage("COMMAND.CONFIG.RELOADED", sender);
		}

		@Override
		@Permission("$CRAZYPLUGIN$.reload.config")
		public boolean hasAccessPermission(final CommandSender sender)
		{
			return PermissionModule.hasPermission(sender, plugin.getName().toLowerCase() + ".reload.config") || PermissionModule.hasPermission(sender, plugin.getName().toLowerCase() + ".reload.*");
		}
	};

	public CrazyPluginCommandMainReload(final S plugin)
	{
		super(plugin);
		addReloadable(configReloadable, "c", "cfg", "config");
	}

	@Override
	public final S getPlugin()
	{
		return plugin;
	}

	@Override
	@Permission({ "$CRAZYPLUGIN$.reload", "$CRAZYPLUGIN$.reload.*" })
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, plugin.getName().toLowerCase() + ".reload");
	}

	@Override
	public Reloadable getDefaultReloadable()
	{
		return allReloadable;
	}
}
