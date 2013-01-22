package de.st_ddt.crazyutil.modes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyutil.modes.Mode;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;

public abstract class BooleanMode extends Mode<Boolean>
{

	public BooleanMode(CrazyPluginInterface plugin, final String name)
	{
		super(plugin, name, Boolean.class);
	}

	@Override
	public void showValue(final CommandSender sender)
	{
		plugin.sendLocaleMessage("MODE.CHANGE", sender, name, getValue() ? "True" : "False");
	}

	@Override
	public List<String> tab(final String... args)
	{
		if (args.length != 1)
			return new ArrayList<String>();
		else
			return BooleanParamitrisable.tabHelp(args[0]);
	}
}
