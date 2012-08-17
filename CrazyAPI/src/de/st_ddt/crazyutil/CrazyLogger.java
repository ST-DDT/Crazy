package de.st_ddt.crazyutil;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyplugin.CrazyPluginInterface;

public class CrazyLogger
{

	protected static final Formatter formater = new CrazyLogFormat();
	protected final HashMap<String, Logger> logChannelsByName = new HashMap<String, Logger>();
	protected final HashMap<String, String> logPathsByName = new HashMap<String, String>();
	protected final HashMap<String, Logger> logChannelsByPath = new HashMap<String, Logger>();
	protected final CrazyPluginInterface plugin;
	protected Level level;

	public CrazyLogger(final CrazyPluginInterface plugin)
	{
		this.plugin = plugin;
		this.level = Level.FINE;
	}

	public CrazyLogger(final CrazyPluginInterface plugin, final Level level)
	{
		this.plugin = plugin;
		this.level = level;
	}

	public CrazyPluginInterface getPlugin()
	{
		return plugin;
	}

	public Level getLevel()
	{
		return level;
	}

	public void setLevel(final Level level)
	{
		this.level = level;
	}

	public boolean hasLogChannel(final String channel)
	{
		return logChannelsByName.containsKey(channel);
	}

	public Logger getLogChannel(final String channel)
	{
		return logChannelsByName.get(channel);
	}

	public Set<String> getLogChannelNames()
	{
		return logChannelsByName.keySet();
	}

	public boolean isActiveLogChannel(final String channel)
	{
		return logPathsByName.containsKey(channel);
	}

	public String getPath(final String channel)
	{
		return logPathsByName.get(channel);
	}

	public int getAllLogChannelCount()
	{
		return logChannelsByName.size();
	}

	public int getLogChannelCount()
	{
		return logPathsByName.size();
	}

	public Collection<Logger> getLoggers()
	{
		return logChannelsByPath.values();
	}

	public void createEmptyLogChannels(final String... channels)
	{
		for (final String channel : channels)
		{
			logChannelsByName.put(channel, null);
			logPathsByName.remove(channel);
		}
	}

	public Logger createLogChannel(final String channel, final ConfigurationSection config, final String defaulPath)
	{
		if (config == null)
			return createLogChannel(channel, defaulPath);
		if (config.getBoolean(channel, false))
			return createLogChannel(channel, defaulPath);
		if (!config.getBoolean(channel, true))
			return null;
		return createLogChannel(channel, config.getString(channel, defaulPath));
	}

	public Logger createLogChannel(final String channel, final String path)
	{
		if (path == null)
			return logChannelsByName.get(channel);
		if (path.startsWith("$"))
			return createRootLogChannel(channel, path.substring(1));
		Logger log = logChannelsByPath.get(path);
		if (log == null)
		{
			try
			{
				log = Logger.getLogger(plugin.getName() + "." + channel);
				log.setLevel(level);
				final File file = new File(plugin.getDataFolder().getPath() + "/" + path);
				if (file.getParentFile() != null)
					file.getParentFile().mkdirs();
				final FileHandler fileHandler = new FileHandler(file.getPath(), true);
				fileHandler.setFormatter(formater);
				log.addHandler(fileHandler);
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
			logChannelsByPath.put(path, log);
		}
		logChannelsByName.put(channel, log);
		logPathsByName.put(channel, path);
		return log;
	}

	public void createLogChannels(final ConfigurationSection config, final String... channels)
	{
		if (config == null)
		{
			createEmptyLogChannels(channels);
			return;
		}
		for (final String channel : channels)
		{
			logChannelsByName.put(channel, null);
			if (!config.getBoolean(channel, true))
				logChannelsByName.put(channel, null);
			else if (config.getBoolean(channel, false))
				createLogChannel(channel, "logs/plugin.log");
			else
				createLogChannel(channel, config.getString(channel, null));
		}
	}

	public Logger createRootLogChannel(final String channel, final String path)
	{
		if (path == null)
			return logChannelsByName.get(channel);
		Logger log = logChannelsByPath.get(path);
		if (log == null)
		{
			try
			{
				log = Logger.getLogger(channel);
				log.setLevel(level);
				final File file = new File(path);
				if (file.getParentFile() != null)
					file.getParentFile().mkdirs();
				final FileHandler fileHandler = new FileHandler(file.getPath(), true);
				fileHandler.setFormatter(formater);
				log.addHandler(fileHandler);
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
			logChannelsByPath.put(path, log);
		}
		logChannelsByName.put(channel, log);
		logPathsByName.put(channel, "$" + path);
		return log;
	}

	public boolean isRootLogChannel(final String channel)
	{
		return logPathsByName.get(channel).startsWith("$");
	}

	public void log(final String channel, final String... message)
	{
		log(channel, level, message);
	}

	public void log(final String channel, final Level level, final String... message)
	{
		final Logger log = getLogChannel(channel);
		if (log == null)
			return;
		if (isRootLogChannel(channel))
			log.log(level, "[" + plugin.getName() + "." + channel + "] " + ChatHelper.listingString("\n\t", message));
		else
			log.log(level, "[" + channel + "] " + ChatHelper.listingString("\n\t", message));
	}

	public void save(final ConfigurationSection config, final String path)
	{
		for (final Entry<String, Logger> entry : logChannelsByName.entrySet())
			if (entry.getValue() == null)
				config.set(path + entry.getKey(), false);
			else
				config.set(path + entry.getKey(), logPathsByName.get(entry.getKey()));
	}

	protected static class CrazyLogFormat extends Formatter
	{

		@Override
		public String format(final LogRecord record)
		{
			return CrazyPluginInterface.DateFormat.format(new Date(record.getMillis())) + " - " + record.getMessage() + '\n';
		}
	}
}
