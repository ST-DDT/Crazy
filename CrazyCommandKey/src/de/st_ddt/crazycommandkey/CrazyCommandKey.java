package de.st_ddt.crazycommandkey;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyutil.ChatHelper;

public class CrazyCommandKey extends CrazyPlugin
{

	private static CrazyCommandKey plugin;
	protected final HashMap<String, String> keys = new HashMap<String, String>();

	public static CrazyCommandKey getPlugin()
	{
		return plugin;
	}

	@Override
	protected String getShortPluginName()
	{
		return "cckey";
	}

	@Override
	protected boolean isSupportingLanguages()
	{
		return false;
	}

	@Override
	public void onEnable()
	{
		plugin = this;
		super.onEnable();
	}

	@Override
	public void load()
	{
		super.load();
		ConfigurationSection config = getConfig();
		keys.clear();
		ConfigurationSection keySection = config.getConfigurationSection("keys");
		if (keySection != null)
			for (String key : keySection.getKeys(false))
				keys.put(key, keySection.getString(key));
	}

	@Override
	public void save()
	{
		ConfigurationSection config = getConfig();
		config.set("keys", null);
		for (Entry<String, String> entry : keys.entrySet())
			config.set("keys." + entry.getKey(), entry.getValue());
		super.save();
	}

	@Override
	public boolean command(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyCommandException
	{
		if (commandLabel.equalsIgnoreCase("key"))
		{
			commandKeyUse(sender, args);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("gkey") || commandLabel.equalsIgnoreCase("genkey"))
		{
			commandKeyGen(sender, args);
			return true;
		}
		return false;
	}

	private void commandKeyGen(CommandSender sender, String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazycommandkey.keygen"))
			throw new CrazyCommandPermissionException();
		if (args.length < 1)
			throw new CrazyCommandUsageException("/genkey <Command>");
		String key = String.valueOf(Math.round(Math.random() * Long.MAX_VALUE));
		while (!getConfig().getString("keys." + key, "").equals(""))
			key = String.valueOf(Math.round(Math.random() * Long.MAX_VALUE));
		key = key.substring(0, 6);
		sender.sendMessage("Key created: " + key);
		String command = ChatHelper.listingString(" ", args);
		keys.put(key, command);
		save();
	}

	private void commandKeyUse(CommandSender sender, String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazycommandkey.keyuse"))
			throw new CrazyCommandPermissionException();
		if (args.length != 1)
			throw new CrazyCommandUsageException("/key <Key>");
		String command = keys.remove(args[0]);
		if (command == null)
		{
			sender.sendMessage("Key not found");
			return;
		}
		sender.sendMessage("Done");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
		save();
	}
}
