package de.st_ddt.crazyfeather;

import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazyplugin.CrazyPlugin;

public class CrazyFeather extends CrazyPlugin
{

	private static CrazyFeather plugin;
	private CrazyFeatherPlayerListener playerListener;

	public static CrazyFeather getPlugin()
	{
		return plugin;
	}

	@Override
	public void onEnable()
	{
		plugin = this;
		registerHooks();
		super.onEnable();
	}

	public void registerHooks()
	{
		this.playerListener = new CrazyFeatherPlayerListener(this);
		final PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(playerListener, this);
	}

	@Override
	protected boolean isSupportingLanguages()
	{
		return false;
	}
}
