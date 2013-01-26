package de.st_ddt.crazyutil.modes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.Named;
import de.st_ddt.crazyutil.locales.Localized;

public abstract class Mode<S> implements Named
{

	protected final CrazyPluginInterface plugin;
	protected final String name;
	protected final Class<S> clazz;

	public Mode(final CrazyPluginInterface plugin, final String name, final Class<S> clazz)
	{
		super();
		this.plugin = plugin;
		this.name = name;
		this.clazz = clazz;
	}

	@Override
	public final String getName()
	{
		return name;
	}

	public final Class<S> getClazz()
	{
		return clazz;
	}

	@Localized("CRAZYPLUGIN.MODE.CHANGE $Name$ $Value$")
	public void showValue(final CommandSender sender)
	{
		plugin.sendLocaleMessage("MODE.CHANGE", sender, name, getValue());
	}

	public abstract S getValue();

	public abstract void setValue(CommandSender sender, String... args) throws CrazyException;

	public abstract void setValue(S newValue) throws CrazyException;

	public List<String> tab(final String... args)
	{
		return new ArrayList<String>();
	}
}
