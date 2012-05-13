package de.st_ddt.crazyplugin;

import org.bukkit.command.CommandSender;

public class LanguageLoadRunnable implements Runnable
{

	protected final CrazyPlugin plugin;
	protected final String language;
	protected final CommandSender sender;

	public LanguageLoadRunnable(CrazyPlugin plugin, String language, CommandSender sender)
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
