package de.st_ddt.crazychats.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.data.ChatPlayerData;
import de.st_ddt.crazyplugin.CrazyLightPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.PlayerDataParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.StringParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.TargetDateParamitrisable;

public class CrazyChatsCommandPlayerSilence extends CrazyChatsCommandExecutor
{

	public CrazyChatsCommandPlayerSilence(final CrazyChats plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYCHATS.COMMAND.PLAYER.SILENCED.DONE $Player$ $UntilDateTime$", "CRAZYCHATS.COMMAND.PLAYER.SILENCED.MESSAGE $Muter$ $UntilDateTime$", "CRAZYCHATS.COMMAND.PLAYER.SILENCED.MESSAGE2 $Muter$ $UntilDateTime$ $Reason$" })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		final Map<String, TabbedParamitrisable> params = new TreeMap<String, TabbedParamitrisable>();
		final PlayerDataParamitrisable<ChatPlayerData> playerData = new PlayerDataParamitrisable<ChatPlayerData>(plugin);
		params.put("p", playerData);
		params.put("player", playerData);
		final TargetDateParamitrisable until = new TargetDateParamitrisable(60000);
		params.put("until", until);
		params.put("duration", until);
		final BooleanParamitrisable quiet = new BooleanParamitrisable(false);
		params.put("q", quiet);
		params.put("quiet", quiet);
		final StringParamitrisable reason = new StringParamitrisable(null);
		params.put("r", reason);
		params.put("reason", reason);
		ChatHelperExtended.readParameters(args, params, playerData, until, quiet, reason);
		final ChatPlayerData data = playerData.getValue();
		if (data == null)
			throw new CrazyCommandUsageException("<player:Player> [until:Date/Duration] [quiet:Boolean] [reason:String]");
		data.setSilenced(until.getValue());
		plugin.sendLocaleMessage("COMMAND.PLAYER.SILENCED.DONE", sender, data.getName(), CrazyLightPluginInterface.DATETIMEFORMAT.format(until.getValue()));
		if (!quiet.getValue())
		{
			final Player player = data.getPlayer();
			if (player != null)
				if (player.isOnline())
					if (reason.getValue() == null)
						plugin.sendLocaleMessage("COMMAND.PLAYER.SILENCED.MESSAGE", player, sender.getName(), CrazyLightPluginInterface.DATETIMEFORMAT.format(until.getValue()));
					else
						plugin.sendLocaleMessage("COMMAND.PLAYER.SILENCED.MESSAGE2", player, sender.getName(), CrazyLightPluginInterface.DATETIMEFORMAT.format(until.getValue()), reason.getValue());
		}
		plugin.getCrazyDatabase().save(data);
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		final Map<String, TabbedParamitrisable> params = new HashMap<String, TabbedParamitrisable>();
		final PlayerDataParamitrisable<ChatPlayerData> playerData = new PlayerDataParamitrisable<ChatPlayerData>(plugin);
		params.put("p", playerData);
		params.put("player", playerData);
		final TargetDateParamitrisable until = new TargetDateParamitrisable(60000);
		params.put("until", until);
		params.put("duration", until);
		final BooleanParamitrisable quiet = new BooleanParamitrisable(false);
		params.put("q", quiet);
		params.put("quiet", quiet);
		final StringParamitrisable reason = new StringParamitrisable(null);
		params.put("r", reason);
		params.put("reason", reason);
		return ChatHelperExtended.tabHelp(args, params, playerData, until, quiet, reason);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazychats.player.silence");
	}
}
