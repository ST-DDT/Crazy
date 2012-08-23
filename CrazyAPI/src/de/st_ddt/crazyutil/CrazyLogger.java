package de.st_ddt.crazyutil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyplugin.CrazyPluginInterface;

public class CrazyLogger
{

	protected static final HashMap<String, CrazyLog> logChannelsByRootPath = new HashMap<String, CrazyLog>();
	protected final HashMap<String, CrazyLog> logChannelsByName = new HashMap<String, CrazyLog>();
	protected final HashMap<String, Boolean> logToConsoleByName = new HashMap<String, Boolean>();
	protected final HashMap<String, String> logPathsByName = new HashMap<String, String>();
	protected final CrazyPluginInterface plugin;

	public CrazyLogger(final CrazyPluginInterface plugin)
	{
		this.plugin = plugin;
	}

	public CrazyPluginInterface getPlugin()
	{
		return plugin;
	}

	public boolean hasLogChannel(final String channel)
	{
		return logChannelsByName.containsKey(channel);
	}

	public CrazyLog getLogChannel(final String channel)
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

	public HashSet<CrazyLog> getLoggers()
	{
		final HashSet<CrazyLog> logger = new HashSet<CrazyLog>();
		logger.addAll(logChannelsByName.values());
		logger.remove(null);
		return logger;
	}

	public void createEmptyLogChannels(final String... channels)
	{
		for (final String channel : channels)
		{
			logChannelsByName.put(channel, null);
			logPathsByName.remove(channel);
		}
	}

	public CrazyLog createLogChannel(final String channel, final ConfigurationSection config, final String defaulPath, final boolean defaultConsole)
	{
		logChannelsByName.put(channel, null);
		if (!config.getBoolean("path", true))
		{
			logToConsoleByName.put(channel, config.getBoolean(channel + ".console", false));
			return null;
		}
		else if (config.getBoolean("path", false))
			return createLogChannel(channel, "logs/plugin.log", config.getBoolean("console", false));
		else
			return createLogChannel(channel, config.getString("path", null), config.getBoolean("console", false));
	}

	public CrazyLog createLogChannel(final String channel, final String path, final Boolean console)
	{
		if (console != null)
			logToConsoleByName.put(channel, console);
		if (path == null)
			return logChannelsByName.get(channel);
		if (path.startsWith("$"))
			return createRootLogChannel(channel, path.substring(1), null);
		final String realPath = plugin.getDataFolder().getPath() + File.separator + path;
		CrazyLog log = logChannelsByRootPath.get(realPath);
		if (log == null)
		{
			try
			{
				final File file = new File(realPath);
				if (file.getParentFile() != null)
					file.getParentFile().mkdirs();
				log = new CrazyLog(file, true);
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
			logChannelsByRootPath.put(realPath, log);
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
			if (!config.getBoolean(channel + ".path", config.getBoolean(channel, true)))
				logToConsoleByName.put(channel, config.getBoolean(channel + ".console", false));
			else if (config.getBoolean(channel + ".path", config.getBoolean(channel, false)))
				createLogChannel(channel, "logs/plugin.log", config.getBoolean(channel + ".console", false));
			else
				createLogChannel(channel, config.getString(channel + ".path", config.getString(channel, null)), config.getBoolean(channel + ".console", false));
		}
	}

	public CrazyLog createRootLogChannel(final String channel, final String path, final Boolean console)
	{
		if (console != null)
			logToConsoleByName.put(channel, console);
		if (path == null)
			return logChannelsByName.get(channel);
		CrazyLog log = logChannelsByRootPath.get(path);
		if (log == null)
		{
			try
			{
				final File file = new File(path);
				if (file.getParentFile() != null)
					file.getParentFile().mkdirs();
				log = new CrazyLog(file, true);
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
			logChannelsByRootPath.put(path, log);
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
		if (logToConsoleByName.get(channel))
			plugin.getLogger().log(Level.INFO, "[" + plugin.getName() + "." + channel + "] " + ChatHelper.listingString("\n\t", message));
		final CrazyLog log = getLogChannel(channel);
		if (log == null)
			return;
		if (isRootLogChannel(channel))
			log.log("[" + plugin.getName() + "." + channel + "] " + ChatHelper.listingString("\n\t", message));
		else
			log.log("[" + channel + "] " + ChatHelper.listingString("\n\t", message));
	}

	public void save(final ConfigurationSection config, final String path)
	{
		for (final Entry<String, CrazyLog> entry : logChannelsByName.entrySet())
		{
			if (entry.getValue() == null)
				config.set(path + entry.getKey() + ".path", false);
			else
				config.set(path + entry.getKey() + ".path", logPathsByName.get(entry.getKey()));
			config.set(path + entry.getKey() + ".console", logToConsoleByName.get(entry.getKey()));
		}
	}

	protected final class CrazyLog
	{

		private final FileWriter out;

		public CrazyLog(final File out, final boolean append) throws IOException
		{
			super();
			this.out = new FileWriter(out, append);
		}

		public void log(final String message)
		{
			try
			{
				out.write(CrazyPluginInterface.DateFormat.format(new Date()) + " - " + message + '\n');
				out.flush();
			}
			catch (final IOException e)
			{}
		}
	}
}
