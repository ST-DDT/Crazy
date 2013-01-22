package de.st_ddt.crazyutil.modes;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public abstract class BooleanFalseMode extends BooleanMode
{

	public BooleanFalseMode(CrazyPluginInterface plugin, final String name)
	{
		super(plugin, name);
	}

	@Override
	public void setValue(final CommandSender sender, final String... args) throws CrazyException
	{
		if (args.length > 1)
			throw new CrazyCommandUsageException("[Boolean (true/false)]");
		try
		{
			final String value = args[0].toLowerCase();
			if (value.equals("true"))
				setValue(true);
			else if (value.equals("1"))
				setValue(true);
			else if (value.equals("y"))
				setValue(true);
			else if (value.equals("yes"))
				setValue(true);
			else if (value.equals("on"))
				setValue(true);
			else
				setValue(false);
			showValue(sender);
		}
		catch (final NumberFormatException e)
		{
			throw new CrazyCommandParameterException(0, "Boolean (true/false)");
		}
	}
}
