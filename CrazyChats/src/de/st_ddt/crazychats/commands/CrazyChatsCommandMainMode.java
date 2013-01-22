package de.st_ddt.crazychats.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.commands.CrazyPluginCommandMainMode;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.CrazyChatsChatHelper;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazyChatsCommandMainMode extends CrazyPluginCommandMainMode
{

	public CrazyChatsCommandMainMode(final CrazyChats plugin)
	{
		super(plugin);
	}

	public CrazyChats getPlugin()
	{
		return (CrazyChats) plugin;
	}

	public abstract class ChatFormatMode extends Mode<String>
	{

		protected final CrazyPluginInterface plugin;

		public ChatFormatMode(final String name)
		{
			super(name, String.class);
			this.plugin = getPlugin();
		}

		public ChatFormatMode(final CrazyPluginInterface plugin, final String name)
		{
			super(name, String.class);
			this.plugin = plugin;
		}

		@Override
		@Localized({ "CRAZYCHATS.FORMAT.CHANGE $FormatName$ $Value$", "CRAZYCHATS.FORMAT.EXAMPLE $Example$" })
		public void showValue(final CommandSender sender)
		{
			final String raw = getRawValue();
			plugin.sendLocaleMessage("FORMAT.CHANGE", sender, name, raw);
			plugin.sendLocaleMessage("FORMAT.EXAMPLE", sender, CrazyChatsChatHelper.exampleFormat(ChatHelper.colorise(raw)));
		}

		public String getRawValue()
		{
			return CrazyChatsChatHelper.unmakeFormat(getValue());
		}

		@Override
		public void setValue(final CommandSender sender, final String... args) throws CrazyException
		{
			setValue(CrazyChatsChatHelper.makeFormat(ChatHelper.listingString(" ", args)));
			showValue(sender);
		}

		@Override
		public List<String> tab(final String... args)
		{
			if (args.length != 1 && args[0].length() != 0)
				return null;
			final List<String> res = new ArrayList<String>(1);
			res.add(getRawValue());
			return res;
		}
	}
}
