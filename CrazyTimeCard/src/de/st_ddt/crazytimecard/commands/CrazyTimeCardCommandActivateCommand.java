package de.st_ddt.crazytimecard.commands;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyLightPluginInterface;
import de.st_ddt.crazyplugin.commands.CrazyCommandExecutor;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazytimecard.CrazyTimeCard;
import de.st_ddt.crazytimecard.data.CardData;
import de.st_ddt.crazytimecard.data.PlayerTimeData;
import de.st_ddt.crazytimecard.exceptions.CrazyTimeCardCardAlreadyUsedException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazyTimeCardCommandActivateCommand extends CrazyCommandExecutor<CrazyTimeCard>
{

	public CrazyTimeCardCommandActivateCommand(final CrazyTimeCard plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYTIMECARD.COMMAND.ACTIVATE.SUCCESS $Card$ $NewLimit$")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!sender.hasPermission("crazytimecard.activate"))
			throw new CrazyCommandPermissionException();
		final PlayerTimeData data = plugin.getCrazyDatabase().getEntry(sender.getName());
		if (data == null)
			throw new CrazyException();
		final String key = ChatHelper.listingString(" ", args);
		final CardData card = plugin.getCardDatabase().getEntry(key);
		if (card == null)
			throw new CrazyCommandNoSuchException("CardData", key);
		if (card.isUsed())
			throw new CrazyTimeCardCardAlreadyUsedException();
		data.activate(card);
		plugin.sendLocaleMessage("COMMAND.ACTIVATE.SUCCESS", sender, key, CrazyLightPluginInterface.DATETIMEFORMAT.format(data.getLimit()));
	}
}
