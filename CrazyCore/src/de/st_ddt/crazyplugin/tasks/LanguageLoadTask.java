package de.st_ddt.crazyplugin.tasks;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPlugin;

public class LanguageLoadTask implements Runnable
{

	protected final CrazyPlugin plugin;
	protected final String language;
	protected final CommandSender sender;

	public LanguageLoadTask(final CrazyPlugin plugin, final String language, final CommandSender sender)
	{
		super();
		this.plugin = plugin;
		this.language = language;
		this.sender = sender;
	}

	@Override
	public void run()
	{
		plugin.loadLanguage(language, sender);
	}
}
