package de.st_ddt.crazyutil.modes;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public abstract class DoubleMode extends Mode<Double>
{

	public DoubleMode(final CrazyPluginInterface plugin, final String name)
	{
		super(plugin, name, Double.class);
	}

	@Override
	public void setValue(final CommandSender sender, final String... args) throws CrazyException
	{
		if (args.length > 1)
			throw new CrazyCommandUsageException("[Number (Double)]");
		try
		{
			setValue(Double.parseDouble(args[0]));
			showValue(sender);
		}
		catch (final NumberFormatException e)
		{
			throw new CrazyCommandParameterException(0, "Number (Double)");
		}
	}
}
