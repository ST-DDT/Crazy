package de.st_ddt.crazypromoter;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.commands.CrazyPluginCommandMainMode;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazypromoter.commands.CrazyPromoterCommandCheck;
import de.st_ddt.crazypromoter.data.Promotion;
import de.st_ddt.crazypromoter.listener.CrazyPromoterPlayerListener;
import de.st_ddt.crazypromoter.tasks.CheckTask;
import de.st_ddt.crazyutil.conditions.ConditionList;
import de.st_ddt.crazyutil.conditions.Condition_AND;
import de.st_ddt.crazyutil.conditions.Condition_FALSE;
import de.st_ddt.crazyutil.conditions.player.ConditionPlayerPermissionGroup;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazyPromoter extends CrazyPlugin
{

	private static CrazyPromoter plugin;
	private final CrazyPluginCommandMainMode modeCommand = new CrazyPluginCommandMainMode(this);
	protected ArrayList<Promotion> promotions = new ArrayList<Promotion>();
	private int checkInterval;

	public static CrazyPromoter getPlugin()
	{
		return plugin;
	}

	public CrazyPromoter()
	{
		super();
		registerModes();
	}

	@Localized("CRAZYPROMOTER.MODE.CHANGE $Name$ $Value$")
	private void registerModes()
	{
		modeCommand.addMode(modeCommand.new IntegerMode("checkInterval")
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				sendLocaleMessage("MODE.CHANGE", sender, name, getValue() == -1 ? "disabled" : getValue() + " seconds");
			}

			@Override
			public Integer getValue()
			{
				return checkInterval;
			}

			@Override
			public void setValue(final Integer newValue) throws CrazyException
			{
				checkInterval = Math.max(newValue, -1);
				saveConfiguration();
			}
		});
	}

	private void registerCommands()
	{
		getCommand("promotioncheck").setExecutor(new CrazyPromoterCommandCheck(this));
	}

	private void registerHooks()
	{
		final PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new CrazyPromoterPlayerListener(this), this);
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
		super.onEnable();
		registerCommands();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void loadConfiguration()
	{
		super.loadConfiguration();
		final ConfigurationSection config = getConfig();
		checkInterval = config.getInt("checkInterval", 60);
		promotions.clear();
		final ConfigurationSection promotionConfig = config.getConfigurationSection("promotions");
		if (promotionConfig == null)
		{
			final Promotion promotion = new Promotion("default");
			promotions.add(promotion);
			final ConditionList<Player> condition = new Condition_AND<Player>();
			promotion.setCondition(condition);
			condition.getConditions().add(new Condition_FALSE<Player>());
			condition.getConditions().add(new ConditionPlayerPermissionGroup("default"));
		}
		else
			for (final String name : promotionConfig.getKeys(false))
				promotions.add(new Promotion(promotionConfig.getConfigurationSection(name)));
		getServer().getScheduler().scheduleAsyncRepeatingTask(this, new CheckTask(plugin), 100, checkInterval * 20 * 60);
	}

	@Override
	public void saveConfiguration()
	{
		final ConfigurationSection config = getConfig();
		config.set("checkInterval", checkInterval);
		config.set("promotions", null);
		for (final Promotion promotion : promotions)
			promotion.save(config, "promotions." + promotion.getName() + ".");
		super.saveConfiguration();
	}

	public void checkStatus()
	{
		for (final Player player : Bukkit.getOnlinePlayers())
			checkStatus(player);
	}

	@Localized("CRAZYPROMOTER.COMMAND.CHECK.SUCCESS $Name$ $Promotion$")
	public boolean checkStatus(final Player player)
	{
		for (final Promotion promotion : promotions)
			if (promotion.isApplyable(player))
			{
				promotion.apply(player);
				broadcastLocaleMessage("COMMAND.CHECK.SUCCESS", player.getName(), promotion.getName());
				return true;
			}
		return false;
	}
}
