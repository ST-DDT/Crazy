package de.st_ddt.crazycommandkey;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazycommandkey.commands.CrazyCommandKeyCommandGenKey;
import de.st_ddt.crazycommandkey.commands.CrazyCommandKeyCommandUseKey;
import de.st_ddt.crazyplugin.CrazyPlugin;

public class CrazyCommandKey extends CrazyPlugin
{

	private static CrazyCommandKey plugin;
	private final HashMap<String, String> keys = new HashMap<String, String>();

	public static CrazyCommandKey getPlugin()
	{
		return plugin;
	}

	private void registerCommands()
	{
		getCommand("genkey").setExecutor(new CrazyCommandKeyCommandGenKey(this));
		getCommand("key").setExecutor(new CrazyCommandKeyCommandUseKey(this));
	}

	@Override
	public void onEnable()
	{
		plugin = this;
		super.onEnable();
		registerCommands();
	}

	@Override
	public void loadConfiguration()
	{
		final ConfigurationSection config = getConfig();
		logger.createLogChannels(config.getConfigurationSection("logs"), "KeyUse", "KeyGen");
		keys.clear();
		final ConfigurationSection keySection = config.getConfigurationSection("keys");
		if (keySection != null)
			for (final String key : keySection.getKeys(false))
				keys.put(key, keySection.getString(key));
	}

	@Override
	public void saveConfiguration()
	{
		final ConfigurationSection config = getConfig();
		logger.save(config, "logs.");
		config.set("keys", null);
		for (final Entry<String, String> entry : keys.entrySet())
			config.set("keys." + entry.getKey(), entry.getValue());
		super.saveConfiguration();
	}

	public Map<String, String> getKeys()
	{
		return keys;
	}
}
