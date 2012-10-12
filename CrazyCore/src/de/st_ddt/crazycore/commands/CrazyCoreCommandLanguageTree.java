package de.st_ddt.crazycore.commands;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.commands.CrazyCommandTreeExecutor;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;

public final class CrazyCoreCommandLanguageTree extends CrazyCommandTreeExecutor<CrazyCore>
{

	private final CrazyCoreCommandExecutor list = new CrazyCoreCommandLanguageList(plugin);
	private final CrazyCoreCommandExecutor select = new CrazyCoreCommandLanguageSelect(plugin);

	public CrazyCoreCommandLanguageTree(final CrazyCore plugin)
	{
		super(plugin, null);
		defaultExecutor = new CrazyCoreCommandLanguageDefault(plugin, list, select);
		addSubCommand(list, "list");
		addSubCommand(select, "select");
		addSubCommand(new CrazyCoreCommandLanguagePrint(plugin), "print");
		addSubCommand(new CrazyCoreCommandLanguageLink(plugin), "link");
		addSubCommand(new CrazyCoreCommandLanguageSetDefault(plugin), "setdefault");
		addSubCommand(new CrazyCoreCommandLanguageAddPreloaded(plugin), "addpreloaded");
		addSubCommand(new CrazyCoreCommandLanguageRemovePreloaded(plugin), "removepreloaded");
		addSubCommand(new CrazyCoreCommandLanguageReload(plugin), "reload");
		addSubCommand(new CrazyCoreCommandLanguageDownload(plugin), "download");
		addSubCommand(new CrazyCoreCommandLanguageExtract(plugin), "extract");
	}

	private class CrazyCoreCommandLanguageDefault extends CrazyCoreCommandExecutor
	{

		private final CrazyCoreCommandExecutor list;
		private final CrazyCoreCommandExecutor select;

		public CrazyCoreCommandLanguageDefault(final CrazyCore plugin, final CrazyCoreCommandExecutor list, final CrazyCoreCommandExecutor select)
		{
			super(plugin);
			this.list = list;
			this.select = select;
		}

		@Override
		public void command(final CommandSender sender, final String[] args) throws CrazyException
		{
			System.out.println(ChatHelper.listingString(" ", args));
			if (args.length == 0)
				list.command(sender, args);
			else
				select.command(sender, args);
		}
	}
}
