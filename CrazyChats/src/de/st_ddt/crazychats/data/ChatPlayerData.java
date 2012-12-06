package de.st_ddt.crazychats.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.channels.ChannelInterface;
import de.st_ddt.crazychats.channels.PrivateChannel;
import de.st_ddt.crazyplugin.CrazyLightPluginInterface;
import de.st_ddt.crazyplugin.data.PlayerData;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.CrazyChatsChatHelper;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;
import de.st_ddt.crazyutil.databases.ConfigurationPlayerDataDatabaseEntry;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.locales.Localized;

public class ChatPlayerData extends PlayerData<ChatPlayerData> implements ConfigurationPlayerDataDatabaseEntry
{

	private final Set<ChannelInterface> accessibleChannels = Collections.synchronizedSet(new LinkedHashSet<ChannelInterface>());
	private final Set<String> mutedPlayers = Collections.synchronizedSet(new HashSet<String>());
	private ChannelInterface currentChannel = null;
	private PrivateChannel privateChannel = null;
	private String displayName = null;
	private String listName = null;
	private String headName = null;
	private Date silenced;

	public ChatPlayerData(final String name)
	{
		super(name);
		silenced = new Date(0);
	}

	// aus Config-Datenbank laden
	public ChatPlayerData(final ConfigurationSection config, final String[] columnNames)
	{
		super(config.getString(columnNames[0]));
		final List<String> muted = config.getStringList(columnNames[1]);
		if (muted != null)
			mutedPlayers.addAll(muted);
		setDisplayName(CrazyChatsChatHelper.makeFormat(config.getString(columnNames[2], null)));
		setListName(CrazyChatsChatHelper.makeFormat(config.getString(columnNames[3], null)));
		setHeadName(CrazyChatsChatHelper.makeFormat(config.getString(columnNames[4], null)));
		silenced = ObjectSaveLoadHelper.StringToDate(config.getString(columnNames[5]), new Date());
	}

	// in Config-Datenbank speichern
	@Override
	public void saveToConfigDatabase(final ConfigurationSection config, final String path, final String[] columnNames)
	{
		config.set(path + columnNames[0], name);
		final List<String> list;
		synchronized (columnNames)
		{
			list = new ArrayList<String>(mutedPlayers);
		}
		config.set(path + columnNames[1], list);
		config.set(path + columnNames[2], CrazyChatsChatHelper.unmakeFormat(displayName));
		config.set(path + columnNames[3], CrazyChatsChatHelper.unmakeFormat(listName));
		config.set(path + columnNames[4], CrazyChatsChatHelper.unmakeFormat(headName));
		config.set(path + columnNames[5], CrazyLightPluginInterface.DATETIMEFORMAT.format(silenced));
	}

	public ChannelInterface getCurrentChannel()
	{
		if (currentChannel != null)
			if (!currentChannel.hasTalkPermission(getPlayer()))
				currentChannel = null;
		return currentChannel;
	}

	public void setCurrentChannelForced(final ChannelInterface channel)
	{
		this.currentChannel = channel;
	}

	public void setCurrentChannel(ChannelInterface channel)
	{
		if (channel != null)
			if (!channel.hasTalkPermission(getPlayer()))
				channel = null;
		this.currentChannel = channel;
	}

	public PrivateChannel getPrivateChannel()
	{
		return privateChannel;
	}

	public void setPrivateChannel(final PrivateChannel privateChannel)
	{
		this.privateChannel = privateChannel;
		accessibleChannels.add(privateChannel);
	}

	public Set<ChannelInterface> getAccessibleChannels()
	{
		return accessibleChannels;
	}

	public Map<String, ChannelInterface> getChannelMap()
	{
		accessibleChannels.remove(null);
		final Map<String, ChannelInterface> res = new TreeMap<String, ChannelInterface>();
		for (final ChannelInterface channel : accessibleChannels)
			if (channel.hasTalkPermission(getPlayer()))
				for (final String alias : channel.getAliases())
					res.put(alias, channel);
		return res;
	}

	@Override
	@Localized({ "CRAZYCHATS.PLAYERINFO.CHANNEL $Name$", "CRAZYCHATS.PLAYERINFO.PRIVATECHANNEL.TARGETS $Targets$", "CRAZYCHATS.PLAYERINFO.DISPLAYNAME $Name$", "CRAZYCHATS.PLAYERINFO.LISTNAME $Name$", "CRAZYCHATS.PLAYERINFO.HEADNAME $Name$", "CRAZYCHATS.PLAYERINFO.SILENCED $UntilDateTime$" })
	public void showDetailed(final CommandSender target, final String chatHeader)
	{
		final CrazyLocale locale = CrazyLocale.getLocaleHead().getSecureLanguageEntry("CRAZYCHATS.PLAYERINFO");
		if (getCurrentChannel() == null)
			ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("CHANNEL"), "NONE");
		else
			ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("CHANNEL"), currentChannel.getName());
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("PRIVATECHANNEL.TARGETS"), ChatHelper.listingString(privateChannel.getTargets(null)));
		if (displayName != null)
			ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("DISPLAYNAME"), displayName);
		if (listName != null)
			ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("LISTNAME"), listName);
		if (headName != null)
			ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("HEADNAME"), headName);
		if (isSilenced())
			ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("SILENCED"), CrazyLightPluginInterface.DATETIMEFORMAT.format(silenced));
	}

	@Override
	public String getParameter(final CommandSender sender, final int index)
	{
		switch (index)
		{
			case 0:
				return name;
			default:
				return "";
		}
	}

	@Override
	public int getParameterCount()
	{
		return 1;
	}

	protected CrazyChats getPlugin()
	{
		return CrazyChats.getPlugin();
	}

	@Override
	protected String getChatHeader()
	{
		return getPlugin().getChatHeader();
	}

	public void quit()
	{
		privateChannel = null;
		currentChannel = null;
		accessibleChannels.clear();
	}

	public Set<String> getMutedPlayers()
	{
		return mutedPlayers;
	}

	public boolean isMuted(final OfflinePlayer player)
	{
		return isMuted(player.getName());
	}

	public boolean isMuted(final String name)
	{
		return mutedPlayers.contains(name.toLowerCase());
	}

	public void mute(final OfflinePlayer player)
	{
		mute(player.getName());
	}

	public void mute(final String name)
	{
		mutedPlayers.add(name.toLowerCase());
	}

	public void unmute(final OfflinePlayer player)
	{
		unmute(player.getName());
	}

	public void unmute(final String name)
	{
		mutedPlayers.remove(name.toLowerCase());
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public void setDisplayName(final String displayName)
	{
		this.displayName = displayName;
	}

	public String getListName()
	{
		return listName;
	}

	public void setListName(final String listName)
	{
		if (listName.length() > 16)
			this.listName = listName.substring(0, 16);
		else
			this.listName = listName;
	}

	public String getHeadName()
	{
		return headName;
	}

	public void setHeadName(final String headName)
	{
		if (headName.length() > 16)
			this.headName = headName.substring(0, 16);
		else
			this.headName = headName;
	}

	public boolean isSilenced()
	{
		return silenced.after(new Date());
	}

	public Date getSilenced()
	{
		return silenced;
	}

	public void setSilenced(final Date silenced)
	{
		this.silenced = silenced;
	}
}
