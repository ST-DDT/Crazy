package de.st_ddt.crazyutil.modes;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public abstract class BooleanTrueMode extends BooleanMode
{

	public BooleanTrueMode(CrazyPluginInterface plugin, final String name)
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
			if (value.equals("false"))
				setValue(false);
			else if (value.equals("0"))
				setValue(false);
			else if (value.equals("n"))
				setValue(false);
			else if (value.equals("no"))
				setValue(false);
			else if (value.equals("off"))
				setValue(false);
			else
				setValue(true);
			showValue(sender);
		}
		catch (final NumberFormatException e)
		{
			throw new CrazyCommandParameterException(0, "Boolean (true/false)");
		}
	}
}
