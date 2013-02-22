package de.st_ddt.crazyutil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyplugin.CrazyLightPluginInterface;
import de.st_ddt.crazyplugin.CrazyPluginInterface;

public class CrazyLogger implements Logger
{

	protected static final Map<String, CrazyLog> logChannelsByRootPath = new HashMap<String, CrazyLog>();
	protected final Map<String, CrazyLog> logChannelsByName = new TreeMap<String, CrazyLog>();
	protected final Map<String, Boolean> logToConsoleByName = new HashMap<String, Boolean>();
	protected final Map<String, String> logPathsByName = new HashMap<String, String>();
	protected final CrazyPluginInterface plugin;

	public CrazyLogger(final CrazyPluginInterface plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public CrazyPluginInterface getPlugin()
	{
		return plugin;
	}

	@Override
	public boolean hasLogChannel(final String channel)
	{
		return logChannelsByName.containsKey(channel);
	}

	@Override
	public CrazyLog getLogChannel(final String channel)
	{
		return logChannelsByName.get(channel);
	}

	@Override
	public Set<String> getLogChannelNames()
	{
		return logChannelsByName.keySet();
	}

	@Override
	public boolean isActiveLogChannel(final String channel)
	{
		return logPathsByName.containsKey(channel);
	}

	@Override
	public String getPath(final String channel)
	{
		return logPathsByName.get(channel);
	}

	@Override
	public boolean isLoggedToConsole(final String channel)
	{
		return Boolean.TRUE.equals(logToConsoleByName.get(channel));
	}

	@Override
	public void setLoggedToConsole(final String channel, final boolean logged)
	{
		logToConsoleByName.put(channel, logged);
	}

	@Override
	public int getAllLogChannelCount()
	{
		return logChannelsByName.size();
	}

	@Override
	public int getLogChannelCount()
	{
		return logPathsByName.size();
	}

	@Override
	public HashSet<CrazyLog> getLoggers()
	{
		final HashSet<CrazyLog> logger = new HashSet<CrazyLog>();
		logger.addAll(logChannelsByName.values());
		logger.remove(null);
		return logger;
	}

	@Override
	public void createEmptyLogChannels(final String... channels)
	{
		for (final String channel : channels)
		{
			logChannelsByName.put(channel, null);
			logPathsByName.remove(channel);
			logToConsoleByName.put(channel, false);
		}
	}

	@Override
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

	@Override
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
			if (!config.getBoolean(channel + ".path", true))
				createLogChannel(channel, null, config.getBoolean(channel + ".console", false));
			else if (config.getBoolean(channel + ".path", false))
				createLogChannel(channel, "logs/plugin.log", config.getBoolean(channel + ".console", false));
			else
				createLogChannel(channel, config.getString(channel + ".path"), config.getBoolean(channel + ".console", false));
		}
	}

	@Override
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

	@Override
	public boolean isRootLogChannel(final String channel)
	{
		return logPathsByName.get(channel).startsWith("$");
	}

	@Override
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

	@Override
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

	protected final class CrazyLog implements Log
	{

		private final FileWriter out;

		public CrazyLog(final File out, final boolean append) throws IOException
		{
			super();
			this.out = new FileWriter(out, append);
		}

		@Override
		public void log(final String message)
		{
			try
			{
				out.write(CrazyLightPluginInterface.DATETIMEFORMAT.format(new Date()) + " - " + message + '\n');
				out.flush();
			}
			catch (final IOException e)
			{}
		}
	}
}
