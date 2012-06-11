package de.st_ddt.crazyfeather;

import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazyplugin.CrazyPlugin;

public class CrazyFeather extends CrazyPlugin
{

	private static CrazyFeather plugin;

	public static CrazyFeather getPlugin()
	{
		return plugin;
	}

	protected CrazyFeatherPlayerListener playerListener;

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
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(playerListener, this);
	}

	@Override
	protected boolean isSupportingLanguages()
	{
		return false;
	}
}
