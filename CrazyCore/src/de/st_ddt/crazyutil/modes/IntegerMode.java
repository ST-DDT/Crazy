package de.st_ddt.crazyutil.modes;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.modes.Mode;

public abstract class IntegerMode extends Mode<Integer>
{

	public IntegerMode(CrazyPluginInterface plugin, final String name)
	{
		super(plugin, name, Integer.class);
	}

	@Override
	public void setValue(final CommandSender sender, final String... args) throws CrazyException
	{
		if (args.length > 1)
			throw new CrazyCommandUsageException("[Number (Integer)]");
		try
		{
			setValue(Integer.parseInt(args[0]));
			showValue(sender);
		}
		catch (final NumberFormatException e)
		{
			throw new CrazyCommandParameterException(0, "Number (Integer)");
		}
	}
}
