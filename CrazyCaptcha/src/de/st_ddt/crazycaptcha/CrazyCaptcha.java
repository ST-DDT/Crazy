package de.st_ddt.crazycaptcha;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazycaptcha.captcha.BasicCaptchaGenerator;
import de.st_ddt.crazycaptcha.captcha.Captcha;
import de.st_ddt.crazycaptcha.captcha.CaptchaGenerator;
import de.st_ddt.crazycaptcha.captcha.CaptchaHelper;
import de.st_ddt.crazycaptcha.commands.CrazyCaptchaCommandExecutor;
import de.st_ddt.crazycaptcha.commands.CrazyCaptchaCommandMainCommands;
import de.st_ddt.crazycaptcha.commands.CrazyCaptchaCommandReverify;
import de.st_ddt.crazycaptcha.commands.CrazyCaptchaCommandVerify;
import de.st_ddt.crazycaptcha.commands.CrazyCommandVerifiedCheck;
import de.st_ddt.crazycaptcha.listener.CrazyCaptchaPlayerListener;
import de.st_ddt.crazycaptcha.listener.CrazyCaptchaPlayerListener_125;
import de.st_ddt.crazycaptcha.listener.CrazyCaptchaPlayerListener_132;
import de.st_ddt.crazycaptcha.tasks.CaptchaReminderTask;
import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.commands.CrazyPluginCommandMainMode;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.VersionComparator;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.login.LoginModule;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public final class CrazyCaptcha extends CrazyPlugin
{

	private static CrazyCaptcha plugin;
	private final Set<String> verified = new HashSet<String>();
	private final HashMap<String, Integer> verificationFailures = new HashMap<String, Integer>();
	private final Map<String, Date> tempBans = new HashMap<String, Date>();
	private final Map<String, Captcha> captchas = new HashMap<String, Captcha>();
	private final CrazyPluginCommandMainMode modeCommand = new CrazyPluginCommandMainMode(this);
	private CrazyCaptchaPlayerListener playerListener;
	private CaptchaGenerator generator;
	private List<String> commandWhiteList;
	private long reminderInterval;
	private int autoKick;
	private long autoTempBan;
	private int autoKickVerificationFailer;
	private long autoTempBanVerificationFailer;
	private boolean autoKickCommandUsers;
	private boolean skipLoginRegistered;
	static
	{
		CaptchaHelper.registerGenerator("Basic", BasicCaptchaGenerator.class);
	}

	public static CrazyCaptcha getPlugin()
	{
		return plugin;
	}

	public CrazyCaptcha()
	{
		super();
		registerModes();
	}

	@Localized("CRAZYCAPTCHA.MODE.CHANGE $Name$ $Value$")
	private void registerModes()
	{
		modeCommand.addMode(modeCommand.new DurationMode("reminderInterval")
		{

			@Override
			public void setValue(final Long newValue) throws CrazyException
			{
				reminderInterval = newValue / 1000;
			}

			@Override
			public Long getValue()
			{
				return reminderInterval * 1000;
			}
		});
		modeCommand.addMode(modeCommand.new IntegerMode("autoKick")
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				sendLocaleMessage("MODE.CHANGE", sender, name, getValue() == -1 ? "disabled" : getValue() + " seconds");
			}

			@Override
			public Integer getValue()
			{
				return autoKick;
			}

			@Override
			public void setValue(final Integer newValue) throws CrazyException
			{
				autoKick = Math.max(newValue, -1);
				saveConfiguration();
			}
		});
		modeCommand.addMode(modeCommand.new LongMode("autoTempBan")
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				sendLocaleMessage("MODE.CHANGE", sender, name, getValue() == -1 ? "disabled" : getValue() + " seconds");
			}

			@Override
			public Long getValue()
			{
				return autoTempBan;
			}

			@Override
			public void setValue(final Long newValue) throws CrazyException
			{
				autoTempBan = Math.max(newValue, -1);
				saveConfiguration();
			}
		});
		modeCommand.addMode(modeCommand.new IntegerMode("autoKickVerificationFailer")
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				sendLocaleMessage("MODE.CHANGE", sender, name, getValue() == -1 ? "disabled" : getValue() + " failed attempts");
			}

			@Override
			public Integer getValue()
			{
				return autoKickVerificationFailer;
			}

			@Override
			public void setValue(final Integer newValue) throws CrazyException
			{
				autoKickVerificationFailer = Math.max(newValue, -1);
				saveConfiguration();
			}
		});
		modeCommand.addMode(modeCommand.new LongMode("autoTempBanVerificationFailer")
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				sendLocaleMessage("MODE.CHANGE", sender, name, getValue() == -1 ? "disabled" : getValue() + " seconds");
			}

			@Override
			public Long getValue()
			{
				return autoTempBanVerificationFailer;
			}

			@Override
			public void setValue(final Long newValue) throws CrazyException
			{
				autoTempBanVerificationFailer = Math.max(newValue, -1);
				saveConfiguration();
			}
		});
		modeCommand.addMode(modeCommand.new BooleanFalseMode("autoKickCommandUsers")
		{

			@Override
			public Boolean getValue()
			{
				return autoKickCommandUsers;
			}

			@Override
			public void setValue(final Boolean newValue) throws CrazyException
			{
				autoKickCommandUsers = newValue;
				saveConfiguration();
			}
		});
	}

	private void registerCommands()
	{
		getCommand("captcha").setExecutor(new CrazyCaptchaCommandVerify(this));
		final CrazyCaptchaCommandExecutor reverify = new CrazyCaptchaCommandReverify(this);
		getCommand("recaptcha").setExecutor(reverify);
		mainCommand.addSubCommand(reverify, "reverify", "recaptcha");
		mainCommand.addSubCommand(new CrazyCommandVerifiedCheck(this, modeCommand), "mode");
		mainCommand.addSubCommand(new CrazyCaptchaCommandMainCommands(this), "commands");
	}

	private void registerHooks()
	{
		final String mcVersion = ChatHelper.getMinecraftVersion();
		if (VersionComparator.compareVersions(mcVersion, "1.2.5") == 1)
			playerListener = new CrazyCaptchaPlayerListener_132(this);
		else
			playerListener = new CrazyCaptchaPlayerListener_125(this);
		final PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(playerListener, this);
	}

	@Override
	public void onLoad()
	{
		plugin = this;
		super.onLoad();
	}

	@Override
	public void onEnable()
	{
		registerHooks();
		LoginModule.init(getChatHeader(), Bukkit.getConsoleSender());
		super.onEnable();
		registerCommands();
		verified.clear();
		for (final Player player : Bukkit.getOnlinePlayers())
			playerListener.PlayerJoin(player);
	}

	@Override
	public void onDisable()
	{
		verified.clear();
		super.onDisable();
	}

	@Override
	public void loadConfiguration()
	{
		super.loadConfiguration();
		final ConfigurationSection config = getConfig();
		commandWhiteList = config.getStringList("commandWhitelist");
		reminderInterval = config.getLong("reminderInterval", 5);
		autoKick = Math.max(config.getInt("autoKick", -1), -1);
		autoTempBan = Math.max(config.getInt("autoTempBan", -1), -1);
		tempBans.clear();
		autoKickVerificationFailer = Math.max(config.getInt("autoKickVerificationFailer", 3), -1);
		autoTempBanVerificationFailer = Math.max(config.getInt("autoTempBanVerificationFailer", -1), -1);
		verificationFailures.clear();
		autoKickCommandUsers = config.getBoolean("autoKickCommandUsers", false);
		generator = CaptchaHelper.getCaptchaGenerator(this, config.getConfigurationSection("generator"));
		if (generator == null)
			generator = new BasicCaptchaGenerator(this, null);
		skipLoginRegistered = config.getBoolean("skipLoginRegistered", true);
		// Logger
		logger.createLogChannels(config.getConfigurationSection("logs"), "Join", "Quit", "Captcha", "CaptchaFail", "ChatBlocked", "CommandBlocked", "AccessDenied");
	}

	@Override
	public void saveConfiguration()
	{
		final ConfigurationSection config = getConfig();
		config.set("commandWhitelist", commandWhiteList);
		config.set("reminderInterval", reminderInterval);
		config.set("autoKick", autoKick);
		config.set("autoTempBan", autoTempBan);
		config.set("autoKickVerificationFailer", autoKickVerificationFailer);
		config.set("autoTempBanVerificationFailer", autoTempBanVerificationFailer);
		config.set("autoKickCommandUsers", autoKickCommandUsers);
		if (generator != null)
			generator.save(config, "generator.");
		config.set("skipLoginRegistered", skipLoginRegistered);
		super.saveConfiguration();
	}

	public boolean isVerified(final OfflinePlayer player)
	{
		return isVerified(player.getName());
	}

	public boolean isVerified(final String name)
	{
		return verified.contains(name.toLowerCase());
	}

	public List<String> getCommandWhiteList()
	{
		return commandWhiteList;
	}

	public boolean isTempBanned(final String IP)
	{
		final Date date = tempBans.get(IP);
		if (date == null)
			return false;
		else
			return new Date().before(date);
	}

	public Date getTempBanned(final String IP)
	{
		return tempBans.get(IP);
	}

	public String getTempBannedString(final String IP)
	{
		final Date date = getTempBanned(IP);
		if (date == null)
			return DATETIMEFORMAT.format(new Date(0));
		return DATETIMEFORMAT.format(date);
	}

	public void setTempBanned(final Player player, final long duration)
	{
		setTempBanned(player.getAddress().getAddress().getHostAddress(), duration);
	}

	public void setTempBanned(final String IP, final long duration)
	{
		final Date until = new Date();
		until.setTime(until.getTime() + duration * 1000);
		tempBans.put(IP, until);
	}

	public boolean playerJoin(final Player player)
	{
		if (isVerified(player))
			return true;
		if (PermissionModule.hasPermission(player, "crazycaptcha.donotbotherme"))
		{
			verified.add(player.getName().toLowerCase());
			return true;
		}
		if (skipLoginRegistered)
			if (LoginModule.hasAccount(player))
			{
				verified.add(player.getName().toLowerCase());
				return true;
			}
		new CaptchaReminderTask(player, plugin).startTask(reminderInterval);
		return false;
	}

	public void playerQuit(final Player player)
	{
		playerQuit(player.getName());
	}

	public void playerQuit(final String name)
	{
		verified.remove(name.toLowerCase());
		captchas.remove(name.toLowerCase());
	}

	public void requestVerification(final Player player)
	{
		if (isVerified(player))
			return;
		final Captcha captcha = generator.generateCaptcha();
		captchas.put(player.getName().toLowerCase(), captcha);
		captcha.sendRequest(player);
	}

	@Localized({ "CRAZYCAPTCHA.VERIFICATION.SUCCESS", "CRAZYCAPTCHA.VERIFICATION.FAILED" })
	public void playerVerify(final Player player, final String captcha)
	{
		final Captcha realCaptcha = captchas.remove(player.getName().toLowerCase());
		if (realCaptcha == null)
			return;
		if (realCaptcha.check(captcha))
		{
			logger.log("Captcha", player.getName() + " successfully passed captcha check.");
			verified.add(player.getName().toLowerCase());
			sendLocaleMessage("VERIFICATION.SUCCESS", player);
		}
		else
		{
			Integer fails = verificationFailures.get(player.getName().toLowerCase());
			if (fails == null)
				fails = 0;
			fails++;
			if (fails >= autoKickVerificationFailer)
			{
				player.kickPlayer(locale.getLocaleMessage(player, "VERIFICATION.FAILED"));
				if (autoTempBanVerificationFailer > 0)
					setTempBanned(player, autoTempBanVerificationFailer);
				fails = 0;
			}
			else
			{
				requestVerification(player);
				sendLocaleMessage("VERIFICATION.FAILED", player);
			}
		}
	}

	public void playerReverify(final Player player)
	{
		verified.remove(player.getName().toLowerCase());
		requestVerification(player);
		new CaptchaReminderTask(player, plugin).startTask(reminderInterval);
	}

	public int getAutoKick()
	{
		return autoKick;
	}

	public long getAutoTempBan()
	{
		return autoTempBan;
	}

	public int getAutoKickVerificationFailer()
	{
		return autoKickVerificationFailer;
	}

	public long getAutoTempBanVerificationFailer()
	{
		return autoTempBanVerificationFailer;
	}

	public boolean isAutoKickCommandUsers()
	{
		return autoKickCommandUsers;
	}

	public Map<String, Captcha> getCaptchas()
	{
		return captchas;
	}
}
