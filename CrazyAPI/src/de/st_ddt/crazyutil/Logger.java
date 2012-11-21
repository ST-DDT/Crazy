package de.st_ddt.crazyutil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyplugin.CrazyPluginInterface;

public interface Logger
{

	public final static DateFormat LOGDATETIMEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public abstract CrazyPluginInterface getPlugin();

	public abstract boolean hasLogChannel(String channel);

	public abstract Log getLogChannel(String channel);

	public abstract Set<String> getLogChannelNames();

	public abstract boolean isActiveLogChannel(String channel);

	public abstract String getPath(String channel);

	public boolean isLoggedToConsole(String channel);

	public void setLoggedToConsole(String channel, boolean logged);

	public abstract int getAllLogChannelCount();

	public abstract int getLogChannelCount();

	public abstract HashSet<? extends Log> getLoggers();

	public abstract void createEmptyLogChannels(String... channels);

	public abstract Log createLogChannel(String channel, String path, Boolean console);

	public abstract void createLogChannels(ConfigurationSection config, String... channels);

	public abstract Log createRootLogChannel(String channel, String path, Boolean console);

	public abstract boolean isRootLogChannel(String channel);

	public abstract void log(String channel, String... message);

	public abstract void save(ConfigurationSection config, String path);

	public interface Log
	{

		public void log(final String message);
	}
}
