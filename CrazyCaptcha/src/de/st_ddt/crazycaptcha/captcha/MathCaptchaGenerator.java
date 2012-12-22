package de.st_ddt.crazycaptcha.captcha;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.st_ddt.crazycaptcha.CrazyCaptcha;
import de.st_ddt.crazyplugin.commands.CrazyCommandTreeExecutor;
import de.st_ddt.crazyplugin.commands.CrazyPluginCommandMainMode;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.paramitrisable.IntegerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;

public final class MathCaptchaGenerator extends AbstractCaptchaGenerator
{

	private final Random random = new Random();
	private final CrazyCommandTreeExecutor<CrazyCaptcha> commands;
	private int max;

	public MathCaptchaGenerator(final CrazyCaptcha plugin, final ConfigurationSection config)
	{
		this(plugin);
		if (config == null)
			max = 50;
		else
			max = config.getInt("max", 50);
	}

	public MathCaptchaGenerator(final CrazyCaptcha plugin, final String[] args) throws CrazyException
	{
		this(plugin);
		final Map<String, Paramitrisable> params = new HashMap<String, Paramitrisable>();
		final IntegerParamitrisable max = new IntegerParamitrisable(6);
		params.put("m", max);
		params.put("max", max);
		ChatHelperExtended.readParameters(args, params, max);
		this.max = max.getValue();
	}

	protected MathCaptchaGenerator(final CrazyCaptcha plugin)
	{
		super(plugin);
		this.commands = new CrazyCommandTreeExecutor<CrazyCaptcha>(plugin);
		final CrazyPluginCommandMainMode modeCommand = new CrazyPluginCommandMainMode(plugin);
		commands.addSubCommand(modeCommand, "mode");
		registerModes(modeCommand);
	}

	@Localized("CRAZYCAPTCHA.GENERATORMODE.CHANGE")
	private void registerModes(final CrazyPluginCommandMainMode modeCommand)
	{
		modeCommand.addMode(modeCommand.new IntegerMode("max")
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				plugin.sendLocaleMessage("GENERATORMODE.CHANGE", sender, name, getValue());
			}

			@Override
			public Integer getValue()
			{
				return max;
			}

			@Override
			public void setValue(final Integer newValue) throws CrazyException
			{
				max = Math.max(newValue, 0);
				plugin.saveConfiguration();
			}
		});
	}

	@Override
	public final String getName()
	{
		return "Math";
	}

	@Override
	public Captcha generateCaptcha()
	{
		return new MathCaptcha();
	}

	@Override
	public CrazyCommandTreeExecutor<CrazyCaptcha> getCommands()
	{
		return commands;
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		config.set(path + "max", max);
	}

	@Override
	public String toString()
	{
		return getName() + " (Max: " + max + ")";
	}

	private class MathCaptcha implements Captcha
	{

		private final String captcha;
		private final String show;

		public MathCaptcha()
		{
			super();
			final int v1 = random.nextInt(max);
			final int v2 = random.nextInt(max);
			captcha = new Integer(v1 + v2).toString();
			show = v1 + " + " + v2;
		}

		@Override
		@Localized("CRAZYCAPTCHA.VERIFICATION.REQUEST.MATH")
		public void sendRequest(final Player player)
		{
			plugin.sendLocaleMessage("VERIFICATION.REQUEST.MATH", player, show);
		}

		@Override
		public boolean check(final String captcha)
		{
			return this.captcha.equals(captcha);
		}
	}
}
