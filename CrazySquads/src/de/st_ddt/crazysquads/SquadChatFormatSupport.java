package de.st_ddt.crazysquads;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazyplugin.commands.CrazyPluginCommandMainMode;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.modes.ChatFormatMode;
import de.st_ddt.crazyutil.modes.Mode;

class SquadChatFormatSupport
{

	public static void registerChatFormats(final CrazySquads squadPlugin, final CrazyPluginCommandMainMode modeCommand)
	{
		final Mode<String> squadChatFormatMode = new ChatFormatMode(squadPlugin, "squadChatFormat")
		{

			@Override
			public String getValue()
			{
				return squadPlugin.getSquadChatFormat();
			}

			@Override
			public void setValue(final String newValue) throws CrazyException
			{
				squadPlugin.setSquadChatFormat(newValue);
				squadPlugin.saveConfiguration();
			}
		};
		final Mode<String> squadLeaderChatFormatMode = new ChatFormatMode(squadPlugin, "squadLeaderChatFormat")
		{

			@Override
			public String getValue()
			{
				return squadPlugin.getSquadLeaderChatFormat();
			}

			@Override
			public void setValue(final String newValue) throws CrazyException
			{
				squadPlugin.setSquadHeadNamePrefix(newValue);
				squadPlugin.saveConfiguration();
			}
		};
		modeCommand.addMode(squadChatFormatMode);
		modeCommand.addMode(squadLeaderChatFormatMode);
		final CrazyPluginCommandMainMode chatsModeCommand = CrazyChats.getPlugin().getModeCommand();
		chatsModeCommand.addMode(squadChatFormatMode);
		chatsModeCommand.addMode(squadLeaderChatFormatMode);
	}
}
