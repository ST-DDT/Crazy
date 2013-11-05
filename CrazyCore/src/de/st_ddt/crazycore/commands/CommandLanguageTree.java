package de.st_ddt.crazycore.commands;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.commands.CrazyCommandTreeExecutor;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public final class CommandLanguageTree extends CrazyCommandTreeExecutor<CrazyCore>
{

	private final CommandExecutor list = new CommandLanguageList(plugin);
	private final CommandExecutor select = new CommandLanguageSelect(plugin);

	public CommandLanguageTree(final CrazyCore plugin)
	{
		super(plugin, null);
		defaultExecutor = new CrazyCoreCommandLanguageDefault(plugin, list, select);
		addSubCommand(list, "list");
		addSubCommand(select, "select");
		addSubCommand(new CommandLanguagePrint(plugin), "print");
		addSubCommand(new CommandLanguageLink(plugin), "link");
		addSubCommand(new CommandLanguageSetDefault(plugin), "setdefault");
		addSubCommand(new CommandLanguageAddPreloaded(plugin), "addpreloaded");
		addSubCommand(new CommandLanguageRemovePreloaded(plugin), "removepreloaded");
		addSubCommand(new CommandLanguageReload(plugin), "reload");
		addSubCommand(new CommandLanguageExtract(plugin), "extract");
	}

	private class CrazyCoreCommandLanguageDefault extends CommandExecutor
	{

		private final CommandExecutor list;
		private final CommandExecutor select;

		public CrazyCoreCommandLanguageDefault(final CrazyCore plugin, final CommandExecutor list, final CommandExecutor select)
		{
			super(plugin);
			this.list = list;
			this.select = select;
		}

		@Override
		public void command(final CommandSender sender, final String[] args) throws CrazyException
		{
			if (args.length == 0)
				list.command(sender, args);
			else
				select.command(sender, args);
		}
	}
}
