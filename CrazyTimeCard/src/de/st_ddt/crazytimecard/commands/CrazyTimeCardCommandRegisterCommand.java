package de.st_ddt.crazytimecard.commands;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.commands.CrazyCommandExecutor;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazytimecard.CrazyTimeCard;
import de.st_ddt.crazytimecard.data.CardData;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.databases.Database;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.paramitrisable.DurationParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.IntegerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.StringParamitrisable;

public class CrazyTimeCardCommandRegisterCommand extends CrazyCommandExecutor<CrazyTimeCard>
{

	public CrazyTimeCardCommandRegisterCommand(final CrazyTimeCard plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYTIMECARD.COMMAND.REGISTER.HEADER $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$", "CRAZYTIMECARD.COMMAND.REGISTER.LISTFORMAT $Index$ $Entry$ $ChatHeader$" })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!sender.hasPermission("crazytimecard.register"))
			throw new CrazyCommandPermissionException();
		final Map<String, Paramitrisable> params = new TreeMap<String, Paramitrisable>();
		final StringParamitrisable card = new StringParamitrisable(null);
		params.put("", card);
		params.put("c", card);
		params.put("card", card);
		final StringParamitrisable owner = new StringParamitrisable(sender.getName());
		params.put("o", owner);
		params.put("owner", owner);
		final DurationParamitrisable duration = new DurationParamitrisable(plugin.getDefaultDuration());
		params.put("d", duration);
		params.put("duration", duration);
		final IntegerParamitrisable amount = new IntegerParamitrisable(1);
		params.put("a", amount);
		params.put("amount", amount);
		ChatHelperExtended.readParameters(args, params);
		final ArrayList<String> cards = new ArrayList<String>(amount.getValue());
		if (card.getValue() == null)
			for (int i = 0; i < amount.getValue(); i++)
				cards.add(plugin.genCardKey());
		else
			for (int i = 0; i < amount.getValue(); i++)
				cards.add(card.getValue() + "_" + i);
		final Database<CardData> database = plugin.getCardDatabase();
		for (int i = 0; i < amount.getValue(); i++)
			database.save(new CardData(cards.get(i), owner.getValue(), duration.getValue()));
		plugin.sendLocaleList(sender, "COMMAND.REGISTER.HEADER", "COMMAND.REGISTER.LISTFORMAT", null, -1, 1, cards);
	}
}
