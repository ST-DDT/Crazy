package de.st_ddt.crazyutil.modes;

import java.util.List;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatConverter;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.paramitrisable.DurationParamitrisable;

public abstract class DurationMode extends LongMode
{

	public DurationMode(final CrazyPluginInterface plugin, final String name)
	{
		super(plugin, name);
	}

	@Override
	@Localized("CRAZYPLUGIN.MODE.CHANGE $Name$ $Value$")
	public void showValue(final CommandSender sender)
	{
		plugin.sendLocaleMessage("MODE.CHANGE", sender, name, ChatConverter.timeConverter(getValue() / 1000, 1, sender, 0, true));
	}

	@Override
	public void setValue(final CommandSender sender, final String... args) throws CrazyException
	{
		if (args.length > 1)
		{
			setValue(ChatConverter.stringToDuration(args));
			showValue(sender);
		}
		else
			try
			{
				setValue(Long.parseLong(args[0]));
				showValue(sender);
			}
			catch (final NumberFormatException e)
			{
				setValue(ChatConverter.stringToDuration(args));
				showValue(sender);
			}
	}

	@Override
	public List<String> tab(final String... args)
	{
		return DurationParamitrisable.tabHelp(args[args.length - 1]);
	}
}
