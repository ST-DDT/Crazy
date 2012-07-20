package de.st_ddt.crazycommandkey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
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
	public void onEnable()
	{
		plugin = this;
		super.onEnable();
	}

	@Override
	public void load()
	{
		super.load();
		final ConfigurationSection config = getConfig();
		logger.createLogChannels(config.getConfigurationSection("logs"), "KeyUse", "KeyGen");
		keys.clear();
		final ConfigurationSection keySection = config.getConfigurationSection("keys");
		if (keySection != null)
			for (final String key : keySection.getKeys(false))
				keys.put(key, keySection.getString(key));
	}

	@Override
	public void save()
	{
		final ConfigurationSection config = getConfig();
		logger.save(config, "logs.");
		config.set("keys", null);
		for (final Entry<String, String> entry : keys.entrySet())
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

	private void commandKeyGen(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazycommandkey.keygen"))
			throw new CrazyCommandPermissionException();
		if (args.length < 1)
			throw new CrazyCommandUsageException("/genkey <Command>");
		int amount = 1;
		int startIndex = 0;
		if (args[0].toLowerCase().startsWith("amount:"))
		{
			startIndex++;
			try
			{
				amount = Integer.parseInt(args[0].substring(7));
			}
			catch (final NumberFormatException e)
			{
				throw new CrazyCommandParameterException(0, "Integer");
			}
		}
		final ArrayList<String> genKeys = new ArrayList<String>();
		final String command = ChatHelper.listingString(" ", ChatHelper.shiftArray(args, startIndex));
		for (int i = 0; i < amount; i++)
		{
			String key = Long.toHexString(Math.round(Math.random() * Long.MAX_VALUE)) + Long.toHexString(Math.round(Math.random() * Long.MAX_VALUE));
			while (!getConfig().getString("keys." + key, "").equals(""))
				key = Long.toHexString(Math.round(Math.random() * Long.MAX_VALUE)) + Long.toHexString(Math.round(Math.random() * Long.MAX_VALUE));
			key = key.substring(0, 13);
			keys.put(key, command);
			genKeys.add(key);
		}
		sendLocaleMessage("COMMAND.KEYGEN", sender, ChatHelper.listingString(genKeys));
		logger.log("KeyGen", sender.getName() + " created " + amount + " keys for Command: " + command, "Keys: " + ChatHelper.listingString(genKeys));
		save();
	}

	private void commandKeyUse(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazycommandkey.keyuse"))
			throw new CrazyCommandPermissionException();
		if (args.length != 1)
			throw new CrazyCommandUsageException("/key <Key>");
		String command = keys.remove(args[0]);
		if (command == null)
			throw new CrazyCommandNoSuchException("Key", args[0]);
		command = ChatHelper.putArgs(command, sender.getName());
		sendLocaleMessage("COMMAND.KEYUSE", sender, args[0]);
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
		logger.log("KeyUse", sender.getName() + " used key: " + args[0], "Command: " + command);
		save();
	}
}
