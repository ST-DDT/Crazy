package de.st_ddt.crazyplugin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.bukkit.plugin.Plugin;

import de.st_ddt.crazyplugin.data.ParameterData;
import de.st_ddt.crazyutil.ChatHeaderProvider;
import de.st_ddt.crazyutil.Named;

/**
 * This object represents a lightweighted CrazyPlugin.
 */
public interface CrazyLightPluginInterface extends Named, ChatHeaderProvider, ParameterData, Plugin, Comparable<CrazyLightPluginInterface>
{

	/**
	 * Used to format Dates to a readable String <b>yyyy-MM-dd HH:mm:ss</b>
	 */
	public static DateFormat DATETIMEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * Used to format Dates to a readable String <b>yyyy-MM-dd</b>
	 */
	public static DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * Used to format Dates to a readable String <b>HH:mm:ss</b>
	 */
	public static DateFormat TIMEFORMAT = new SimpleDateFormat("HH:mm:ss");

	/**
	 * @return The current version number of this plugin.
	 */
	public String getVersion();
}
