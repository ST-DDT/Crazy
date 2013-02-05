package de.st_ddt.crazychats.channels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.data.ChatPlayerData;
import de.st_ddt.crazyplugin.commands.CrazyCommandCollectionEditor;
import de.st_ddt.crazyplugin.commands.CrazyCommandExecutorInterface;
import de.st_ddt.crazyplugin.commands.CrazyCommandModeEditor;
import de.st_ddt.crazyplugin.commands.CrazyCommandTreeExecutor;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHeaderProvider;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.ConfigurationSaveable;
import de.st_ddt.crazyutil.CrazyChatsChatHelper;
import de.st_ddt.crazyutil.ListFormat;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modes.BooleanFalseMode;
import de.st_ddt.crazyutil.modes.ChatFormatMode;
import de.st_ddt.crazyutil.modes.Mode;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class CustomChannel extends AbstractMuteableChannel implements ControlledChannelInterface, ConfigurationSaveable, ChatHeaderProvider
{

	private final int id;
	private final CrazyCommandTreeExecutor<CustomChannel> mainCommand = new CrazyCommandTreeExecutor<CustomChannel>(this);
	private final String owner;
	private final Set<String> members;
	private final Set<Player> activeMembers;
	private CustomChannelPermissionRule listenRule;
	private String listenPermission;
	private CustomChannelPermissionRule talkRule;
	private final Set<String> talkPlayers;
	private String talkPermission;
	private CustomChannelPermissionRule joinRule;
	private final Set<String> invitedPlayers;
	private CustomChannelPermissionRule inviteRule;
	private String joinPermission;
	private String chatFormat;
	private boolean affectedByServerSilence;
	private final boolean persistent;

	public CustomChannel(final CrazyChats plugin, final String name, final CommandSender owner, final boolean persistent)
	{
		super(name);
		this.id = plugin.getNewChannelID();
		aliases.add(name.toLowerCase());
		aliases.add(Integer.toString(id));
		this.owner = owner.getName();
		this.members = new HashSet<String>();
		if (owner instanceof Player)
			this.members.add(owner.getName());
		this.activeMembers = new HashSet<Player>();
		if (owner instanceof Player)
			activeMembers.add((Player) owner);
		this.listenRule = CustomChannelPermissionRule.ALLMEMBERS;
		this.listenPermission = null;
		this.talkRule = CustomChannelPermissionRule.ALLMEMBERS;
		this.talkPlayers = new HashSet<String>();
		if (owner instanceof Player)
			this.talkPlayers.add(owner.getName());
		this.talkPermission = null;
		this.joinRule = CustomChannelPermissionRule.MEMBERLIST;
		this.invitedPlayers = new HashSet<String>();
		this.inviteRule = CustomChannelPermissionRule.OWNERONLY;
		this.joinPermission = null;
		this.chatFormat = CrazyChatsChatHelper.makeFormat("&2[" + name + "] &F$0$: $1$");
		this.affectedByServerSilence = true;
		this.persistent = persistent;
		registerCommands();
	}

	public CustomChannel(final CrazyChats plugin, final String name, final String owner, final CustomChannelPermissionRule listenRule, final String listenPermission, final CustomChannelPermissionRule talkRule, final String talkPermission, final CustomChannelPermissionRule joinRule, final CustomChannelPermissionRule inviteRule, final String joinPermission, final String chatFormat, final boolean affectedByServerSilence, final boolean persistent)
	{
		super(name);
		this.id = plugin.getNewChannelID();
		aliases.add(name.toLowerCase());
		aliases.add(Integer.toString(id));
		this.owner = owner;
		this.members = new HashSet<String>();
		this.members.add(owner);
		this.members.remove(null);
		this.activeMembers = new HashSet<Player>();
		this.activeMembers.add(Bukkit.getPlayerExact(owner));
		this.activeMembers.remove(null);
		this.listenRule = listenRule;
		this.listenPermission = listenPermission;
		this.talkRule = talkRule;
		this.talkPlayers = new HashSet<String>();
		this.talkPlayers.add(owner);
		this.talkPlayers.remove(null);
		this.talkPermission = talkPermission;
		this.joinRule = joinRule;
		this.invitedPlayers = new HashSet<String>();
		this.inviteRule = inviteRule;
		this.joinPermission = joinPermission;
		this.chatFormat = CrazyChatsChatHelper.makeFormat(chatFormat);
		this.affectedByServerSilence = affectedByServerSilence;
		this.persistent = persistent;
		registerCommands();
	}

	public CustomChannel(final CrazyChats plugin, final ConfigurationSection config)
	{
		super(config.getString("name", "Custom"));
		this.id = config.getInt("id", -1);
		aliases.add(name.toLowerCase());
		aliases.add(Integer.toString(id));
		this.owner = config.getString("owner");
		this.members = new HashSet<String>();
		final List<String> members = config.getStringList("members");
		if (members != null)
			this.members.addAll(members);
		this.members.add(owner);
		this.members.remove(null);
		this.activeMembers = new HashSet<Player>();
		for (final String member : this.members)
			this.activeMembers.add(Bukkit.getPlayerExact(member));
		this.activeMembers.remove(null);
		this.listenRule = CustomChannelPermissionRule.valueOf(config.getString("listenRule", "ALLMEMBERS"));
		this.listenPermission = config.getString("listenPermission");
		this.talkRule = CustomChannelPermissionRule.valueOf(config.getString("talkRule", "ALLMEMBERS"));
		this.talkPlayers = new HashSet<String>();
		final List<String> talkPlayers = config.getStringList("talkPlayers");
		if (talkPlayers != null)
			this.talkPlayers.addAll(talkPlayers);
		this.talkPlayers.add(owner);
		this.talkPlayers.remove(null);
		this.talkPermission = config.getString("talkPermission");
		this.joinRule = CustomChannelPermissionRule.valueOf(config.getString("joinRule", "ALLMEMBERS"));
		this.invitedPlayers = new HashSet<String>();
		final List<String> invitedPlayers = config.getStringList("invitedPlayers");
		if (invitedPlayers == null)
			this.invitedPlayers.addAll(invitedPlayers);
		this.invitedPlayers.remove(owner);
		this.invitedPlayers.remove(null);
		this.inviteRule = CustomChannelPermissionRule.valueOf(config.getString("inviteRule", "OWNERONLY"));
		this.joinPermission = config.getString("joinPermission");
		this.chatFormat = CrazyChatsChatHelper.makeFormat(config.getString("chatFormat"));
		this.affectedByServerSilence = config.getBoolean("affectedByServerSilence", true);
		this.persistent = true;
		registerCommands();
	}

	private void registerCommands()
	{
		final CustomChannelCommandMode modeCommand = new CustomChannelCommandMode(this);
		mainCommand.addSubCommand(modeCommand, "c", "cfg", "config", "o", "option");
		registerModes(modeCommand);
		mainCommand.addSubCommand(new CrazyCommandCollectionEditor<CrazyChats, String>(plugin)
		{

			@Override
			public Collection<String> getCollection()
			{
				return members;
			}

			@Override
			public ListFormat listFormat()
			{
				return new ListFormat()
				{

					CrazyLocale locale = plugin.getLocale().getLanguageEntry("COMMAND.CUSTOMCHANNEL.MEMBERS");

					@Override
					@Localized("CRAZYCHATS.COMMAND.CUSTOMCHANNEL.MEMBERS.LISTFORMAT $Index$ $Entry$ $ChatHeader$")
					public String listFormat(final CommandSender target)
					{
						return locale.getLocaleMessage(target, "LISTFORMAT");
					}

					@Override
					@Localized("CRAZYCHATS.COMMAND.CUSTOMCHANNEL.MEMBERS.HEADFORMAT $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$")
					public String headFormat(final CommandSender target)
					{
						return locale.getLocaleMessage(target, "HEADFORMAT");
					}

					@Override
					public String entryFormat(final CommandSender target)
					{
						return "$0$";
					}
				};
			}

			@Override
			public String getEntry(final CommandSender sender, final String... args) throws CrazyException
			{
				return ChatHelper.listingString(args);
			}

			@Override
			@Localized("CRAZYCHATS.COMMAND.CUSTOMCHANNEL.MEMBERS.ADDED $Element$")
			public String addLocale()
			{
				return "COMMAND.CUSTOMCHANNEL.MEMBERS.ADDED";
			}

			@Override
			@Localized("CRAZYCHATS.COMMAND.CUSTOMCHANNEL.MEMBERS.REMOVED $Element$")
			public String removeLocale()
			{
				return "COMMAND.CUSTOMCHANNEL.MEMBERS.REMOVED";
			}

			@Override
			public boolean hasAccessPermission(final CommandSender sender)
			{
				return sender.getName().equals(owner) || PermissionModule.hasPermission(sender, "crazychats.customchannel.admin");
			}
		}, "members");
		mainCommand.addSubCommand(new CrazyCommandCollectionEditor<CrazyChats, String>(plugin)
		{

			@Override
			public Collection<String> getCollection()
			{
				return talkPlayers;
			}

			@Override
			public ListFormat listFormat()
			{
				return new ListFormat()
				{

					CrazyLocale locale = plugin.getLocale().getLanguageEntry("COMMAND.CUSTOMCHANNEL.TALKPLAYERS");

					@Override
					@Localized("CRAZYCHATS.COMMAND.CUSTOMCHANNEL.TALKPLAYERS.LISTFORMAT $Index$ $Entry$ $ChatHeader$")
					public String listFormat(final CommandSender target)
					{
						return locale.getLocaleMessage(target, "LISTFORMAT");
					}

					@Override
					@Localized("CRAZYCHATS.COMMAND.CUSTOMCHANNEL.TALKPLAYERS.HEADFORMAT $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$")
					public String headFormat(final CommandSender target)
					{
						return locale.getLocaleMessage(target, "HEADFORMAT");
					}

					@Override
					public String entryFormat(final CommandSender target)
					{
						return "$0$";
					}
				};
			}

			@Override
			public String getEntry(final CommandSender sender, final String... args) throws CrazyException
			{
				return ChatHelper.listingString(args);
			}

			@Override
			@Localized("CRAZYCHATS.COMMAND.CUSTOMCHANNEL.TALKPLAYERS.ADDED $Element$")
			public String addLocale()
			{
				return "COMMAND.CUSTOMCHANNEL.TALKPLAYERS.ADDED";
			}

			@Override
			@Localized("CRAZYCHATS.COMMAND.CUSTOMCHANNEL.TALKPLAYERS.REMOVED $Element$")
			public String removeLocale()
			{
				return "COMMAND.CUSTOMCHANNEL.TALKPLAYERS.REMOVED";
			}

			@Override
			public boolean hasAccessPermission(final CommandSender sender)
			{
				return sender.getName().equals(owner) || PermissionModule.hasPermission(sender, "crazychats.customchannel.admin");
			}
		}, "talkPlayers");
		mainCommand.addSubCommand(new CrazyCommandCollectionEditor<CrazyChats, String>(plugin)
		{

			@Override
			public Collection<String> getCollection()
			{
				return invitedPlayers;
			}

			@Override
			public ListFormat listFormat()
			{
				return new ListFormat()
				{

					CrazyLocale locale = plugin.getLocale().getLanguageEntry("COMMAND.CUSTOMCHANNEL.INVITEDPLAYERS");

					@Override
					@Localized("CRAZYCHATS.COMMAND.CUSTOMCHANNEL.INVITEDPLAYERS.LISTFORMAT $Index$ $Entry$ $ChatHeader$")
					public String listFormat(final CommandSender target)
					{
						return locale.getLocaleMessage(target, "LISTFORMAT");
					}

					@Override
					@Localized("CRAZYCHATS.COMMAND.CUSTOMCHANNEL.INVITEDPLAYERS.HEADFORMAT $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$")
					public String headFormat(final CommandSender target)
					{
						return locale.getLocaleMessage(target, "HEADFORMAT");
					}

					@Override
					public String entryFormat(final CommandSender target)
					{
						return "$0$";
					}
				};
			}

			@Override
			public String getEntry(final CommandSender sender, final String... args) throws CrazyException
			{
				return ChatHelper.listingString(args);
			}

			@Override
			@Localized("CRAZYCHATS.COMMAND.CUSTOMCHANNEL.INVITEDPLAYERS.ADDED $Element$")
			public String addLocale()
			{
				return "COMMAND.CUSTOMCHANNEL.INVITEDPLAYERS.ADDED";
			}

			@Override
			@Localized("CRAZYCHATS.COMMAND.CUSTOMCHANNEL.INVITEDPLAYERS.REMOVED $Element$")
			public String removeLocale()
			{
				return "COMMAND.CUSTOMCHANNEL.INVITEDPLAYERS.REMOVED";
			}

			@Override
			public boolean hasAccessPermission(final CommandSender sender)
			{
				if (PermissionModule.hasPermission(sender, "crazychats.customchannel.admin"))
					return true;
				switch (inviteRule)
				{
					case EVERYONE:
						return true;
					case ALLMEMBERS:
						return members.contains(sender.getName());
					case MEMBERLIST:
						return talkPlayers.contains(sender.getName());
					case OWNERONLY:
						return sender.getName().equals(owner);
					case PERMISSION:
						return PermissionModule.hasPermission(sender, joinPermission);
					default:
						return false;
				}
			}
		}, "invitedPlayers");
	}

	private void registerModes(final CustomChannelCommandMode modeCommand)
	{
		modeCommand.addMode(new RuleMode(plugin, "listenRule")
		{

			@Override
			public void setValue(final CustomChannelPermissionRule newValue) throws CrazyException
			{
				listenRule = newValue;
			}

			@Override
			public CustomChannelPermissionRule getValue()
			{
				return listenRule;
			}
		});
		modeCommand.addMode(new Mode<String>(plugin, "listenPermission", String.class)
		{

			@Override
			public void setValue(final CommandSender sender, final String... args) throws CrazyException
			{
				setValue(ChatHelper.listingString(args));
				if (persistent)
					plugin.saveConfiguration();
			}

			@Override
			public void setValue(final String newValue) throws CrazyException
			{
				listenPermission = newValue;
			}

			@Override
			public String getValue()
			{
				return listenPermission;
			}
		});
		modeCommand.addMode(new RuleMode(plugin, "talkRule")
		{

			@Override
			public void setValue(final CustomChannelPermissionRule newValue) throws CrazyException
			{
				talkRule = newValue;
			}

			@Override
			public CustomChannelPermissionRule getValue()
			{
				return talkRule;
			}
		});
		modeCommand.addMode(new Mode<String>(plugin, "talkPermission", String.class)
		{

			@Override
			public void setValue(final CommandSender sender, final String... args) throws CrazyException
			{
				setValue(ChatHelper.listingString(args));
				if (persistent)
					plugin.saveConfiguration();
			}

			@Override
			public void setValue(final String newValue) throws CrazyException
			{
				talkPermission = newValue;
			}

			@Override
			public String getValue()
			{
				return talkPermission;
			}
		});
		modeCommand.addMode(new RuleMode(plugin, "joinRule")
		{

			@Override
			public void setValue(final CustomChannelPermissionRule newValue) throws CrazyException
			{
				joinRule = newValue;
			}

			@Override
			public CustomChannelPermissionRule getValue()
			{
				return joinRule;
			}
		});
		modeCommand.addMode(new RuleMode(plugin, "inviteRule")
		{

			@Override
			public void setValue(final CustomChannelPermissionRule newValue) throws CrazyException
			{
				inviteRule = newValue;
			}

			@Override
			public CustomChannelPermissionRule getValue()
			{
				return inviteRule;
			}
		});
		modeCommand.addMode(new Mode<String>(plugin, "joinPermission", String.class)
		{

			@Override
			public void setValue(final CommandSender sender, final String... args) throws CrazyException
			{
				setValue(ChatHelper.listingString(args));
				if (persistent)
					plugin.saveConfiguration();
			}

			@Override
			public void setValue(final String newValue) throws CrazyException
			{
				joinPermission = newValue;
			}

			@Override
			public String getValue()
			{
				return joinPermission;
			}
		});
		modeCommand.addMode(new ChatFormatMode(plugin, "chatFormat")
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				final String raw = getValue();
				plugin.sendLocaleMessage("FORMAT.CHANGE", sender, name, raw);
				plugin.sendLocaleMessage("FORMAT.EXAMPLE", sender, ChatHelper.putArgs(ChatHelper.colorise(raw), "Sender", "Message", "GroupPrefix", "GroupSuffix", "World"));
			}

			@Override
			public String getValue()
			{
				return CrazyChatsChatHelper.unmakeFormat(chatFormat);
			}

			@Override
			public void setValue(final CommandSender sender, final String... args) throws CrazyException
			{
				setValue(CrazyChatsChatHelper.makeFormat(ChatHelper.listingString(" ", args)));
				showValue(sender);
			}

			@Override
			public void setValue(final String newValue) throws CrazyException
			{
				chatFormat = newValue;
				plugin.saveConfiguration();
			}

			@Override
			public List<String> tab(final String... args)
			{
				if (args.length != 1 && args[0].length() != 0)
					return null;
				final List<String> res = new ArrayList<String>(1);
				res.add(getValue());
				return res;
			}
		});
		modeCommand.addMode(new BooleanFalseMode(plugin, "affectedByServerSilence")
		{

			@Override
			public void setValue(final Boolean newValue) throws CrazyException
			{
				affectedByServerSilence = newValue;
			}

			@Override
			public Boolean getValue()
			{
				return affectedByServerSilence;
			}
		});
	}

	@Override
	public boolean hasTalkPermission(final Player player)
	{
		switch (talkRule)
		{
			case EVERYONE:
				return true;
			case ALLMEMBERS:
				return members.contains(player.getName());
			case MEMBERLIST:
				return talkPlayers.contains(player.getName());
			case OWNERONLY:
				return player.getName().equals(owner);
			case PERMISSION:
				if (talkPermission == null)
					return false;
				else
					return PermissionModule.hasPermission(player, talkPermission);
			default:
				return false;
		}
	}

	@Override
	public Set<Player> getTargets(final Player player)
	{
		switch (listenRule)
		{
			case EVERYONE:
			{
				final Set<Player> players = new HashSet<Player>();
				for (final Player target : Bukkit.getOnlinePlayers())
					players.add(target);
				return players;
			}
			case OWNERONLY:
			{
				final Set<Player> players = new HashSet<Player>();
				players.add(Bukkit.getPlayerExact(owner));
				players.remove(null);
				return players;
			}
			case ALLMEMBERS:
				return activeMembers;
			case MEMBERLIST:
				return activeMembers;
			case PERMISSION:
			{
				final Set<Player> players = new HashSet<Player>();
				if (listenPermission == null)
					return players;
				for (final Player target : Bukkit.getOnlinePlayers())
					if (PermissionModule.hasPermission(target, listenPermission))
						players.add(target);
				return players;
			}
			default:
				return new HashSet<Player>();
		}
	}

	@Override
	public String getFormat(final Player player)
	{
		return chatFormat;
	}

	@Override
	public boolean isAffectedByServerSilence()
	{
		return affectedByServerSilence;
	}

	public boolean participate(final Player player, final boolean viaSign)
	{
		final ChatPlayerData data = plugin.getPlayerData(player);
		if (data == null)
			return false;
		switch (joinRule)
		{
			case EVERYONE:
			{
				members.add(player.getName());
				activeMembers.add(player);
				data.getAccessibleChannels().add(this);
				return true;
			}
			case ALLMEMBERS:
				if (invitedPlayers.remove(player.getName()))
				{
					members.add(player.getName());
					activeMembers.add(player);
					data.getAccessibleChannels().add(this);
					return true;
				}
				else
					return false;
			case MEMBERLIST:
				if (invitedPlayers.remove(player.getName()) || viaSign)
				{
					members.add(player.getName());
					activeMembers.add(player);
					data.getAccessibleChannels().add(this);
					return true;
				}
				else
					return false;
			case OWNERONLY:
				if (viaSign)
				{
					members.add(player.getName());
					activeMembers.add(player);
					data.getAccessibleChannels().add(this);
					return true;
				}
				else
					return false;
			case PERMISSION:
			{
				if (joinPermission == null)
					return false;
				if (PermissionModule.hasPermission(player, joinPermission))
				{
					members.add(player.getName());
					activeMembers.add(player);
					data.getAccessibleChannels().add(this);
					return true;
				}
				else
					return false;
			}
			default:
				return false;
		}
	}

	public boolean join(final Player player)
	{
		if (members.contains(player.getName()))
		{
			activeMembers.add(player);
			return true;
		}
		else
			return false;
	}

	@Override
	public boolean kick(final Player player)
	{
		return activeMembers.remove(player);
	}

	public CrazyCommandExecutorInterface getCommand()
	{
		return mainCommand;
	}

	public void simpleSave(final ConfigurationSection config, final String path)
	{
		save(config, path + id + ".");
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		if (persistent == false)
			return;
		config.set(path + "id", id);
		config.set(path + "name", name);
		config.set(path + "owner", owner);
		config.set(path + "members", new ArrayList<String>(members));
		config.set(path + "listenRule", listenRule.name());
		config.set(path + "listenPermission", listenPermission);
		config.set(path + "talkRule", talkRule.name());
		config.set(path + "talkPlayers", new ArrayList<String>(talkPlayers));
		config.set(path + "talkPermission", talkPermission);
		config.set(path + "joinRule", joinRule.name());
		config.set(path + "invitedPlayers", new ArrayList<String>(invitedPlayers));
		config.set(path + "inviteRule", inviteRule.name());
		config.set(path + "joinPermission", joinPermission);
		config.set(path + "chatFormat", CrazyChatsChatHelper.unmakeFormat(chatFormat));
		config.set(path + "affectedByServerSilence", affectedByServerSilence);
	}

	@Override
	public String toString()
	{
		return "CustomChannel (Name: " + name + ", ID: " + id + ", Owner: " + owner + ")";
	}

	@Override
	public int hashCode()
	{
		return id;
	}

	public int getId()
	{
		return id;
	}

	public String getOwner()
	{
		return owner;
	}

	@Override
	public String getChatHeader()
	{
		return plugin.getChatHeader().replace(']', '_') + ChatColor.GREEN + "Custom" + ChatColor.RED + "]";
	}

	public static enum CustomChannelPermissionRule
	{
		EVERYONE,
		OWNERONLY,
		ALLMEMBERS,
		MEMBERLIST,
		PERMISSION;
	}

	protected class CustomChannelCommandMode extends CrazyCommandModeEditor<CustomChannel>
	{

		public CustomChannelCommandMode(final CustomChannel channel)
		{
			super(channel);
		}

		@Override
		public boolean hasAccessPermission(final CommandSender sender)
		{
			return sender.getName().equals(owner) || PermissionModule.hasPermission(sender, "crazychats.customchannel.admin");
		}
	}

	protected abstract class RuleMode extends Mode<CustomChannelPermissionRule>
	{

		public RuleMode(final CrazyChats plugin, final String name)
		{
			super(plugin, name, CustomChannelPermissionRule.class);
		}

		@Override
		public void setValue(final CommandSender sender, final String... args) throws CrazyException
		{
			CustomChannelPermissionRule rule;
			try
			{
				rule = CustomChannelPermissionRule.valueOf(args[0].toUpperCase());
			}
			catch (final Exception e)
			{
				rule = null;
			}
			if (rule == null)
				throw new CrazyCommandNoSuchException("Rule", args[0]);
			setValue(rule);
			if (persistent)
				plugin.saveConfiguration();
		}
	}
}
