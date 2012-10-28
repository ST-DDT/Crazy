package de.st_ddt.crazyplugin.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public abstract class CrazyCommandExecutor<S extends CrazyPluginInterface> implements CrazyCommandExecutorInterface
{

	protected final S plugin;

	public CrazyCommandExecutor(final S plugin)
	{
		super();
		this.plugin = plugin;
	}

	public final S getPlugin()
	{
		return plugin;
	}

	@Override
	public final boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args)
	{
		try
		{
			if (!hasAccessPermission(sender))
				throw new CrazyCommandPermissionException();
			command(sender, args);
		}
		catch (final CrazyCommandException e)
		{
			e.addCommandPrefix(commandLabel);
			e.setCommand(commandLabel, args);
			e.print(sender, plugin.getChatHeader());
		}
		catch (final CrazyException e)
		{
			e.print(sender, plugin.getChatHeader());
		}
		return true;
	}

	@Override
	public abstract void command(final CommandSender sender, final String[] args) throws CrazyException;

	@Override
	public final List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args)
	{
		if (hasAccessPermission(sender))
			return tab(sender, args);
		else
			return null;
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		return null;
	}

	@Override
	public boolean hasAccessPermission(CommandSender sender)
	{
		return true;
	}
}
