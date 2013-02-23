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
import de.st_ddt.crazyutil.modes.IntegerMode;
import de.st_ddt.crazyutil.modes.Mode;
import de.st_ddt.crazyutil.paramitrisable.IntegerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.StringParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public final class BasicCaptchaGenerator extends AbstractCaptchaGenerator
{

	private final static String DEFAULTCHARS = "0123456789";
	private final static int DEFAULTLENGTH = 6;
	private final Random random = new Random();
	private final CrazyCommandTreeExecutor<CrazyCaptcha> commands;
	private int length;
	private char[] chars;

	public BasicCaptchaGenerator(final CrazyCaptcha plugin, final ConfigurationSection config)
	{
		this(plugin);
		if (config == null)
		{
			length = DEFAULTLENGTH;
			chars = DEFAULTCHARS.toCharArray();
		}
		else
		{
			length = config.getInt("length", DEFAULTLENGTH);
			chars = config.getString("captchaChars", DEFAULTCHARS).toCharArray();
		}
	}

	public BasicCaptchaGenerator(final CrazyCaptcha plugin, final String[] args) throws CrazyException
	{
		this(plugin);
		final Map<String, Paramitrisable> params = new HashMap<String, Paramitrisable>();
		final IntegerParamitrisable length = new IntegerParamitrisable(DEFAULTLENGTH);
		params.put("l", length);
		params.put("len", length);
		params.put("length", length);
		final StringParamitrisable chars = new StringParamitrisable(DEFAULTCHARS);
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
		modeCommand.addMode(new IntegerMode(plugin, "length")
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
		modeCommand.addMode(new Mode<String>(plugin, "chars", String.class)
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
		@Localized("CRAZYCAPTCHA.VERIFICATION.REQUEST.BASIC $Captcha$")
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
