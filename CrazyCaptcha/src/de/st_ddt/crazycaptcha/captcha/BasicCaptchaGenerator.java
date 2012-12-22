package de.st_ddt.crazycaptcha.captcha;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
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
import de.st_ddt.crazyutil.paramitrisable.StringParamitrisable;

public final class BasicCaptchaGenerator extends AbstractCaptchaGenerator
{

	private final Random random = new Random();
	private final CrazyCommandTreeExecutor<CrazyCaptcha> commands;
	private int length;
	private char[] chars;

	public BasicCaptchaGenerator(final CrazyCaptcha plugin, final ConfigurationSection config)
	{
		this(plugin);
		if (config == null)
		{
			length = 6;
			chars = "0123456789".toCharArray();
		}
		else
		{
			length = config.getInt("length", 6);
			chars = config.getString("captchaChars", "0123456789").toCharArray();
		}
	}

	public BasicCaptchaGenerator(final CrazyCaptcha plugin, final String[] args) throws CrazyException
	{
		this(plugin);
		final Map<String, Paramitrisable> params = new HashMap<String, Paramitrisable>();
		final IntegerParamitrisable length = new IntegerParamitrisable(6);
		params.put("l", length);
		params.put("len", length);
		params.put("length", length);
		final StringParamitrisable chars = new StringParamitrisable("0123456789");
		params.put("c", chars);
		params.put("chars", chars);
		params.put("characters", chars);
		ChatHelperExtended.readParameters(args, params, length, chars);
		this.length = length.getValue();
		this.chars = chars.getValue().toCharArray();
	}

	protected BasicCaptchaGenerator(final CrazyCaptcha plugin)
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
		modeCommand.addMode(modeCommand.new IntegerMode("length")
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				plugin.sendLocaleMessage("GENERATORMODE.CHANGE", sender, name, getValue());
			}

			@Override
			public Integer getValue()
			{
				return length;
			}

			@Override
			public void setValue(final Integer newValue) throws CrazyException
			{
				length = Math.max(newValue, 0);
				plugin.saveConfiguration();
			}
		});
		modeCommand.addMode(modeCommand.new Mode<String>("chars", String.class)
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				plugin.sendLocaleMessage("GENERATORMODE.CHANGE", sender, name, getValue());
			}

			@Override
			public String getValue()
			{
				return new String(chars);
			}

			@Override
			public void setValue(final CommandSender sender, final String... args) throws CrazyException
			{
				setValue(StringUtils.join(args, ' '));
				showValue(sender);
			}

			@Override
			public void setValue(final String newValue) throws CrazyException
			{
				chars = newValue.toCharArray();
				plugin.saveConfiguration();
			}
		});
	}

	@Override
	public final String getName()
	{
		return "Basic";
	}

	@Override
	public Captcha generateCaptcha()
	{
		return new BasicCaptcha();
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
		config.set(path + "length", length);
		config.set(path + "chars", new String(chars));
	}

	@Override
	public String toString()
	{
		return getName() + " (Length: " + length + ", Characters: " + new String(chars) + ")";
	}

	private class BasicCaptcha implements Captcha
	{

		private final String captcha;

		public BasicCaptcha()
		{
			super();
			final StringBuilder res = new StringBuilder(length);
			final int len = chars.length;
			for (int i = 0; i < length; i++)
				res.append(chars[random.nextInt(len)]);
			captcha = res.toString();
		}

		@Override
		@Localized("CRAZYCAPTCHA.VERIFICATION.REQUEST.BASIC")
		public void sendRequest(final Player player)
		{
			plugin.sendLocaleMessage("VERIFICATION.REQUEST.BASIC", player, captcha);
		}

		@Override
		public boolean check(final String captcha)
		{
			return this.captcha.equals(captcha);
		}
	}
}
