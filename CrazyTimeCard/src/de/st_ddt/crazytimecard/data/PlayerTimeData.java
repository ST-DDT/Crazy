package de.st_ddt.crazytimecard.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyplugin.CrazyLightPluginInterface;
import de.st_ddt.crazyplugin.data.PlayerData;
import de.st_ddt.crazytimecard.CrazyTimeCard;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;
import de.st_ddt.crazyutil.databases.ConfigurationPlayerDataDatabaseEntry;
import de.st_ddt.crazyutil.databases.FlatPlayerDataDatabaseEntry;
import de.st_ddt.crazyutil.databases.MySQLDatabase;
import de.st_ddt.crazyutil.databases.MySQLPlayerDataDatabaseEntry;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public class PlayerTimeData extends PlayerData<PlayerTimeData> implements ConfigurationPlayerDataDatabaseEntry, FlatPlayerDataDatabaseEntry, MySQLPlayerDataDatabaseEntry
{

	private String card;
	private Date limit;

	public PlayerTimeData(final String name)
	{
		super(name);
		this.card = "";
		this.limit = new Date();
	}

	public PlayerTimeData(final String name, final long limit)
	{
		super(name);
		this.card = "";
		this.limit = new Date();
		this.limit.setTime(this.limit.getTime() + limit);
	}

	// aus Config-Datenbank laden
	public PlayerTimeData(final ConfigurationSection config, final String[] columnNames)
	{
		super(config.getString(columnNames[0]));
		this.card = config.getString(columnNames[1]);
		this.limit = ObjectSaveLoadHelper.StringToDate(config.getString(columnNames[2]), new Date());
	}

	// in Config-Datenbank speichern
	@Override
	public void saveToConfigDatabase(final ConfigurationSection config, final String path, final String[] columnNames)
	{
		config.set(path + columnNames[0], name);
		config.set(path + columnNames[1], card);
		config.set(path + columnNames[2], limit);
	}

	// aus Flat-Datenbank laden
	public PlayerTimeData(final String[] rawData)
	{
		super(rawData[0]);
		if (rawData.equals("."))
			this.card = "";
		else
			this.card = rawData[1];
		limit = ObjectSaveLoadHelper.StringToDate(rawData[2], new Date());
	}

	// in Flat-Datenbank speichern
	@Override
	public String[] saveToFlatDatabase()
	{
		final String[] strings = new String[3];
		strings[0] = name;
		if (card.length() == 0)
			strings[1] = ".";
		else
			strings[1] = card;
		strings[2] = ObjectSaveLoadHelper.DateToString(limit);
		return strings;
	}

	// aus MySQL-Datenbank laden
	public PlayerTimeData(final ResultSet rawData, final String[] columnNames)
	{
		super(MySQLDatabase.readName(rawData, columnNames[0]));
		try
		{
			card = rawData.getString(columnNames[1]);
		}
		catch (final SQLException e)
		{
			card = "FAILEDLOADING";
			e.printStackTrace();
		}
		try
		{
			limit = rawData.getTimestamp(columnNames[2]);
		}
		catch (final SQLException e)
		{
			limit = new Date();
			e.printStackTrace();
		}
	}

	// in MySQL-Datenbank speichern
	@Override
	public String saveToMySQLDatabase(final String[] columnNames)
	{
		final Timestamp timestamp = new Timestamp(limit.getTime());
		return columnNames[1] + "='" + card + "', " + columnNames[2] + "='" + timestamp + "'";
	}

	@Override
	public void showDetailed(final CommandSender target, final String chatHeader)
	{
		final CrazyLocale locale = CrazyLocale.getLocaleHead().getSecureLanguageEntry("CRAZYTIMECARD.PLAYERINFO");
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("LIMIT"), CrazyLightPluginInterface.DATETIMEFORMAT.format(limit));
	}

	@Override
	public String getParameter(final CommandSender sender, final int index)
	{
		switch (index)
		{
			case 0:
				return name;
			case 1:
				return card;
			case 2:
				return CrazyLightPluginInterface.DATETIMEFORMAT.format(limit);
			default:
				return null;
		}
	}

	@Override
	public int getParameterCount()
	{
		return 3;
	}

	@Override
	public int compareTo(final PlayerTimeData o)
	{
		return name.compareToIgnoreCase(o.name);
	}

	@Override
	protected String getChatHeader()
	{
		return getPlugin().getChatHeader();
	}

	protected CrazyTimeCard getPlugin()
	{
		return CrazyTimeCard.getPlugin();
	}

	public boolean isActive()
	{
		return limit.after(new Date());
	}

	public Date getLimit()
	{
		return limit;
	}

	public void setLimit(final Date limit)
	{
		this.limit = limit;
	}

	public String getCard()
	{
		return card;
	}

	public boolean activate(final CardData card)
	{
		if (card.isUsed())
			return false;
		final long now = new Date().getTime();
		final long rest = Math.max(limit.getTime() - now, 0);
		limit = new Date(now + card.getDuration() + rest);
		card.activate(name);
		final CrazyTimeCard plugin = getPlugin();
		plugin.getCrazyDatabase().save(this);
		plugin.getCardDatabase().save(card);
		return true;
	}
}
