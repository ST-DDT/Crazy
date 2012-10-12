package de.st_ddt.crazyloginfilter.commands;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyloginfilter.CrazyLoginFilter;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;

public class CrazyLoginFilterCommandServerFilter extends CrazyLoginFilterCommandExecutor
{

	private final CrazyLoginFilterCommandShow showCommand;
	private final CrazyLoginFilterCommandIP ipCommand;
	private final CrazyLoginFilterCommandConnection connectionCommand;

	public CrazyLoginFilterCommandServerFilter(final CrazyLoginFilter plugin, final CrazyLoginFilterCommandShow showCommand, final CrazyLoginFilterCommandIP ipCommand, final CrazyLoginFilterCommandConnection connectionCommand)
	{
		super(plugin);
		this.showCommand = showCommand;
		this.ipCommand = ipCommand;
		this.connectionCommand = connectionCommand;
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length == 0)
			throw new CrazyCommandUsageException("show [Page]", "ip ...", "connection ...");
		final String[] newArgs = ChatHelperExtended.shiftArray(args, 1);
		final String commandLabel = args[0];
		if (commandLabel.equals("show") || commandLabel.equals("list"))
			showCommand.show(sender, newArgs, plugin.getServerAccessFilter());
		else if (commandLabel.equals("ip") || commandLabel.equals("ips"))
			ipCommand.ip(sender, newArgs, plugin.getServerAccessFilter());
		else if (commandLabel.equals("connection") || commandLabel.equals("connections"))
			connectionCommand.connection(sender, newArgs, plugin.getServerAccessFilter());
		else
			throw new CrazyCommandUsageException("show", "ip", "connection");
	}
}
