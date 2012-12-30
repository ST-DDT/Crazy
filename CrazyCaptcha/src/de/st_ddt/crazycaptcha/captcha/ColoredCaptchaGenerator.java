package de.st_ddt.crazycaptcha.captcha;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.st_ddt.crazycaptcha.CrazyCaptcha;
import de.st_ddt.crazyplugin.commands.CrazyCommandTreeExecutor;
import de.st_ddt.crazyplugin.commands.CrazyPluginCommandMainMode;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.paramitrisable.ColorParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.EnumParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.IntegerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.MultiParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.StringParamitrisable;

public final class ColoredCaptchaGenerator extends AbstractCaptchaGenerator
{

	private final static ChatColor[] DEFAULTCOLORS = new ChatColor[] { ChatColor.RED, ChatColor.BLUE, ChatColor.YELLOW, ChatColor.GREEN, ChatColor.WHITE };
	private final static String DEFAULTCHARS = "0123456789ABCDEFGHKLMNPRSTUVWXYZ";
	private final static int DEFAULTLENGTH = 10;
	private final Random random = new Random();
	private final CrazyCommandTreeExecutor<CrazyCaptcha> commands;
	private final Set<ChatColor> colors = new HashSet<ChatColor>(16);
	private int length;
	private char[] chars;

	public ColoredCaptchaGenerator(final CrazyCaptcha plugin, final ConfigurationSection config)
	{
		this(plugin);
		if (config == null)
		{
			colors.addAll(Arrays.asList(DEFAULTCOLORS));
			length = DEFAULTLENGTH;
			chars = DEFAULTCHARS.toCharArray();
		}
		else
		{
			final List<String> colorsConfig = config.getStringList("colors");
			if (colorsConfig == null)
				colors.addAll(Arrays.asList(DEFAULTCOLORS));
			else
				for (final String color : colorsConfig)
					colors.add(ChatColor.valueOf(color));
			if (colors.size() == 0)
				colors.addAll(Arrays.asList(DEFAULTCOLORS));
			length = config.getInt("length", DEFAULTLENGTH);
			chars = config.getString("captchaChars", DEFAULTCHARS).toCharArray();
		}
	}

	public ColoredCaptchaGenerator(final CrazyCaptcha plugin, final String[] args) throws CrazyException
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
		final MultiParamitrisable<ChatColor> colors = new MultiParamitrisable<ChatColor>(new ColorParamitrisable(null, true, false));
		params.put("color", colors);
		params.put("colors", colors);
		ChatHelperExtended.readParameters(args, params, length, chars, colors);
		this.colors.addAll(colors.getValue());
		if (this.colors.size() == 0)
			this.colors.addAll(Arrays.asList(DEFAULTCOLORS));
		this.length = length.getValue();
		this.chars = chars.getValue().toCharArray();
	}

	protected ColoredCaptchaGenerator(final CrazyCaptcha plugin)
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
		return "Color";
	}

	@Override
	public Captcha generateCaptcha()
	{
		return new ColorCaptcha();
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
		final List<String> colors = new ArrayList<String>();
		for (final ChatColor color : this.colors)
			colors.add(color.name());
		config.set(path + "color", colors);
		config.set(path + "length", length);
		config.set(path + "chars", new String(chars));
	}

	@Override
	public String toString()
	{
		return getName() + " (Length: " + length + ", Characters: " + new String(chars) + ", Colors: " + ChatHelper.listingString(EnumParamitrisable.getEnumNames(colors)) + ")";
	}

	private class ColorCaptcha implements Captcha
	{

		private final ChatColor color;
		private final String captcha;
		private final String message;

		public ColorCaptcha()
		{
			super();
			final ChatColor[] colorArray = colors.toArray(new ChatColor[colors.size()]);
			color = colorArray[random.nextInt(colorArray.length)];
			final StringBuilder resCaptcha = new StringBuilder(length);
			final int len = chars.length;
			StringBuilder resMessage = null;
			while (resCaptcha.length() == 0)
			{
				resMessage = new StringBuilder(length);
				for (int i = 0; i < length; i++)
				{
					final ChatColor color = colorArray[random.nextInt(colorArray.length)];
					final char character = chars[random.nextInt(len)];
					resMessage.append(color.toString() + character);
					if (this.color == color)
						resCaptcha.append(character);
				}
			}
			message = resMessage.toString();
			captcha = resCaptcha.toString();
		}

		@Override
		@Localized("CRAZYCAPTCHA.VERIFICATION.REQUEST.COLOR $Message$ $ColorName$ $ColorCode$")
		public void sendRequest(final Player player)
		{
			plugin.sendLocaleMessage("VERIFICATION.REQUEST.COLOR", player, message, CrazyLocale.getUnitText("COLOR." + color.name(), player), color.toString());
		}

		@Override
		public boolean check(final String captcha)
		{
			return this.captcha.equals(captcha);
		}
	}
}
