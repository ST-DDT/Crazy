package de.st_ddt.crazyannouncer;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.databases.Saveable;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public abstract class Announcement<T> implements Saveable, Runnable
{

	protected ArrayList<CrazyLocale> localeMessages = new ArrayList<CrazyLocale>();
	protected ArrayList<String> messages = new ArrayList<String>();
	protected ArrayList<String> commands = new ArrayList<String>();
	protected ArrayList<T> triggers = new ArrayList<T>();
	protected final String name;
	protected final CrazyAnnouncer plugin;
	protected boolean opOnly;
	protected String permissionOnly;

	public Announcement(String name, CrazyAnnouncer plugin)
	{
		super();
		this.name = name;
		this.plugin = plugin;
	}

	@Override
	public void save(ConfigurationSection config, String path)
	{
		ArrayList<String> msgs = new ArrayList<String>();
		for (CrazyLocale locale : localeMessages)
			msgs.add("#" + locale.getPath().substring(36));
		msgs.addAll(messages);
		config.set(path + "name", name);
		config.set(path + "messages", msgs);
		config.set(path + "commands", commands);
		config.set(path + "opOnly", opOnly);
		config.set(path + "permissionOnly", permissionOnly);
	}

	@Override
	public String getName()
	{
		return name;
	}

	public abstract void addTrigger(String... strings);

	public void addTrigger(T trigger)
	{
		if (!triggers.contains(trigger))
			triggers.add(trigger);
	}

	public void removeTrigger(T trigger)
	{
		triggers.remove(trigger);
	}

	public void removeTrigger(int index) throws IndexOutOfBoundsException
	{
		triggers.remove(index);
	}

	public abstract List<String> listTriggerNames();

	public List<T> listTriggers()
	{
		List<T> list = new ArrayList<T>();
		list.addAll(triggers);
		return list;
	}

	public void addMessage(String message) throws CrazyCommandException
	{
		if (message == null)
			return;
		message = message.trim();
		if (message.equals(""))
			return;
		if (message.startsWith("#"))
		{
			CrazyLocale locale = StringToLocale(message.substring(1));
			if (!CrazyLocale.isValid(locale))
				throw new CrazyCommandNoSuchException("LanguageEntry", message);
			localeMessages.add(locale);
			return;
		}
		messages.add(message);
	}

	private CrazyLocale StringToLocale(String localePath)
	{
		return plugin.getLocale().getLanguageEntry("Announcements." + localePath);
	}

	public void removeMessage(int index) throws IndexOutOfBoundsException
	{
		if (index < 0)
			return;
		int size = messages.size();
		if (index < size)
			messages.remove(index);
		else
			localeMessages.remove(index - size);
	}

	public List<String> listMessages(CommandSender sender)
	{
		List<String> list = new ArrayList<String>();
		list.addAll(messages);
		for (CrazyLocale locale : localeMessages)
			list.add(locale.getLanguageText(sender));
		return list;
	}

	public void addCommand(String command)
	{
		commands.add(command);
	}

	public void removeCommand(int index) throws IndexOutOfBoundsException
	{
		commands.remove(index);
	}

	public List<String> listCommands()
	{
		List<String> list = new ArrayList<String>();
		list.addAll(commands);
		return list;
	}

	@Override
	public void run()
	{
		for (Player player : Bukkit.getOnlinePlayers())
			if (player.isOp() || !opOnly)
				if (player.hasPermission(permissionOnly) || permissionOnly == null)
				{
					for (String message : messages)
						player.sendMessage(ChatHelper.colorise(message));
					for (CrazyLocale locale : localeMessages)
						locale.sendMessage(player);
				}
		for (String command : commands)
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
	}
}
