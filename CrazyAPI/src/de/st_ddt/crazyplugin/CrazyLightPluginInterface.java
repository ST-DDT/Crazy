package de.st_ddt.crazyplugin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.bukkit.plugin.Plugin;

import de.st_ddt.crazyplugin.data.ParameterData;
import de.st_ddt.crazyutil.ChatHeaderProvider;
import de.st_ddt.crazyutil.Named;

public interface CrazyLightPluginInterface extends Named, ChatHeaderProvider, ParameterData, Plugin, Comparable<CrazyLightPluginInterface>
{

	public static DateFormat DATETIMEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static DateFormat TIMEFORMAT = new SimpleDateFormat("HH:mm:ss");

	public String getVersion();
}
