package de.st_ddt.crazyplugin;

import java.io.File;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import de.st_ddt.crazyplugin.data.ParameterData;
import de.st_ddt.crazyutil.Named;

public interface CrazyLightPluginInterface extends Named, ParameterData, Plugin
{

	public String getChatHeader();

	public PluginDescriptionFile getDescription();

	public File getDataFolder();

	public String getVersion();
}
