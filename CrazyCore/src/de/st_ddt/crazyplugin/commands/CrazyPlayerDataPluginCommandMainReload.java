package de.st_ddt.crazyplugin.commands;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPlayerDataPluginInterface;
import de.st_ddt.crazyplugin.data.PlayerDataInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.Permission;

public class CrazyPlayerDataPluginCommandMainReload<T extends PlayerDataInterface> extends CrazyPluginCommandMainReload<CrazyPlayerDataPluginInterface<T, ? extends T>> implements CrazyPlayerDataPluginCommandExecutorInterface<T, CrazyPlayerDataPluginInterface<T, ? extends T>>
{

	protected final Reloadable databaseReloadable = new Reloadable()
	{

		@Override
		@Localized("$CRAZYPLUGIN$.COMMAND.DATABASE.RELOADED")
		public void reload(final CommandSender sender) throws CrazyException
		{
			plugin.loadDatabase();
			plugin.saveDatabase();
			plugin.sendLocaleMessage("COMMAND.DATABASE.RELOADED", sender);
		}

		@Override
		@Permission("$CRAZYPLUGIN$.reload.database")
		public boolean hasAccessPermission(final CommandSender sender)
		{
			return PermissionModule.hasPermission(sender, plugin.getName().toLowerCase() + ".reload.database") || PermissionModule.hasPermission(sender, plugin.getName().toLowerCase() + ".reload.*");
		}
	};

	public CrazyPlayerDataPluginCommandMainReload(final CrazyPlayerDataPluginInterface<T, ? extends T> plugin)
	{
		super(plugin);
		addReloadable(databaseReloadable, "d", "db", "database");
	}
}
